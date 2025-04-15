/*Enable notifications during an ongoing call

If a phone call is ongoing, the alert will be allowed but only
using a special incall notification tone and no vibration. The tone will
be played on the voice call audio stream.

The requirement behind this is to allow audible SMS/MMS notifications
during phone calls, something that is requested by a number of
European operators.

Change-Id:I89326cb0768cf8b8ffda979d038e4c0788a8b43a*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index f6d3b608..d355cdb 100755

//Synthetic comment -- @@ -40,6 +40,7 @@
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.IRingtonePlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
//Synthetic comment -- @@ -109,6 +110,11 @@
private static final boolean ENABLE_BLOCKED_NOTIFICATIONS = true;
private static final boolean ENABLE_BLOCKED_TOASTS = true;

    private static final int TONE_INCALL_NOTIFICATION_MAX_LENGTH = 1000;

    private ToneGenerator mInCallToneGenerator;
    private final Object mInCallToneGeneratorLock = new Object();

final Context mContext;
final IActivityManager mAm;
final IBinder mForegroundToken = new Binder();
//Synthetic comment -- @@ -541,6 +547,19 @@
mInCall = (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
TelephonyManager.EXTRA_STATE_OFFHOOK));
updateNotificationPulse();
                synchronized (mInCallToneGeneratorLock) {
                    if (mInCall) {
                        if (mInCallToneGenerator == null) {
                            mInCallToneGenerator = new ToneGenerator(
                                    AudioManager.STREAM_VOICE_CALL, ToneGenerator.MAX_VOLUME);
                        }
                    } else {
                        if (mInCallToneGenerator != null) {
                            mInCallToneGenerator.release();
                            mInCallToneGenerator = null;
                        }
                     }
                }
} else if (action.equals(Intent.ACTION_USER_PRESENT)) {
// turn off LED when user passes through lock screen
mNotificationLight.turnOff();
//Synthetic comment -- @@ -1022,46 +1041,52 @@
final boolean useDefaultSound =
(notification.defaults & Notification.DEFAULT_SOUND) != 0;
if (useDefaultSound || notification.sound != null) {
                    if (mInCall) {
                        playInCallNotification();
} else {
                        Uri uri;
                        if (useDefaultSound) {
                            uri = Settings.System.DEFAULT_NOTIFICATION_URI;
                        } else {
                            uri = notification.sound;
                        }
                        boolean looping = (notification.flags & Notification.FLAG_INSISTENT) != 0;
                        int audioStreamType;
                        if (notification.audioStreamType >= 0) {
                            audioStreamType = notification.audioStreamType;
                        } else {
                            audioStreamType = DEFAULT_STREAM_TYPE;
                        }
                        mSoundNotification = r;
                        // do not play notifications if stream volume is 0
                        // (typically because ringer mode is silent).
                        if (audioManager.getStreamVolume(audioStreamType) != 0) {
                            final long identity = Binder.clearCallingIdentity();
                            try {
                                final IRingtonePlayer player = mAudioService.getRingtonePlayer();
                                if (player != null) {
                                    player.playAsync(uri, looping, audioStreamType);
                                }
                            } catch (RemoteException e) {
                            } finally {
                                Binder.restoreCallingIdentity(identity);
}
}
}
}
                if (!mInCall) {
                    // vibrate
                    final boolean useDefaultVibrate =
                        (notification.defaults & Notification.DEFAULT_VIBRATE) != 0;
                    if ((useDefaultVibrate || notification.vibrate != null)
                            && !(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)) {
                        mVibrateNotification = r;

                        mVibrator.vibrate(useDefaultVibrate ? DEFAULT_VIBRATE_PATTERN
                                                            : notification.vibrate,
                                  ((notification.flags & Notification.FLAG_INSISTENT) != 0) ? 0
                                          : -1);
                    }
}
}

//Synthetic comment -- @@ -1089,6 +1114,26 @@
idOut[0] = id;
}

    private void playInCallNotification() {
        new Thread() {
            @Override
            public void run() {
                // If toneGenerator creation fails, just continue the call
                // without playing the notification sound.
                try {
                    synchronized (mInCallToneGeneratorLock) {
                        if (mInCallToneGenerator != null) {
                            mInCallToneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2,
                                    TONE_INCALL_NOTIFICATION_MAX_LENGTH);
                        }
                    }
                } catch (RuntimeException e) {
                    Log.w(TAG, "Exception from ToneGenerator: " + e);
                }
            }
        }.start();
    }

private void sendAccessibilityEvent(Notification notification, CharSequence packageName) {
AccessibilityManager manager = AccessibilityManager.getInstance(mContext);
if (!manager.isEnabled()) {







