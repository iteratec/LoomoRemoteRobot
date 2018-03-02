package de.iteratec.slab.segway.remote.robot.service;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.EditText;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.voice.Speaker;
import com.segway.robot.sdk.voice.VoiceException;
import com.segway.robot.sdk.voice.tts.TtsListener;

/**
 * Created by abr on 22.12.17.
 */

public class LoomoSpeakService implements TextToSpeech.OnInitListener {

    private EditText txtText;

    MediaPlayer player = new MediaPlayer();

    @Override
    public void onInit(int status) {
    }

    private static final String TAG = "LoomoSpeakService";
    private Speaker speaker;
    private Context context;

    private TtsListener ttsListener = new TtsListener() {
        @Override
        public void onSpeechStarted(String word) {
            Log.d(TAG, "onSpeechStarted with " + word);
        }

        @Override
        public void onSpeechFinished(String word) {
            Log.d(TAG, "onSpeechFinished with " + word);
        }

        @Override
        public void onSpeechError(String word, String reason) {
            Log.d(TAG, "onSpeechError with " + word);
        }
    };

    public static LoomoSpeakService instance;

    public static LoomoSpeakService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LoomoSpeakService instance not initialized yet");
        }
        return instance;
    }

    public LoomoSpeakService(Context context) {
        this.context = context;
        init();
        this.instance = this;
    }

    public void restartService() {
        init();
    }

    public void playVoice() {
        try {
            AssetManager assetManager = this.context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("beepboop.mp4");
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endVoice() {
        if (player.isPlaying()) {
            player.stop();
            player.reset();
        }
    }

    public void speak(String sentence) {
        try {
            Log.d(TAG, "sending sentence to speak: " + sentence);
            speaker.speak(sentence, ttsListener);
        } catch (VoiceException e) {
            Log.e(TAG, "could not send sentence", e);
        }
    }


    private void init() {

        speaker = Speaker.getInstance();
        speaker.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                Log.d(TAG, "Speaker onBind");
            }

            @Override
            public void onUnbind(String reason) {
                Log.d(TAG, "Speaker onUnbind");
            }
        });
    }


    public void disconnect() {
        this.speaker.unbindService();
    }

    //Implement broadcast audio file for sound test

}
