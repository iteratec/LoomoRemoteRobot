package de.iteratec.slab.segway.remote.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.iteratec.slab.segway.remote.robot.service.LoomoBaseService;
import de.iteratec.slab.segway.remote.robot.service.LoomoConnectivityService;
import de.iteratec.slab.segway.remote.robot.service.LoomoEmojiService;
import de.iteratec.slab.segway.remote.robot.service.LoomoHeadService;
import de.iteratec.slab.segway.remote.robot.service.LoomoRecognitionService;
import de.iteratec.slab.segway.remote.robot.service.LoomoSpeakService;
import de.iteratec.slab.segway.remote.robot.service.LoomoVisionService;

/**
 * Created by mss on 02.02.18.
 */

public class LoomoIntentReceiver extends BroadcastReceiver {

    private static final String TAG = "LoomoIntentReceiver";

    private static final String TO_SBV = "com.segway.robot.action.TO_SBV";
    private static final String TO_ROBOT = "com.segway.robot.action.TO_ROBOT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received intent:" + intent.getAction());

        switch (intent.getAction()) {
            case TO_SBV:
                stopServices();
                break;
            case TO_ROBOT:
                startServices();
                break;
        }
    }

    private void startServices() {
        Log.i(TAG, "Restarting Loomo Services");
        LoomoConnectivityService.getInstance().restartService();
        LoomoBaseService.getInstance().restartService();
        LoomoEmojiService.getInstance().restartService();
        LoomoRecognitionService.getInstance().restartService();
        LoomoHeadService.getInstance().restartService();
        LoomoSpeakService.getInstance().restartService();
        LoomoVisionService.getInstance().restartService();
    }

    private void stopServices() {
        Log.i(TAG, "Stopping Loomo Services");
        LoomoConnectivityService.getInstance().disconnect();
        LoomoBaseService.getInstance().disconnect();
        LoomoEmojiService.getInstance().disconnect();
        LoomoRecognitionService.getInstance().disconnect();
        LoomoHeadService.getInstance().disconnect();
        LoomoSpeakService.getInstance().disconnect();
        LoomoVisionService.getInstance().disconnect();
    }
}
