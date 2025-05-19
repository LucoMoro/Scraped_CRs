//<Beginning of snippet n. 0>
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

public class RingModeReceiver extends BroadcastReceiver {
    private int mResultCode;
    private Sync mSync = new Sync();
    private RingModeChangeListener mListener;

    public interface RingModeChangeListener {
        void onRingModeChanged(int ringerMode);
    }

    public void setRingModeChangeListener(RingModeChangeListener listener) {
        mListener = listener;
    }

    private static class Sync {
        private boolean notified;

        public synchronized void notifyResult() {
            notified = true;
            notifyAll();
        }

        public synchronized void reset() {
            notified = false;
        }

        public synchronized boolean isNotified() {
            return notified;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            try {
                int ringerMode = audioManager.getRingerMode();
                if (mListener != null) {
                    mListener.onRingModeChanged(ringerMode);
                }
                mSync.notifyResult();
            } catch (Exception e) {
                Log.e("RingModeReceiver", "Error retrieving ringer mode", e);
                if (mListener != null) {
                    mListener.onRingModeChanged(-1); // Indicates an error state
                }
            }
        }
    }

    // Lifecycle management methods
    public void register(Context context) {
        context.getApplicationContext().registerReceiver(this, new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION));
    }

    public void unregister(Context context) {
        context.getApplicationContext().unregisterReceiver(this);
    }
}

// In your AndroidManifest.xml, register the receiver:
// <receiver android:name=".RingModeReceiver">
//    <intent-filter>
//        <action android:name="android.media.RINGER_MODE_CHANGED" />
//    </intent-filter>
// </receiver>
//<End of snippet n. 0>