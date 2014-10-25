package soundapp.hackathon.com.soundapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by john on 25/10/14.
 */
public class SoundFilterIntentService extends IntentService {
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
        Log.d("blah", "onHandleIntent");
    }
}
