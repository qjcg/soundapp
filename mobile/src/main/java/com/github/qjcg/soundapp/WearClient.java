package com.github.qjcg.soundapp;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class WearClient implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener{

   public interface Listener{
      public void onSoundReceived(final DataMap result);
   }

   private GoogleApiClient client;
   private Listener listener;

   public WearClient(GoogleApiClient.Builder builder, Listener listener) {

      this.client = builder
              .addApi(Wearable.API)
              .addConnectionCallbacks(this)
              .build();
      this.client.connect();
      this.listener = listener;
   }

   @Override
   public void onDataChanged(DataEventBuffer dataEvents) {
      for (DataEvent dataEvent : dataEvents){
         if(dataEvent.getDataItem().getUri().getPath().equals("/sound")){
            final DataMap result = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
            listener.onSoundReceived(result);
         }
      }
   }

   @Override
   public void onConnected(Bundle bundle) {
      Wearable.DataApi.addListener(client, this);
      Wearable.MessageApi.addListener(client, this);
   }

   @Override
   public void onConnectionSuspended(int i) {

   }

   @Override
   public void onMessageReceived(MessageEvent messageEvent) {

   }
}
