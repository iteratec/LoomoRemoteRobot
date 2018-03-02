package de.iteratec.slab.segway.remote.robot.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.connectivity.BufferMessage;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.Frame;
import com.segway.robot.sdk.vision.stream.StreamType;

import java.io.ByteArrayOutputStream;

/**
 * Created by mss on 22.12.17.
 */

public class LoomoVisionService {

    private static final String TAG = "LoomoVisionService";
    private final Context context;

    private Vision vision;

    public static LoomoVisionService instance;

    public static LoomoVisionService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LoomoVisionService instance not initialized yet");
        }
        return instance;
    }

    public LoomoVisionService(Context context) {
        this.context = context;
        init();
        instance = this;
    }

    public void restartService() {
        init();
    }

    private void init() {
        this.vision = Vision.getInstance();
        this.vision.bindService(this.context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                Log.d(TAG, "Vision service bind successfully");
            }

            @Override
            public void onUnbind(String reason) {
                Log.d(TAG, "Vision service unbound");
            }
        });
    }

    public void startTransferringImageStream(final MessageConnection messageConnection) {
        Log.i(TAG, "startTransferringImageStream called");
        /*
        StreamInfo[] infos = this.vision.getActivatedStreamInfo();

        for (StreamInfo info : infos) {
            Log.d(TAG, "Streaminfo: Height: " + info.getHeight() + "Width: " + info.getWidth() + "Fps: " + info.getFps() + "PixelFormat: " + info.getPixelFormat() + "Resolution: " + info.getResolution() + "StreamType: " + info.getStreamType());
        }*/
        this.vision.startListenFrame(StreamType.COLOR, new Vision.FrameListener() {

            private int frameCount = 0;

            @Override
            public void onNewFrame(int streamType, Frame frame) {
                if (frameCount == 0) {
                    Log.d(TAG, "sending frame");

                    try {
                        // Get image frame as bitmap
                        Bitmap bitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(frame.getByteBuffer());

                        // Compress via JPG
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                        byte[] byteArray = os.toByteArray();

                        Log.d(TAG, "Byte size: " + byteArray.length);
                        if (byteArray.length > 1000000) {
                            Log.w(TAG, "Byte size is > 1 MB! Exceeds max sending size.");
                        } else {
                            messageConnection.sendMessage(new BufferMessage(byteArray));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                    frameCount = 0;
                } else {
                    frameCount++;
                }
            }
        });
    }

    public void stopTransferringImageStream() {
        Log.d(TAG, "stopTransferringImageStream called");
        this.vision.stopListenFrame(StreamType.COLOR);
    }

    public void disconnect() {
        this.vision.unbindService();
    }
}
