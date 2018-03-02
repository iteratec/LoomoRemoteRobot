package de.iteratec.slab.segway.remote.robot.listener;

import com.segway.robot.sdk.baseconnectivity.Message;

/**
 * Created by abr on 22.12.17.
 */

public abstract class MessageCommand {

    protected final String[] message;

    public MessageCommand(String[] message){
        this.message = message;
    }

    public abstract void execute();
}
