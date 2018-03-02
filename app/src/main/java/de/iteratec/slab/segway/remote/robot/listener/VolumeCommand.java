package de.iteratec.slab.segway.remote.robot.listener;

import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.Message;
import com.segway.robot.sdk.voice.Speaker;
import com.segway.robot.sdk.voice.tts.TtsListener;

/**
 * Created by sei on 03.01.2018.
 */

public class VolumeCommand extends MessageCommand {

    private static final String TAG = "VolumeCommand";
    private static boolean runTest = false;

    private Speaker mSpeaker = Speaker.getInstance();
    private TtsListener mTtsListener;

    public VolumeCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        Log.i(TAG, "Got Volume Message");

            int newVolume = Integer.parseInt(message[1]);
            Log.i(TAG, "Adjust Volume: " + message[1]);

            try {
                //mSpeaker.setVolume(10);
                mSpeaker.setVolume(newVolume);
                //mSpeaker.speak("Hello world, I am a Segway robot", mTtsListener);

            } catch (Exception e) {
                Log.e(TAG, "Exception during Volume command", e);
            }

            //Volume input is supposed to look like a int value between 1-100 (100 being very loud) etc.
            //int volumeAdjustment = Integer.valueOf(split[1]);
            //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeAdjustment, 0);

    }


}
