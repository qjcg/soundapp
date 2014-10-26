package com.github.qjcg.soundapp.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class PhoneClient {

   private GoogleApiClient client;

   public PhoneClient(GoogleApiClient client) {
      this.client = client;
   }

   public void sendSound(final byte[] bytes){

      new AsyncTask<byte[], Void, Void>() {

         @Override
         protected Void doInBackground(byte[]... params) {

            byte[] bytes = params[0];

            PutDataMapRequest putRequest = PutDataMapRequest.create("/sound");

            DataMap map = putRequest.getDataMap();
            map.putString("data", "Hey there" + new Date().getTime());

            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(client, putRequest.asPutDataRequest()).await();

            Collection<String> nodes = getNodes();

            for(String nodeId : nodes){
               MessageApi.SendMessageResult msgResult = Wearable.MessageApi.sendMessage(
                       client, nodeId,"/sound", null).await();
            }

            return null;
         }
      }.execute(bytes);
   }

   private Collection<String> getNodes() {
      HashSet<String> results= new HashSet<String>();
      NodeApi.GetConnectedNodesResult nodes =
              Wearable.NodeApi.getConnectedNodes(client).await();
      for (Node node : nodes.getNodes()) {
         results.add(node.getId());
      }
      return results;
   }
}
