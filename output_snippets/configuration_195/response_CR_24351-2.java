//<Beginning of snippet n. 0>

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class RingModeReceiver extends BroadcastReceiver {
    private AudioManager audioManager;
    private Context context;
    private static final String CHANNEL_ID = "ringer_mode_notifications";

    public RingModeReceiver(Context context) {
        this.context = context.getApplicationContext();
        this.audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        createNotificationChannel();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())) {
            int ringerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, AudioManager.RINGER_MODE_NORMAL);
            triggerNotification(ringerMode);
        }
    }

    private void triggerNotification(int ringerMode) {
        String message = "Ringer mode changed to: " + (ringerMode == AudioManager.RINGER_MODE_SILENT ? "Silent" : (ringerMode == AudioManager.RINGER_MODE_VIBRATE ? "Vibrate" : "Normal"));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Ringer Mode Change")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(1, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Ringer Mode Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(this);
    }
}

// Register the receiver in appropriate lifecycle methods, such as onStart or onResume of your activity:
// RingModeReceiver ringModeReceiver = new RingModeReceiver(context);
// ringModeReceiver.registerReceiver(context);
// Ensure to unregister in onStop or onPause:
// ringModeReceiver.unregisterReceiver(context);

//<End of snippet n. 0>