package com.github.qjcg.soundapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.qjcg.soundapp.common.AudioRecordingService;
import com.github.qjcg.soundapp.common.SoundFilterIntentService;
import com.github.qjcg.soundapp.common.SoundLocation;

import java.net.URI;


public class MainActivity extends Activity {

    String mFileName;
    boolean isRecording = false;
    MediaPlayer mAudioPlayer;

    private SoundLocation mLocation;

    public void applyHappyFilter(View view) {
        Intent i = new Intent(this, SoundFilterIntentService.class);
        i.putExtra(SoundFilterIntentService.EXTRA_FILENAME, mFileName);
        i.putExtra(SoundFilterIntentService.EXTRA_FILTER_TYPE, SoundFilterIntentService.FILTER_HAPPY);
        startService(i);
    }

    public void applyDarkFilter(View view) {
        Intent i = new Intent(this, SoundFilterIntentService.class);
        i.putExtra(SoundFilterIntentService.EXTRA_FILENAME, mFileName);
        i.putExtra(SoundFilterIntentService.EXTRA_FILTER_TYPE, SoundFilterIntentService.FILTER_DARK);
        startService(i);
    }

    public void applyEchoFilter(View view) {
        Intent i = new Intent(this, SoundFilterIntentService.class);
        i.putExtra(SoundFilterIntentService.EXTRA_FILENAME, mFileName);
        i.putExtra(SoundFilterIntentService.EXTRA_FILTER_TYPE, SoundFilterIntentService.FILTER_ECHO);
        startService(i);
    }

    public void playSound(View view) {

        mAudioPlayer = MediaPlayer.create(this, Uri.parse(mFileName));
        try {
            if (mAudioPlayer == null) {
                return;
            }
//            mReverb = new PresetReverb(0, mAudioPlayer.getAudioSessionId());
//            mReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
//            mReverb.setEnabled(true);
            //mAudioPlayer.attachAuxEffect(mReverb.getId());
//            mAudioPlayer.setAuxEffectSendLevel(1.0f);

            mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recordSound(View view) {
        if (!isRecording) {
            mFileName = "/sdcard/soundapp/audio"+ System.currentTimeMillis()+".wav";
            ((ImageButton) view).setImageResource(R.drawable.ic_action_stop);
            Intent i = new Intent(this, AudioRecordingService.class);
            i.putExtra(AudioRecordingService.EXTRA_FILENAME, mFileName);
            startService(i);
            isRecording = true;
        } else {
            Intent i = new Intent(this, AudioRecordingService.class);
            stopService(i);
            ((ImageButton) view).setImageResource(R.drawable.ic_action_mic);
            if (mLocation != null && mLocation.getLocation() != null) {
                Toast.makeText(this, ""
                        + mLocation.getLocation().getLatitude() + " " + mLocation.getLocation().getLongitude() + " +/- " + mLocation.getLocation().getAccuracy(), Toast.LENGTH_LONG).show();
            }
            this.playSound(null);
            isRecording = false;
        }
    }

    public void shareFile(View view){
        if(mFileName == null){
            return;
        }
        Intent i = new Intent();
        Uri myUri = Uri.parse(mFileName);
        i.setAction(Intent.ACTION_SEND);
//        i.setData( myUri);
        i.putExtra(Intent.EXTRA_STREAM, myUri);

        i.setType("audio/wav");
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri dataUri = getIntent().getData();
        if (dataUri != null) {
            mFileName = dataUri.getPath();
        }

        mLocation = new SoundLocation(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mAudioPlayer != null) {
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
        super.onDestroy();
    }
}
