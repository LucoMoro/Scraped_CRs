
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>








