package com.victor.polskavisa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity implements View.OnClickListener {

    private static final int SPLASH_DELAY = 3000;
    private static final int STOP_SPLASH = 1;
    private Button splashButton;
    private Handler handler;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        splashButton = (Button) findViewById(R.id.splash_button);
        splashButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/halvetica_light.otf"));
        splashButton.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == STOP_SPLASH){
                    stopSplashScreen();
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(STOP_SPLASH);
            }
        }, SPLASH_DELAY);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.splash_button)
            stopSplashScreen();
    }

    private void stopSplashScreen() {
        timer.cancel();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
