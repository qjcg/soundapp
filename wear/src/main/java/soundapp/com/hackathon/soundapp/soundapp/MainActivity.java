package soundapp.com.hackathon.soundapp.soundapp;

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

import com.google.android.gms.internal.b;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {


    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.pcm";
    Recorder recorder = new ServiceRecorder(this);
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
                        if (!recorder.isRecording()) {
                            recorder.startRecording();
                            ((TextView) view).setText(R.string.stop);
                        } else {
                            ((TextView) view).setText(R.string.record);

                            recorder.stopRecording();

                            MediaPlayer player = new MediaPlayer();
                            try {
                                player.setDataSource(mFileName);
                                player.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}
