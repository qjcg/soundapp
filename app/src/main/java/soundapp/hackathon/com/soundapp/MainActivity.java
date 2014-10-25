package soundapp.hackathon.com.soundapp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    boolean recording = false;
    AudioRecord audioRecord;
    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.3gp";
    int bufSize = 44100 * 2 * 1 * 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mFileName = getFilesDir().getAbsolutePath() + "/test.wav";
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                stub.findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!recording) {
                            audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize);

                            audioRecord.startRecording();
                            recording = true;
                            ((TextView) view).setText(R.string.stop);
                        } else {
                            ((TextView) view).setText(R.string.record);

                            byte[] buf = new byte[bufSize];
                            audioRecord.read(buf, 0, bufSize);
                            audioRecord.stop();
                            try {
                                new FileOutputStream(mFileName).write(buf, 0, bufSize);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            MediaPlayer player = new MediaPlayer();
                            try {
                                player.setDataSource(mFileName);
                                player.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            recording = false;
                        }
                    }
                });
            }
        });
    }
}
