package com.github.qjcg.soundapp.common;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.StringTokenizer;

public class SoundFilterIntentService extends IntentService {
    private String mFileName;
    private MediaPlayer mAudioPlayer;
    private PresetReverb mReverb;

    public static final String EXTRA_FILENAME = "extra_filename";
    public static final String EXTRA_FILTER_TYPE = "extra_filter_type";

    public static final int FILTER_HAPPY = 1;
    public static final int FILTER_DARK = 2;
    public static final int FILTER_ECHO = 3;

    public static final String LOG_TAG = "soundapp";

    public SoundFilterIntentService(String name) {
        super(name);
    }

    public SoundFilterIntentService() {
        super("SoundFilterIntentService");
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mFileName = intent.getStringExtra(EXTRA_FILENAME);
        if (mFileName == null) {
            Log.e(LOG_TAG, "Missing filename, can't apply a filter!");
            return;
        }

        int filter = intent.getIntExtra(EXTRA_FILTER_TYPE, 1);
        Log.d(LOG_TAG, "Using filter: " + filter);
        playSound(mFileName);
    }

    protected void playSound(String filename) {
        if (mAudioPlayer != null) {
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
        if (mReverb != null) {
            mReverb.release();
            mReverb = null;
        }
        mAudioPlayer = MediaPlayer.create(this, Uri.parse(filename));
        mReverb = new PresetReverb(0, mAudioPlayer.getAudioSessionId());
        mReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
        mReverb.setEnabled(true);
        //mAudioPlayer.attachAuxEffect(mReverb.getId());
        mAudioPlayer.setAuxEffectSendLevel(1.0f);

        mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
         });
    }
}
