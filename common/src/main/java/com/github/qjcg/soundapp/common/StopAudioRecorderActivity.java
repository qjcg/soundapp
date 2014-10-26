package com.github.qjcg.soundapp.common;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StopAudioRecorderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.stop_audio_activity);
    }

    public void stopRecording(View view) {
        Intent audio = new Intent(this, AudioRecordingService.class);
        this.stopService(audio);
    }

    @Override
    public void onDestroy() {
        Intent audio = new Intent(this, AudioRecordingService.class);
        this.stopService(audio);
        super.onDestroy();
    }

}
