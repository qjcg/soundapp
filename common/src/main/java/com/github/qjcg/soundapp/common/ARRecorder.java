package com.github.qjcg.soundapp.common;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by niluge on 25/10/14.
 */
public class ARRecorder implements Recorder {
    boolean recording = false;
    AudioRecord audioRecord;
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    String mFileName;

    public ARRecorder(String fileName) {
        this.mFileName = fileName;
    }

    int bufSize = 1024 * 4;// AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    private Thread recordingThread;

    @Override
    public boolean isRecording() {
        return recording;
    }

    @Override
    public void stopRecording() {
        // stops the recording activity
        if (audioRecord != null) {
            recording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }
    @Override
    public void startRecording() {

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufSize);

        audioRecord.startRecording();
        recording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte

        byte bytes[] = new byte[bufSize];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(mFileName + ".pcm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (recording) {
            // gets the voice output from microphone to byte format

            int read = audioRecord.read(bytes, 0, bytes.length);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                if (read > 0) {
                    os.write(bytes, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Recording finished, convert to wav
        try {
            os = new FileOutputStream(mFileName);
            InputStream is = new FileInputStream(mFileName + ".pcm");
            WaveHeader header = new WaveHeader(WaveHeader.FORMAT_PCM, (short) 1, 44100, (short) 16, is.available());
            header.write(os);
            byte[] buf = new byte[2048];
            int red = -1;
            while ((red = is.read(buf, 0, buf.length)) > -1) {
                os.write(buf, 0, red);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
