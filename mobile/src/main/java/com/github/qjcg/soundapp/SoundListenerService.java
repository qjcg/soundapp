package com.github.qjcg.soundapp;

import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SoundListenerService extends WearableListenerService{

   @Override
   public void onDataChanged(DataEventBuffer dataEvents) {

      for (DataEvent dataEvent : dataEvents){
         if(dataEvent.getDataItem().getUri().getPath().equals("/sound")){
            final DataMap result = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();

            GoogleApiClient client = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();

            ConnectionResult connectionResult =
                    client.blockingConnect(30, TimeUnit.SECONDS);

            if (!connectionResult.isSuccess()){
               Log.e("soundapp", "google api connection failed");
               return;
            }

            InputStream is = Wearable.DataApi.getFdForAsset(client, result.getAsset("data")).await().getInputStream();
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
         }
      }
   }
}
