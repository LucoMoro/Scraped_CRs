/*remove debug logging acedently commited

Change-Id:Icb32422fbb7126a9c0f88560aa9daacf9121f297*/
//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarPreferenceActivity.java b/src/com/android/calendar/CalendarPreferenceActivity.java
//Synthetic comment -- index f58a492..6890aac 100644

//Synthetic comment -- @@ -33,8 +33,6 @@
public class CalendarPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
private static final String BUILD_VERSION = "build_version";

    private static final String TAG = "CalendarPreferenceActivity";

// The name of the shared preferences file. This name must be maintained for historical
// reasons, as it's what PreferenceManager assigned the first time the file was created.
static final String SHARED_PREFS_NAME = "com.android.calendar_preferences";
//Synthetic comment -- @@ -118,7 +116,6 @@
if (key.equals(KEY_ALERTS_TYPE)) {
updateChildPreferences();
}
	Log.v(TAG, "onSharedPreferenceChanged:" + this.getPackageName());
	BackupManager.dataChanged(this.getPackageName());
}








