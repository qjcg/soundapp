package com.github.qjcg.soundapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.qjcg.soundapp.common.ARRecorder;
import com.github.qjcg.soundapp.common.Recorder;
import com.github.qjcg.soundapp.utilities.PhoneClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

public class MainActivity extends Activity {


    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.wav";
    Recorder recorder = new ARRecorder(mFileName);
    PhoneClient client;

    ObjectAnimator recordBtnAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mFileName = getFilesDir().getAbsolutePath() + "/test.wav";
        setContentView(R.layout.activity_main);

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        apiClient.connect();
        client = new PhoneClient(apiClient);


    }

    public void toggleRecording(View view) {
        if (!recorder.isRecording()) {
            recorder.startRecording();
            ((ImageButton) view).setImageResource(R.drawable.ic_action_stop);

           recordBtnAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f).setDuration(500);
           recordBtnAnimator.setRepeatCount(ObjectAnimator.INFINITE);
           recordBtnAnimator.setRepeatMode(ObjectAnimator.REVERSE);
           recordBtnAnimator.start();

        } else {
            ((ImageButton) view).setImageResource(R.drawable.ic_action_mic);

           recordBtnAnimator.cancel();
           ObjectAnimator.ofFloat(view, "alpha", 1).setDuration(500).start();

            recorder.stopRecording();
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(mFileName);
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(View view) {
        client.sendSound(null);
    }
}
