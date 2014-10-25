package soundapp.com.hackathon.soundapp.soundapp.utilities;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

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
            map.putString("data", "Hey there");

            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(client, putRequest.asPutDataRequest()).await();

            return null;
         }
      }.execute(bytes);

   }
}
