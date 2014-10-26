package com.github.qjcg.soundapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.qjcg.soundapp.common.SoundFilterIntentService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;


public class MainActivity extends Activity implements WearClient.Listener {

      WearClient client;

    public void applyHappyFilter (View view) {
        Toast.makeText(this.getApplicationContext(), "applyHappyFilter", Toast.LENGTH_LONG).show();
        Intent i  = new Intent(this, SoundFilterIntentService.class);
        startService(i);
    }

    public void applyDarkFilter (View view) {
        Toast.makeText(this.getApplicationContext(), "applyDarkFilter", Toast.LENGTH_LONG).show();
        Intent i  = new Intent(this, SoundFilterIntentService.class);
        startService(i);
    }

    public void playSound (View view) {
        Toast.makeText(this.getApplicationContext(), "playSound", Toast.LENGTH_LONG).show();
    }

    public void recordSound (View view) {
        Toast.makeText(this.getApplicationContext(), "recordSound", Toast.LENGTH_LONG).show();
    }

    public void applyEchoFilter (View view) {
        Toast.makeText(this.getApplicationContext(), "applyEchoFilter", Toast.LENGTH_LONG).show();
        Intent i  = new Intent(this, SoundFilterIntentService.class);
        startService(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       client = new WearClient(new GoogleApiClient.Builder(this), this);
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
   public void onSoundReceived( final DataMap result) {
      this.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            Toast.makeText(MainActivity.this, result.getString("data"), Toast.LENGTH_SHORT).show();
         }
      });
   }
}
