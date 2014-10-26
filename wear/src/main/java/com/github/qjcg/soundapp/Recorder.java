package com.github.qjcg.soundapp;

/**
 * Created by niluge on 25/10/14.
 */
public interface Recorder {
    boolean isRecording();

    void stopRecording();

    void startRecording();
}
