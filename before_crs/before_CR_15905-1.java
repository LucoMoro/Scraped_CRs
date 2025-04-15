/*Remove unused variable and associated import

Change-Id:I5d83cf68c1604b252e860c6b3258654fcae7a589*/
//Synthetic comment -- diff --git a/src/com/android/settings/Settings.java b/src/com/android/settings/Settings.java
//Synthetic comment -- index c53c2fa..a2816f8 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.telephony.TelephonyManager;

public class Settings extends PreferenceActivity {

//Synthetic comment -- @@ -38,8 +37,6 @@
super.onCreate(savedInstanceState);

addPreferencesFromResource(R.xml.settings);
        
        int activePhoneType = TelephonyManager.getDefault().getPhoneType();

PreferenceGroup parent = (PreferenceGroup) findPreference(KEY_PARENT);
Utils.updatePreferenceToSpecificActivityOrRemove(this, parent, KEY_SYNC_SETTINGS, 0);







