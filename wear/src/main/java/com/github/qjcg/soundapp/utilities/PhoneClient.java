package com.github.qjcg.soundapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class PhoneClient {

   private GoogleApiClient client;

   public PhoneClient(GoogleApiClient client) {
      this.client = client;
   }

   public void sendSound(final String filename){

      new AsyncTask<String, Void, Void>() {

         @Override
         protected Void doInBackground(String... params) {

            String filename = params[0];

            Collection<String> nodes = getNodes();

            for(String nodeId : nodes){

               PutDataMapRequest putRequest = PutDataMapRequest.create("/sound");

               DataMap map = putRequest.getDataMap();
               map.putString("test", "Hey there" + new Date().getTime());

               Asset dataAsset = Asset.createFromUri(Uri.fromFile(new File(filename)));
               map.putAsset("data", dataAsset);

               DataApi.DataItemResult result = Wearable.DataApi.putDataItem(client, putRequest.asPutDataRequest()).await();

               MessageApi.SendMessageResult msgResult = Wearable.MessageApi.sendMessage(
                       client, nodeId,"/sound", null).await();
            }

            return null;
         }
      }.execute(filename);
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
