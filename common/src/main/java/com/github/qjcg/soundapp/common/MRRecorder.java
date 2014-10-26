package com.github.qjcg.soundapp.common;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by niluge on 25/10/14.
 */
public class MRRecorder implements Recorder {
    boolean recording = false;
    private MediaRecorder mRecorder;
    private String mFileName;
    MRRecorder(String fileName) {
        this.mFileName = fileName;
    }

    @Override
    public boolean isRecording() {
        return recording;
    }

    @Override
    public void stopRecording() {
        recording = false;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void startRecording() {
        recording = true;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(MRRecorder.class.getSimpleName(), "prepare() failed");
        }

        mRecorder.start();
    }
}
