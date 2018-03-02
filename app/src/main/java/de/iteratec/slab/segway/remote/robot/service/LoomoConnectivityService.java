package de.iteratec.slab.segway.remote.robot.service;

import android.util.Log;
import android.widget.Toast;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.baseconnectivity.MessageRouter;
import com.segway.robot.sdk.connectivity.RobotMessageRouter;

import de.iteratec.slab.segway.remote.robot.MainActivity;
import de.iteratec.slab.segway.remote.robot.listener.MessageListener;

/**
 * Created by mss on 22.02.18.
 */

public class LoomoConnectivityService {

    private static final String TAG = "LoomoConnectService";

    public static LoomoConnectivityService instance;
    private final MainActivity mainActivity;

    private RobotMessageRouter robotMessageRouter;
    private MessageConnection messageConnection;

    public static LoomoConnectivityService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LoomoConnectivityService instance not initialized yet");
        }
        return instance;
    }

    public LoomoConnectivityService(MainActivity activity) {
        this.mainActivity = activity;
        init();
        instance = this;
    }

    public void restartService() {
        init();
    }


    private void init() {
        this.robotMessageRouter = RobotMessageRouter.getInstance();
        this.robotMessageRouter.bindService(this.mainActivity, bindStateListener);

    }

    private ServiceBinder.BindStateListener bindStateListener = new ServiceBinder.BindStateListener() {
        @Override
        public void onBind() {
            Log.d(TAG, "onBind called");
            try {
                robotMessageRouter.register(messageConnectionListener);
                //statusText.setText("Registered");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUnbind(String reason) {
            Log.d(TAG, "onUnBind called: " + reason);
            //statusText.setText("Unbound");
        }
    };


    private MessageRouter.MessageConnectionListener messageConnectionListener = new MessageRouter.MessageConnectionListener() {
        @Override
        public void onConnectionCreated(final MessageConnection connection) {
            Log.d(TAG, "onConnectionCreated: " + connection.getName());
            messageConnection = connection;
            try {
                messageConnection.setListeners(connectionStateListener, new MessageListener(mainActivity));
            } catch (Exception e) {
                Log.e(TAG, "Could not initialize listener", e);
            }
        }
    };

    private MessageConnection.ConnectionStateListener connectionStateListener = new MessageConnection.ConnectionStateListener() {
        @Override
        public void onOpened() {
            Log.d(TAG, "onOpened called: " + messageConnection.getName());
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, "connected to: " + messageConnection.getName(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public void onClosed(String error) {
            Log.e(TAG, "onClosed called: " + error + ";name=" + messageConnection.getName());
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, "disconnected to: " + messageConnection.getName(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    };


    public MessageConnection getMessageConnection() {
        return messageConnection;
    }

    public void disconnect() {
        this.robotMessageRouter.unbindService();
    }
}
