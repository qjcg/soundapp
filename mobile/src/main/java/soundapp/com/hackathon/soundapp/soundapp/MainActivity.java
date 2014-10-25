package soundapp.com.hackathon.soundapp.soundapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends Activity implements WearClient.Listener {

      WearClient client;

    public void applyFilter1 (View view) {
        Log.d("soundapp", "applyFilter1");
    }
    public void applyFilter2 (View view) {
        Log.d("soundapp", "applyFilter2");
    }
    public void playSound (View view) {
        Log.d("soundapp", "play");
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
