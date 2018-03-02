package de.iteratec.slab.segway.remote.robot.listener;

import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.Message;

import de.iteratec.slab.segway.remote.robot.service.LoomoBaseService;

/**
 * Created by abr on 22.12.17.
 */

public class RawMoveCommand extends MessageCommand {

    private static final String TAG = "RawMoveCommand";

    public RawMoveCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        Log.i(TAG, "move speed: " + message[1]);
        Log.i(TAG, "move angle: " + message[2]);


        float newSpeed = Float.valueOf(message[1]);
        float newAngle = Float.valueOf(message[2]);

        LoomoBaseService.getInstance().move(newSpeed,newAngle);
    }
}
