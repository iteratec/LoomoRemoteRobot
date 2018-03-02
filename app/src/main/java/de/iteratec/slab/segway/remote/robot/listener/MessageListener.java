package de.iteratec.slab.segway.remote.robot.listener;

import android.util.Log;
import android.widget.Toast;

import com.segway.robot.sdk.baseconnectivity.Message;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;

import java.util.Arrays;

import de.iteratec.slab.segway.remote.robot.MainActivity;

/**
 * Created by abr on 22.12.17.
 */

public class MessageListener implements MessageConnection.MessageListener {

    private static String TAG = "MessageListener";
    private final MainActivity activity;

    private Toast messageToast = null;

    public MessageListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onMessageSentError(Message message, String error) {
        Log.d(TAG, "onMessageSentError: " + error + " during message: " + message);
    }

    @Override
    public void onMessageSent(Message message) {
        Log.d(TAG, "onMessageSent: " + message + " was sent successfully!");
    }

    private String[] splitMessage(Message message){
        return message.getContent().toString().split(";");
    }

    @Override
    public void onMessageReceived(final Message message) {
        Log.d(TAG, "onMessageReceived: " + message);

        long startTime = System.currentTimeMillis();

        MessageCommand command = null;
        String[] splitMessage = splitMessage(message);
        String prefix = splitMessage[0];

        Log.i(TAG, "Received " + prefix + " message");
        try {
            switch (prefix){
                case "speak":
                    command = new SpeakCommand(splitMessage);
                    break;
                case "broadcast":
                    command = new BroadcastCommand(splitMessage);
                    break;
                case "move":
                    command = new RawMoveCommand(splitMessage);
                    break;
                case "head":
                    command = new HeadCommand(splitMessage);
                    break;
                case "volume":
                    command = new VolumeCommand(splitMessage);
                    break;
                case "vision":
                    command = new VisionCommand(splitMessage);
                    break;
                case "settings":
                    command = new SettingsCommand(splitMessage, activity);
                    break;
                case "emoji":
                    command = new EmojiCommand(splitMessage);
                    break;
                case "grid_move":
                    command = new GridMoveCommand(splitMessage);
                    break;
                default:
                    Log.w(TAG, "Received unknown message" + Arrays.toString(splitMessage));
            }

            long endTime = System.currentTimeMillis();
            long durationInMs = endTime - startTime;

            if (command != null) {
                command.execute();
                Log.i(TAG, "Executed: " + command.getClass().toString() + " in " + durationInMs);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (messageToast != null) {
                        messageToast.cancel();
                    }
                    messageToast = Toast.makeText(activity, "Got message: " + message.getContent().toString(), Toast.LENGTH_SHORT);
                    messageToast.show();
                }
            });


        } catch (Exception e) {
            Log.w(TAG, "Exception occured during command launch", e);
        }

    }

}
