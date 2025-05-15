//<Beginning of snippet n. 0>


public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref != null) {
        String value = sharedPreferences.getString(key, "");
        if (key.equals("apn_password")) {
            // Update summary with masked password
            pref.setSummary("****");
        } else {
            pref.setSummary(checkNull(value));
        }
    }
}

// Persist masked state on exit
@Override
protected void onPause() {
    super.onPause();
    SharedPreferences.Editor editor = getSharedPreferences("YourPreferences", MODE_PRIVATE).edit();
    editor.putBoolean("password_masked", true);
    editor.apply();
}

// Restore masked state on resume
@Override
protected void onResume() {
    super.onResume();
    SharedPreferences sharedPreferences = getSharedPreferences("YourPreferences", MODE_PRIVATE);
    boolean isMasked = sharedPreferences.getBoolean("password_masked", false);
    if (isMasked) {
        Preference passwordPref = findPreference("apn_password");
        if (passwordPref != null) {
            passwordPref.setSummary("****");
        }
    }
}

//<End of snippet n. 0>