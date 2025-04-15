/*The password in the APN settings is shown instead of stars

The password is presented as stars in the edit box, also if one
exits and then enters the APN editor the password is stars.

With this patch the APN password is consistently starified.*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 62856d1..e097854 100644

//Synthetic comment -- @@ -458,7 +458,11 @@
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
Preference pref = findPreference(key);
if (pref != null) {
            if (pref.equals(mPassword)){
                pref.setSummary(starify(sharedPreferences.getString(key, "")));
            } else {
                pref.setSummary(checkNull(sharedPreferences.getString(key, "")));
            }
}
}
}







