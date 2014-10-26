package com.github.qjcg.soundapp.common;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by niluge on 25/10/14.
 */
public class ServiceRecorder implements Recorder {

    boolean recording = false;
    Activity context;

    ServiceRecorder(Activity activity) {
        this.context = activity;
    }

    @Override
    public boolean isRecording() {
        return recording;
    }

    @Override
    public void stopRecording() {
        context.sendBroadcast(new Intent("stopRecord"));
        recording = false;
    }

    @Override
    public void startRecording() {
        recording = true;
        context.startService(new Intent(context, AudioRecordingService.class));
    }

}
