//<Beginning of snippet n. 0>
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref != null) {
        String password = sharedPreferences.getString(key, null);
        pref.setSummary(getMaskedPassword(password));
    }
}

private String getMaskedPassword(String password) {
    return (password == null || password.isEmpty()) ? "********" : "********";
}
//<End of snippet n. 0>