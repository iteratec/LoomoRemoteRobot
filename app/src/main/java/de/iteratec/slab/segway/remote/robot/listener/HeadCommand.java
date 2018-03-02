package de.iteratec.slab.segway.remote.robot.listener;

import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.Message;
import com.segway.robot.sdk.locomotion.head.Head;

import de.iteratec.slab.segway.remote.robot.service.LoomoHeadService;

/**
 * Created by abr on 22.12.17.
 */

public class HeadCommand extends MessageCommand {

    private static final String TAG = "HeadCommand";

    public HeadCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        float pitchValue = Float.valueOf(message[2]);
        float yawValue = Float.valueOf(message[3]);

        Log.i(TAG, "yawValue: " + yawValue);
        Log.i(TAG, "pitchValue: " + pitchValue);

        if (pitchValue > (3.15F) || pitchValue < (-(3.14F / 2))) {
            Log.e(TAG, "received pitchValue, which is not supported");
        }
        if (yawValue > (3.15F * 0.8) || yawValue < (-(3.15F * 0.8))) {
            Log.e(TAG, "received yawValue, which is not supported");
        }

        try {
            int mode = Head.MODE_SMOOTH_TACKING;
            if (message[1].equalsIgnoreCase("orientation")) {
                mode = Head.MODE_ORIENTATION_LOCK;
            }
            LoomoHeadService.getInstance().move(mode, yawValue, pitchValue);
        } catch (Exception e) {
            Log.e(TAG, "Got Exception during HEAD command", e);
        }

    }
}
