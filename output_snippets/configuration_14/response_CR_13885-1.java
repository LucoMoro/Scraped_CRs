//<Beginning of snippet n. 0>


public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref != null) {
        String password = sharedPreferences.getString(key, "");
        pref.setSummary(maskPassword(password));
    }
}

private String maskPassword(String password) {
    return password.replaceAll(".", "*");
}

//<End of snippet n. 0>