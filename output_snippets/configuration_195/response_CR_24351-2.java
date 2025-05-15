//<Beginning of snippet n. 0>

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

public class RingModeReceiver extends BroadcastReceiver {
    private Context context;

    public RingModeReceiver(Context context) {
        this.context = context;
        registerReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())) {
            int ringerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, AudioManager.RINGER_MODE_NORMAL);
            handleRingerModeChange(ringerMode);
        }
    }

    private void handleRingerModeChange(int ringerMode) {
        // Handle the ringer mode change logic here
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unregisterReceiver() {
        context.unregisterReceiver(this);
    }
}

//<End of snippet n. 0>