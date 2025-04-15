/*Prevent "Preferences from code" crash on pre-ICS device

Add a Build.SDK_INT check before the SwitchPreference
instantiation block.

Change-Id:I168b0fb128e76eb7134e17bf8030c31981c662e9Signed-off-by: Joseph Judistira <joseph.judistira@gmail.com>*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/preference/PreferencesFromCode.java b/samples/ApiDemos/src/com/example/android/apis/preference/PreferencesFromCode.java
//Synthetic comment -- index 884991b..ecf6734 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
//Synthetic comment -- @@ -55,12 +56,14 @@
checkboxPref.setSummary(R.string.summary_checkbox_preference);
inlinePrefCat.addPreference(checkboxPref);

        // Switch preference
        SwitchPreference switchPref = new SwitchPreference(this);
        switchPref.setKey("switch_preference");
        switchPref.setTitle(R.string.title_switch_preference);
        switchPref.setSummary(R.string.summary_switch_preference);
        inlinePrefCat.addPreference(switchPref);

// Dialog based preferences
PreferenceCategory dialogBasedPrefCat = new PreferenceCategory(this);







