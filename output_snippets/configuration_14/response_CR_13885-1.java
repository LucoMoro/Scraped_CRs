//<Beginning of snippet n. 0>


public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref != null) {
        String value = sharedPreferences.getString(key, "");
        if (key.equals("apn_password")) {
            pref.setSummary("*****"); // Mask the password immediately on retrieval
        } else {
            pref.setSummary(checkNull(value));
        }
    }

    // Ensure password is masked when entering the APN editor
    if (key.equals("apn_password") && !value.isEmpty()) {
        // Logic to mask the password in the UI (not shown in the snippet)
        maskPasswordField("*****");
    }
}

// Method to mask the password
private void maskPasswordField(String maskedPassword) {
    // Implementation to set the password field to maskedPassword
}

//<End of snippet n. 0>