/*Keep keyguard from starting after finished call when screen is on

Previously keyguard would start after a call ended, even though
the screen was on, if the screen had not been turned off since
phone startup.

Change-Id:I03c18a0bb2a58760614645871f4703afb9a0677d*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/KeyguardViewMediator.java b/policy/src/com/android/internal/policy/impl/KeyguardViewMediator.java
//Synthetic comment -- index 02eeedf..5fa6dbf 100644

//Synthetic comment -- @@ -230,7 +230,7 @@

private KeyguardUpdateMonitor mUpdateMonitor;

    private boolean mScreenOn;

// last known state of the cellular connection
private String mPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
//Synthetic comment -- @@ -318,6 +318,8 @@
final ContentResolver cr = mContext.getContentResolver();
mShowLockIcon = (Settings.System.getInt(cr, "show_status_bar_lock", 0) == 1);

        mScreenOn = mPM.isScreenOn();

mLockSounds = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
String soundPath = Settings.System.getString(cr, Settings.System.LOCK_SOUND);
if (soundPath != null) {







