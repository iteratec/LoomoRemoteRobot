package de.iteratec.slab.segway.remote.robot.listener;

import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.Message;

import de.iteratec.slab.segway.remote.robot.service.LoomoBaseService;
import de.iteratec.slab.segway.remote.robot.service.LoomoSpeakService;

/**
 * Created by abr on 22.12.17.
 */

public class SpeakCommand extends MessageCommand {

    private static final String TAG = "SpeakCommand";

    public SpeakCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        LoomoSpeakService.getInstance().speak(message[1]);
    }
}
