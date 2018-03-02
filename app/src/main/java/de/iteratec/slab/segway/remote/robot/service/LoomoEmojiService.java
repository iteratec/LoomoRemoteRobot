package de.iteratec.slab.segway.remote.robot.service;

import android.content.Context;
import android.util.Log;
import android.widget.ViewSwitcher;

import com.segway.robot.sdk.emoji.BaseControlHandler;
import com.segway.robot.sdk.emoji.Emoji;
import com.segway.robot.sdk.emoji.EmojiPlayListener;
import com.segway.robot.sdk.emoji.EmojiView;
import com.segway.robot.sdk.emoji.HeadControlHandler;
import com.segway.robot.sdk.emoji.exception.EmojiException;
import com.segway.robot.sdk.emoji.player.RobotAnimator;
import com.segway.robot.sdk.emoji.player.RobotAnimatorFactory;

import de.iteratec.slab.segway.remote.robot.MainActivity;
import de.iteratec.slab.segway.remote.robot.R;

/**
 * Created by mss on 17.01.18.
 */

public class LoomoEmojiService {

    private static final String TAG = "LoomoEmojiService";

    public static LoomoEmojiService instance;
    private final Context context;
    private final MainActivity activity;
    private final ViewSwitcher viewSwitcher;

    private Emoji emoji;

    public static LoomoEmojiService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LoomoRecognitionService instance not initialized yet");
        }
        return instance;
    }

    public LoomoEmojiService(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;

        this.viewSwitcher = (ViewSwitcher) activity.findViewById(R.id.view_switcher);

        init();
        instance = this;
    }

    public void restartService() {
        init();
    }

    private void init() {
        this.emoji = Emoji.getInstance();
        this.emoji.init(this.context);

        this.emoji.init(activity.getApplicationContext());
        this.emoji.setHeadControlHandler(headControlHandler);
        this.emoji.setBaseControlHandler(baseControlHandler);
    }

    public void doEmoji(String emoji) {
        // lets assume this is always the right view when we use emoji functions
        this.emoji.setEmojiView((EmojiView) this.viewSwitcher.getCurrentView());

        int emojiId = -1;

        switch (emoji) {
            case "look_around":
                emojiId = 1;
                break;
            case "look_comfort":
                emojiId = 2;
                break;
            case "look_curious":
                emojiId = 3;
                break;
            case "look_no":
                emojiId = 4;
                break;
            case "look_up":
                emojiId = 31;
                break;
            case "look_down":
                emojiId = 32;
                break;
            case "look_left":
                emojiId = 33;
                break;
            case "look_right":
                emojiId = 34;
                break;
            case "turn_left":
                emojiId = 35;
                break;
            case "turn_right":
                emojiId = 36;
                break;
            case "turn_around":
                emojiId = 37;
                break;
            case "turn_full":
                emojiId = 38;
                break;
        }

        try {
            if (emojiId != -1) {
                this.emoji.startAnimation(RobotAnimatorFactory.getReadyRobotAnimator(emojiId), emojiPlayListener);
            } else {
                Log.e(TAG, "Received animation String is not valid: " + emoji);
            }
        } catch (EmojiException e) {
            Log.d(TAG, "EmojiException", e);
        }
    }

    private HeadControlHandler headControlHandler = new HeadControlHandler() {
        @Override
        public int getMode() {
            return 0;
        }

        @Override
        public void setMode(int mode) {

        }

        @Override
        public void setWorldPitch(float angle) {

        }

        @Override
        public void setWorldYaw(float angle) {

        }

        @Override
        public float getWorldPitch() {
            return 0;
        }

        @Override
        public float getWorldYaw() {
            return 0;
        }
    };

    private BaseControlHandler baseControlHandler = new BaseControlHandler() {
        @Override
        public void setLinearVelocity(float velocity) {

        }

        @Override
        public void setAngularVelocity(float velocity) {

        }

        @Override
        public void stop() {

        }

        @Override
        public Ticks getTicks() {
            return null;
        }
    };

    private EmojiPlayListener emojiPlayListener = new EmojiPlayListener() {
        @Override
        public void onAnimationStart(RobotAnimator animator) {

        }

        @Override
        public void onAnimationEnd(RobotAnimator animator) {

        }

        @Override
        public void onAnimationCancel(RobotAnimator animator) {

        }
    };

    public void disconnect() {
        // not sure how to do this, since we never bind anything
    }
}
