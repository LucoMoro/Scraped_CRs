/*Removed Quick launch menu when no HW Search key exists

The "Quick launch" menu should only be visible if there
is both a HW keyboard and a HW "Search" key, since the
functionality cannot be used if either is missing.

Change-Id:Ia770d40919d92d0e7a32d1a65a0fe73404af578a*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApplicationSettings.java b/src/com/android/settings/ApplicationSettings.java
//Synthetic comment -- index a919ae8..1aecb86 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

public class ApplicationSettings extends PreferenceActivity implements
DialogInterface.OnClickListener {
//Synthetic comment -- @@ -76,8 +78,9 @@
});
}

        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS) {
            // No hard keyboard, remove the setting for quick launch
Preference quickLaunchSetting = findPreference(KEY_QUICK_LAUNCH);
getPreferenceScreen().removePreference(quickLaunchSetting);
}








//Synthetic comment -- diff --git a/tests/src/com/android/settings/QuickLaunchTests.java b/tests/src/com/android/settings/QuickLaunchTests.java
new file mode 100644
//Synthetic comment -- index 0000000..342035a

//Synthetic comment -- @@ -0,0 +1,71 @@







