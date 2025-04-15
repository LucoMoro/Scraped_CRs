/*unused import statements removed and also an unused private static variable removed.

Change-Id:Ib9debf1c265be7a52a9944771e0a062d6e0815bd*/




//Synthetic comment -- diff --git a/src/com/android/settings/SoundSettings.java b/src/com/android/settings/SoundSettings.java
//Synthetic comment -- index a735268..76a56ca 100644

//Synthetic comment -- @@ -23,8 +23,6 @@
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
//Synthetic comment -- @@ -35,14 +33,12 @@
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SoundSettings extends PreferenceActivity implements
Preference.OnPreferenceChangeListener {
private static final String TAG = "SoundAndDisplaysSettings";

/** If there is no setting in the provider, use this. */
private static final int FALLBACK_EMERGENCY_TONE_VALUE = 0;

private static final String KEY_SILENT = "silent";
//Synthetic comment -- @@ -176,7 +172,7 @@
1) == 1);

// Control phone vibe independent of silent mode
        int callsVibrateSetting =
mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);

if (vibeInSilent) {







