package com.github.qjcg.soundapp.common;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        Log.d(LOG_TAG, "Using filter: " + filter + " on file "+ mFileName);
//        mFileName = "/sdcard/soundapp/1.wav";
//        resampleAudioFile(mFileName, mFileName + "_resampled.wav");
        playSound(mFileName);
    }

    protected void playSound(String filename) {
        if (mAudioPlayer != null) {
            onDestroy();
            mAudioPlayer.stop();
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
        if (mReverb != null) {
            mReverb.release();
            mReverb = null;
        }
        mAudioPlayer = MediaPlayer.create(this, Uri.parse(filename));
        try {
            if(mAudioPlayer == null){
                return;
            }
//            mReverb = new PresetReverb(0, mAudioPlayer.getAudioSessionId());
//            mReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
//            mReverb.setEnabled(true);
            //mAudioPlayer.attachAuxEffect(mReverb.getId());
//            mAudioPlayer.setAuxEffectSendLevel(1.0f);

            mAudioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mAudioPlayer != null) {
            mAudioPlayer.stop();
            mAudioPlayer.release();
            mAudioPlayer = null;
        }
        if (mReverb != null) {
            mReverb.release();
            mReverb = null;
        }

        return super.onUnbind(intent);
    }

    /**
     * Dealing with big endian streams
     * http://mobilengineering.blogspot.it/2012/06/audio-mix-and-record-in-android.html
     *
     * @param byte0
     * @param byte1
     * @return a short with the two bytes swapped
     */
    private static short concatBytes(byte byte0, byte byte1) {
        return (short) ((byte1 & 0xff) << 8 | (byte0 & 0xff));
    }

    /**
     * From file to byte[] array
     * http://mobilengineering.blogspot.it/2012/06/audio-mix-and-record-in-android.html
     *
     * @param sample
     * @param swap   should swap bytes?
     * @return
     * @throws IOException
     */
    public static byte[] sampleToByteArray(File sample, boolean swap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sample));
        int BUFFERSIZE = 4096;
        byte[] buffer = new byte[BUFFERSIZE];
        while (bis.read(buffer) != -1) {
            baos.write(buffer);
        }
        byte[] outputByteArray = baos.toByteArray();
        bis.close();
        baos.close();

        if (swap) {
            for (int i = 0; i < outputByteArray.length - 1; i = i + 2) {
                byte byte0 = outputByteArray[i];
                outputByteArray[i] = outputByteArray[i + 1];
                outputByteArray[i + 1] = byte0;
            }
        }

        return outputByteArray;
    }

    /**
     * Read a file and returns its contents as array of shorts
     * http://mobilengineering.blogspot.it/2012/06/audio-mix-and-record-in-android.html
     *
     * @param sample the sample file
     * @param swap   true if we should swap the bytes of short (reading a little-endian file), false otherwise (reading a big-endian file)
     * @return
     * @throws IOException
     */
    public static short[] sampleToShortArray(File sample, boolean swap) throws IOException {
        short[] outputArray = new short[(int) sample.length() / 2];

        byte[] outputByteArray = sampleToByteArray(sample, false);

        for (int i = 0, j = 0; i < outputByteArray.length - 4; i += 2, j++) {
            if (j >= outputArray.length) {
                continue;
            }
            if (swap) {
                outputArray[j] = concatBytes(outputByteArray[i], outputByteArray[i + 1]);
            } else {
                outputArray[j] = concatBytes(outputByteArray[i + 1], outputByteArray[i]);
            }
        }
        return outputArray;
    }

    public static byte[] shortArrayToBytes(short[] audioSamples, boolean swap) {

        byte[] outputByteArray = new byte[audioSamples.length * 2];

        for (int i = 0, j = 0; i < audioSamples.length; i += 2, j++) {
            outputByteArray[i] = (byte) audioSamples[j];
            //todo handle swap and size issues
        }
        return outputByteArray;

    }

    public static void shortArrayToFile(short[] audioSamples, String outputFilename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFilename);
            //todo write each byte to the file stream?
            fos.write(shortArrayToBytes(audioSamples, false));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resampleAudioFile(String inputFilename, String outputFilename) {
        File input = new File(inputFilename);
        try {
            short[] audioSamples = sampleToShortArray(input, false);
            short[] output = new short[audioSamples.length];
            for (int i = 0; i < output.length; i++) {

                float samplef1 = audioSamples[i] / 32768.0f;
//                float samplef2 = audioSamples[i] / 32768.0f;
//                float samplef3 = audioSamples[i] / 32768.0f;

                float mixed = samplef1;
                // reduce the volume a bit:
                mixed *= 0.8;
                // hard clipping
                if (mixed > 1.0f) mixed = 1.0f;
                if (mixed < -1.0f) mixed = -1.0f;
                short outputSample = (short) (mixed * 32768.0f);


                output[i] = outputSample;
            }
            //shortArrayToFile(output, outputFilename);
            AudioRecord ar;
            ar = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                    ARRecorder.RECORDER_SAMPLERATE, ARRecorder.RECORDER_CHANNELS,
                    ARRecorder.RECORDER_AUDIO_ENCODING, ARRecorder.bufSize);
            ar.startRecording();
            ar.read(output, 0, output.length);
            ar.stop();
            ar.release();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
