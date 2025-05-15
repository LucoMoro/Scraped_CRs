//<Beginning of snippet n. 0>
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref != null) {
        String maskedPassword = "*****";
        pref.setSummary(maskedPassword);
    }
}
//<End of snippet n. 0>