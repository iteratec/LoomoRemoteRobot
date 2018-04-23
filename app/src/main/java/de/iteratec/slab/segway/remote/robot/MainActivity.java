package de.iteratec.slab.segway.remote.robot;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.*;
import android.widget.Toast;
import android.speech.*;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ArrayList;

import de.iteratec.slab.segway.remote.robot.service.LoomoBaseService;
import de.iteratec.slab.segway.remote.robot.service.LoomoConnectivityService;
import de.iteratec.slab.segway.remote.robot.service.LoomoEmojiService;
import de.iteratec.slab.segway.remote.robot.service.LoomoHeadService;
import de.iteratec.slab.segway.remote.robot.service.LoomoRecognitionService;
import de.iteratec.slab.segway.remote.robot.service.LoomoSpeakService;
import de.iteratec.slab.segway.remote.robot.service.LoomoVisionService;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "SegWayMainActivity";
    private TextView statusText;

    private LoomoBaseService loomoBaseService;
    private LoomoSpeakService loomoSpeakService;
    private LoomoHeadService loomoHeadService;
    private LoomoVisionService loomoVisionService;
    private LoomoRecognitionService loomoRecognitionService;
    private LoomoEmojiService loomoEmojiService;
    private LoomoConnectivityService loomoConnectionService;

    private Button recordButton;
    private MediaRecorder mRecorder = null;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = (TextView) findViewById(R.id.status_text);

        recordButton = (Button) findViewById(R.id.button);
        recordButton.setOnClickListener(recordListener);

        // Hides the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/test.3gpp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        initStatus();
        initServices();
    }

    private Boolean recording = true;

    public View.OnClickListener recordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            promptSpeechInput();
        }
    };

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                //RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),"empty",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "here", a);

        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getApplicationContext(),(result.get(0)),
                            Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }


    private void initStatus() {
        String ip = getDeviceIp();
        statusText.setText("My Ip: " + ip);
    }

    private String getDeviceIp() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
        return ip;
    }

    private void initServices() {
        this.loomoConnectionService = new LoomoConnectivityService(this);
        this.loomoBaseService = new LoomoBaseService(getApplicationContext());
        this.loomoSpeakService = new LoomoSpeakService(getApplicationContext());
        this.loomoVisionService = new LoomoVisionService(getApplicationContext());
        this.loomoHeadService = new LoomoHeadService(getApplicationContext());
        this.loomoRecognitionService = new LoomoRecognitionService(getApplicationContext());
        this.loomoEmojiService = new LoomoEmojiService(getApplicationContext(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.loomoConnectionService.disconnect();
        this.loomoEmojiService.disconnect();
        this.loomoBaseService.disconnect();
        this.loomoSpeakService.disconnect();
        this.loomoVisionService.disconnect();
        this.loomoHeadService.disconnect();
        this.loomoRecognitionService.disconnect();

    }
}
