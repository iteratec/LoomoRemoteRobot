package de.iteratec.slab.segway.remote.robot.listener;

import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.Message;

import de.iteratec.slab.segway.remote.robot.service.LoomoBaseService;

/**
 * Created by mss on 02.02.18.
 */

public class GridMoveCommand extends MessageCommand {

    private static final String TAG = "GridMoveCommand";

    public GridMoveCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        if (message[1].equalsIgnoreCase("reset")) {
            LoomoBaseService.getInstance().resetPosition();
        } else {
            int x = Integer.valueOf(message[1]);
            int y = Integer.valueOf(message[2]);

            LoomoBaseService.getInstance().moveToCoordinate(x, y);
        }

    }
}
