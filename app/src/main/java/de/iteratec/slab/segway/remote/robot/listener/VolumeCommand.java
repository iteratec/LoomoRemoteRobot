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

    private Speaker mSpeaker = Speaker.getInstance();

    public VolumeCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        Log.i(TAG, "Got Volume Message");

            int newVolume = Integer.parseInt(message[1]);
            Log.i(TAG, "Adjust Volume: " + newVolume);

            try {
                mSpeaker.setVolume(newVolume);
                Log.i(TAG, "Set volume to " + newVolume);

            } catch (Exception e) {
                Log.e(TAG, "Exception during Volume command", e);
            }
    }


}
