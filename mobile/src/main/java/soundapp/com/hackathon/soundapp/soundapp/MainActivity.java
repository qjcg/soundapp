package soundapp.com.hackathon.soundapp.soundapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;


public class MainActivity extends Activity implements DataApi.DataListener{


    public void applyHappyFilter (View view) {
        Toast.makeText(this.getApplicationContext(), "applyHappyFilter", Toast.LENGTH_LONG).show();
    }

    public void applyDarkFilter (View view) {
        Toast.makeText(this.getApplicationContext(), "applyDarkFilter", Toast.LENGTH_LONG).show();
    }

    public void playSound (View view) {
        Toast.makeText(this.getApplicationContext(), "playSound", Toast.LENGTH_LONG).show();
    }

    public void applyEchoFilter (View view) {
        Toast.makeText(this.getApplicationContext(), "applyEchoFilter", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
   public void onDataChanged(DataEventBuffer dataEvents) {
      for (DataEvent dataEvent : dataEvents){
         if(dataEvent.getDataItem().getUri().getPath().equals("/sound")){
            DataMap result = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
            result.getString("data");
         }
      }
   }
}
