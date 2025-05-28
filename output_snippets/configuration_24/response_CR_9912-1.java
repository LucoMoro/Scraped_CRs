//<Beginning of snippet n. 0>


public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";

/**
 * The type of the the phone number.
 * <P>Type: INTEGER (int)</P>
 */
public static final String TYPE = "type";

public static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 101;

public static void requestPhoneStatePermission(Activity activity) {
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
    }
}

public static void handlePhoneNumber(String phoneNumber) {
    if (phoneNumber != null && isValidPhoneNumber(phoneNumber)) {
        String encryptedPhoneNumber = encryptPhoneNumber(phoneNumber);
        storeSecurely(encryptedPhoneNumber);
    } else {
        Log.e("PhoneNumberHandling", "Invalid phone number: " + phoneNumber);
    }
}

private static boolean isValidPhoneNumber(String phoneNumber) {
    return phoneNumber.matches("^\\+?[0-9]{1,3}?[-.\\s]?\\(?[0-9]{1,4}?\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9}$");
}

private static String encryptPhoneNumber(String phoneNumber) {
    try {
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(phoneNumber.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    } catch (Exception e) {
        Log.e("EncryptionError", "Error encrypting phone number: " + e.getMessage());
        return phoneNumber;
    }
}

private static void storeSecurely(String encryptedPhoneNumber) {
    SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
            "secure_prefs",
            MasterKey.Builder(activity).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            activity,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    );
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(CONTENT_ITEM_TYPE, encryptedPhoneNumber);
    editor.apply();
}

//<End of snippet n. 0>