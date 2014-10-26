package com.github.qjcg.soundapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.qjcg.soundapp.common.AudioRecordingService;
import com.github.qjcg.soundapp.common.SoundFilterIntentService;
import com.github.qjcg.soundapp.common.SoundLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends Activity implements WearClient.Listener {

    String mFileName;
    boolean isRecording = false;

    WearClient client;
    private SoundLocation mLocation;

    public void applyHappyFilter(View view) {
        Intent i = new Intent(this, SoundFilterIntentService.class);
        i.putExtra(SoundFilterIntentService.EXTRA_FILENAME, "/storage/sdcard0/soundapp/test.wav");
        i.putExtra(SoundFilterIntentService.EXTRA_FILTER_TYPE, SoundFilterIntentService.FILTER_HAPPY);
        startService(i);
    }

    public void applyDarkFilter(View view) {
        Intent i = new Intent(this, SoundFilterIntentService.class);
        i.putExtra(SoundFilterIntentService.EXTRA_FILENAME, "/storage/sdcard0/soundapp/test.wav");
        i.putExtra(SoundFilterIntentService.EXTRA_FILTER_TYPE, SoundFilterIntentService.FILTER_DARK);
        startService(i);
    }

    public void applyEchoFilter(View view) {
        Intent i = new Intent(this, SoundFilterIntentService.class);
        i.putExtra(SoundFilterIntentService.EXTRA_FILENAME, "/storage/sdcard0/soundapp/test.wav");
        i.putExtra(SoundFilterIntentService.EXTRA_FILTER_TYPE, SoundFilterIntentService.FILTER_ECHO);
        startService(i);
    }

    public void playSound(View view) {
        Toast.makeText(this.getApplicationContext(), "playSound", Toast.LENGTH_LONG).show();
    }

    public void recordSound(View view) {
        if (!isRecording) {
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
                Toast.makeText(this, "TODO save location in the audio file metadata and somewhere else too.. (ie a database?)"
                        + mLocation.getLocation().getLatitude() + " " + mLocation.getLocation().getLongitude() + " +/- " + mLocation.getLocation().getAccuracy(), Toast.LENGTH_LONG).show();
            }
            this.playSound(null);
            isRecording = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new WearClient(new GoogleApiClient.Builder(this), this);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest" + System.currentTimeMillis() + ".mp3";
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
    public void onSoundReceived(final DataMap result) {

       InputStream is = Wearable.DataApi.getFdForAsset(client.getClient(), result.getAsset("data")).await().getInputStream();
       final String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + new Date().getTime() + ".wav";

       // write sound to file
       try {
          FileOutputStream os = new FileOutputStream(filename);
          byte[] buffer = new byte[1024];
          while (is.read(buffer) != -1){
             os.write(buffer);
          }

       } catch (FileNotFoundException e) {
          e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
       }

       // write save file name to preferences
       SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
       Set<String> files = prefs.getStringSet("sounds", new TreeSet<String>());
       files.add(filename);
       prefs.edit().putStringSet("sounds", files);

       this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "file should be saved to " + filename, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
