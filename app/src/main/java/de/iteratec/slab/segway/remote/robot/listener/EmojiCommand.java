package de.iteratec.slab.segway.remote.robot.listener;

import com.segway.robot.sdk.baseconnectivity.Message;

import de.iteratec.slab.segway.remote.robot.MainActivity;
import de.iteratec.slab.segway.remote.robot.service.LoomoEmojiService;

/**
 * Created by mss on 24.01.18.
 */

public class EmojiCommand extends MessageCommand {

    public EmojiCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {
        LoomoEmojiService.getInstance().doEmoji(message[1]);
    }
}
