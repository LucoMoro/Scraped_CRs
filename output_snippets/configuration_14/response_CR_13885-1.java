//<Beginning of snippet n. 0>
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref != null) {
        String password = sharedPreferences.getString(key, "");
        if ("password_key".equals(key)) {
            pref.setSummary(starifyPassword(password));
        } else {
            pref.setSummary(checkNull(password));
        }
    }
}

private String starifyPassword(String password) {
    return "****";
}
//<End of snippet n. 0>