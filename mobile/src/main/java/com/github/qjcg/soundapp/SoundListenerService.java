package com.github.qjcg.soundapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
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
            String filename = new Date().getTime() + ".wav";
            final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;

            // write sound to file
            try {
               FileOutputStream os = new FileOutputStream(filePath);
               byte[] buffer = new byte[1024];
               while (is.read(buffer) != -1){
                  os.write(buffer);
               }

               NotificationCompat.Builder mBuilder =
                       new NotificationCompat.Builder(this)
                               .setSmallIcon(R.drawable.ic_launcher)
                               .setContentTitle("Your Sound is Ready")
                               .setContentText(filename);


               Intent resultIntent = new Intent(this, MainActivity.class);
               resultIntent.setAction(Intent.ACTION_VIEW);
               resultIntent.setData(Uri.fromFile(new File(filePath)));

               TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
               stackBuilder.addParentStack(MainActivity.class);
               stackBuilder.addNextIntent(resultIntent);
               PendingIntent resultPendingIntent =
                       stackBuilder.getPendingIntent(
                               0,
                               PendingIntent.FLAG_UPDATE_CURRENT
                       );
               mBuilder.setContentIntent(resultPendingIntent);
               NotificationManager mNotificationManager =
                       (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
               mNotificationManager.notify(0, mBuilder.build());

            } catch (FileNotFoundException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
