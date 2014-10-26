package com.github.qjcg.soundapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.qjcg.soundapp.common.ARRecorder;
import com.github.qjcg.soundapp.common.Recorder;
import com.github.qjcg.soundapp.utilities.PhoneClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

public class MainActivity extends Activity {

    private final static String FRAGMENT_TAG = "FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       FragmentManager fragmentManager = getFragmentManager();
       RecordSoundFragment fragment = (RecordSoundFragment)fragmentManager.findFragmentByTag(FRAGMENT_TAG);
       if (fragment == null){
          fragment = new RecordSoundFragment();
          fragmentManager.beginTransaction()
                  .add(R.id.rootLayout, fragment, FRAGMENT_TAG)
          .commit();
       }
    }
}
