//<Beginning of snippet n. 0>
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class RingModeReceiver extends BroadcastReceiver {
    private int mResultCode;
    private Sync mSync = new Sync();

    private static class Sync {
        private boolean notified;

        void notifyResult() {
            notified = true;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_RINGER_MODE_CHANGED.equals(intent.getAction())) {
            int resultCode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1);
            mSync.notifyResult();
            mResultCode = resultCode;
        }
    }
}
//<End of snippet n. 0>