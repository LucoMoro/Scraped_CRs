//<Beginning of snippet n. 0>

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

public class RingModeReceiver extends BroadcastReceiver {
    private static final String TAG = "RingModeReceiver";
    private AudioManager audioManager;
    private int currentRingMode;
    private boolean isRegistered = false;

    public RingModeReceiver(Context context) {
        audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public void registerReceiver(Context context) {
        if (!isRegistered) {
            IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
            context.getApplicationContext().registerReceiver(this, filter);
            isRegistered = true;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction() != null && intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                synchronized (this) {
                    currentRingMode = audioManager.getRingerMode();
                    notifyRingModeChange(); // Method to handle notifying other components
                    Log.d(TAG, "Ringer mode changed: " + currentRingMode);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error receiving ringer mode change", e);
        }
    }

    private void notifyRingModeChange() {
        // Implement a callback or use an event bus to notify interested components
    }

    public int getCurrentRingMode() {
        synchronized (this) {
            return currentRingMode;
        }
    }

    public void unregister(Context context) {
        if (isRegistered) {
            context.getApplicationContext().unregisterReceiver(this);
            isRegistered = false;
        }
    }

    public void onDestroy(Context context) {
        unregister(context);
    }

    public void onStop(Context context) {
        unregister(context);
    }
}

//<End of snippet n. 0>