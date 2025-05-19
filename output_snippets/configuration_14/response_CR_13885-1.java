//<Beginning of snippet n. 0>

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializePasswordMasking();
}

private void initializePasswordMasking() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String key = "your_password_key"; // Replace with the actual key
    String password = sharedPreferences.getString(key, "");
    Preference pref = findPreference(key);
    if (pref != null) {
        pref.setSummary(maskPassword(password));
    }
}

@Override
protected void onResume() {
    super.onResume();
    initializePasswordMasking();
}

@Override
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals("your_password_key")) { // Ensure it only affects the password key
        initializePasswordMasking();
    }
}

private String maskPassword(String password) {
    return password.isEmpty() ? "" : password.replaceAll(".", "*");
}

// Ensure password is masked immediately upon retrieval
private String getMaskedPassword() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String key = "your_password_key"; // Replace with the actual key
    return maskPassword(sharedPreferences.getString(key, ""));
}

//<End of snippet n. 0>