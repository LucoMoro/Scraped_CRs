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
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        registerReceiver(context);
    }

    public void registerReceiver(Context context) {
        if (!isRegistered) {
            IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
            context.registerReceiver(this, filter);
            isRegistered = true;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction() != null && intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                synchronized (this) {
                    currentRingMode = audioManager.getRingerMode();
                    // Notify internal state and update logic here
                    Log.d(TAG, "Ringer mode changed: " + currentRingMode);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error receiving ringer mode change", e);
        }
    }

    public int getCurrentRingMode() {
        synchronized (this) {
            return currentRingMode;
        }
    }

    public void unregister(Context context) {
        if (isRegistered) {
            context.unregisterReceiver(this);
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