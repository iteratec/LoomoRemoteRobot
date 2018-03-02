package de.iteratec.slab.segway.remote.robot.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.PoseVLS;
import com.segway.robot.algo.VLSPoseListener;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.algo.minicontroller.ObstacleStateChangedListener;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.locomotion.sbv.StartVLSListener;

/**
 * Created by abr on 22.12.17.
 */

public class LoomoBaseService {

    private static final String TAG = "LoomoBaseService";

    private Base base = null;
    private Context context;
    private Handler timehandler;
    private Runnable lastStop = null;
    private RobotCheckpointListener checkpointListener = null;
    private RobotVLSListener vlsListener = null;

    private float lastXPosition = 0f;
    private float lastYPosition = 0f;

    public static LoomoBaseService instance;

    public static LoomoBaseService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LoomoBaseService instance not initialized yet");
        }
        return instance;
    }

    public LoomoBaseService(Context context) {
        timehandler = new Handler();
        this.context = context;
        initBase();
        this.instance = this;
    }

    public void restartService() {
        initBase();
    }

    public void resetPosition() {
        Log.d(TAG, "Resetting navigation coordinates to 0,0");
        setupNavigationVLS();
        base.cleanOriginalPoint();
        PoseVLS pose2D = base.getVLSPose(-1);
        base.setOriginalPoint(pose2D);

        lastXPosition = 0f;
        lastYPosition = 0f;
        Log.i(TAG, "Reset position");
    }

    public void moveToCoordinate(float x, float y) {
        setupNavigationVLS();
        Log.i(TAG, "Moving to: " + x + " / " + y);
        base.addCheckPoint(x, y);
    }

    private void setupNavigationVLS() {
        setNavControlMode();
        if (checkpointListener == null) {
            checkpointListener = new RobotCheckpointListener();
            base.setOnCheckPointArrivedListener(checkpointListener);
        }
        Log.d(TAG, "is vls started?" + base.isVLSStarted());

        if (!base.isVLSStarted()) {
            Log.d(TAG, "starting VLS");
            if (vlsListener == null) {
                vlsListener = new RobotVLSListener();
                base.setVLSPoseListener(vlsPoseListener);
            }

            base.startVLS(true, true, vlsListener);
            // Wait for VLS listener to finish, otherwise our moves will throw exceptions
            try {
                while (!base.isVLSStarted()) {
                    Log.d(TAG, "Waiting for VLS to get ready...");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // enable obstacle avoidance
            Log.d(TAG, "is obstacle avoidance on? " + base.isUltrasonicObstacleAvoidanceEnabled() + " with distance " + base.getUltrasonicObstacleAvoidanceDistance());
            base.setUltrasonicObstacleAvoidanceEnabled(true);
            base.setUltrasonicObstacleAvoidanceDistance(0.5f);
            base.setObstacleStateChangeListener(obstacleStateChangedListener);
        }
    }

    public void move(float linearVelocity, float angularVelocity) {
        setRawControlMode();
        base.setLinearVelocity(linearVelocity);
        base.setAngularVelocity(angularVelocity);

        if (lastStop != null) {
            timehandler.removeCallbacks(lastStop);
            Log.d(TAG, "removed callback to stop");
        }

        lastStop = new Runnable() {
            @Override
            public void run() {
                base.setLinearVelocity(0);
                base.setAngularVelocity(0);
            }
        };


        timehandler.postDelayed(lastStop, 1000);
        Log.d(TAG, "added callback to stop");
    }

    private void setRawControlMode() {
        if (base.isVLSStarted()) {
            Log.d(TAG, "Stopping VLS");
            base.stopVLS();
        }

        if (base.getControlMode() != Base.CONTROL_MODE_RAW) {
            Log.d(TAG, "Setting control mode to: RAW");
            base.setControlMode(Base.CONTROL_MODE_RAW);
        }
    }

    private void setNavControlMode() {
        if (base.getControlMode() != Base.CONTROL_MODE_NAVIGATION) {
            Log.d(TAG, "Setting control mode to: NAVIGATION");
            base.setControlMode(Base.CONTROL_MODE_NAVIGATION);
        }
    }

    private void initBase() {
        base = Base.getInstance();
        base.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                Log.d(TAG, "Base bind successful");
                base.setControlMode(Base.CONTROL_MODE_NAVIGATION);

                base.setOnCheckPointArrivedListener(new CheckPointStateListener() {
                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, final Pose2D realPose, boolean isLast) {
                        Log.i(TAG, "Position before moving: " + lastXPosition + " / " + lastYPosition);
                        lastXPosition = checkPoint.getX();
                        lastYPosition = checkPoint.getY();
                        Log.i(TAG, "Position after moving: " + lastXPosition + " / " + lastYPosition);
                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {
                        lastXPosition = checkPoint.getX();
                        lastYPosition = checkPoint.getY();
                        Log.i(TAG, "Missed checkpoint: " + lastXPosition + " " + lastYPosition);
                    }
                });

            }

            @Override
            public void onUnbind(String reason) {
                Log.d(TAG, "Base bind failed");
            }
        });
    }

    private ObstacleStateChangedListener obstacleStateChangedListener = new ObstacleStateChangedListener() {
        @Override
        public void onObstacleStateChanged(int ObstacleAppearance) {
            Log.i(TAG, "ObstacleStateChanged " + ObstacleAppearance);
        }
    };

    private class RobotCheckpointListener implements CheckPointStateListener {
        @Override
        public void onCheckPointArrived(CheckPoint checkPoint, final Pose2D realPose, boolean isLast) {
            Log.i(TAG, "Arrived to checkpoint: " + checkPoint);
        }

        @Override
        public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {
            Log.i(TAG, "Missed checkpoint: " + checkPoint);
        }
    }

    private class RobotVLSListener implements StartVLSListener {
        @Override
        public void onOpened() {
            Log.i(TAG, "VLSListener onOpenend");
            base.setNavigationDataSource(Base.NAVIGATION_SOURCE_TYPE_VLS);
        }

        @Override
        public void onError(String errorMessage) {
            Log.i(TAG, "VLSListener error: " + errorMessage);

        }
    }

    private VLSPoseListener vlsPoseListener = new VLSPoseListener() {
        @Override
        public void onVLSPoseUpdate(long timestamp, float pose_x, float pose_y, float pose_theta, float v, float w) {
            Log.d(TAG, "onVLSPoseUpdate() called with: timestamp = [" + timestamp + "], pose_x = [" + pose_x + "], pose_y = [" + pose_y + "], pose_theta = [" + pose_theta + "], v = [" + v + "], w = [" + w + "]");
            Log.d(TAG, "Ultrasonic: " + base.getUltrasonicDistance());
        }
    };

    public float getLastXPosition() {
        return lastXPosition;
    }

    public float getLastYPosition() {
        return lastYPosition;
    }

    public void disconnect() {
        this.base.unbindService();
    }
}
