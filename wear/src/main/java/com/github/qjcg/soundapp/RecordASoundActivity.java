package com.github.qjcg.soundapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class RecordASoundActivity extends Activity {

   private final static String FRAGMENT_TAG = "FRAGMENT_TAG";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      FragmentManager fragmentManager = getFragmentManager();
      RecordSoundFragment fragment = (RecordSoundFragment)fragmentManager.findFragmentByTag(FRAGMENT_TAG);
      if (fragment == null){
         fragment = new RecordSoundFragment();
         Bundle args = new Bundle();
         args.putBoolean(RecordSoundFragment.ARG_START_RECORDING, true);
         fragment.setArguments(args);
         fragmentManager.beginTransaction()
                 .add(R.id.rootLayout, fragment, FRAGMENT_TAG)
                 .commit();
      }
   }
}
