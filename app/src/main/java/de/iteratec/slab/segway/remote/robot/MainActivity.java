package de.iteratec.slab.segway.remote.robot;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = (TextView) findViewById(R.id.status_text);

        // Hides the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        initStatus();
        initServices();
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
