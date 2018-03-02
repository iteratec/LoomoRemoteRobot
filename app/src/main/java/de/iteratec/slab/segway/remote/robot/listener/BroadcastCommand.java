package de.iteratec.slab.segway.remote.robot.listener;

import com.segway.robot.sdk.baseconnectivity.Message;

import de.iteratec.slab.segway.remote.robot.service.LoomoSpeakService;

/**
 * Created by sei on 15.02.2018.
 */

public class BroadcastCommand  extends MessageCommand {

    private static final String TAG = "BroadcastCommand";

    public BroadcastCommand(String[] message) { super(message);}

    @Override
    public void execute() {
        if(message[1].equals("start")){
            LoomoSpeakService.getInstance().playVoice();
        } else {
            LoomoSpeakService.getInstance().endVoice();
        }
    }
}
