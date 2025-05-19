//<Beginning of snippet n. 0>


public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";

/**
 * The type of the the phone number.
 * <P>Type: INTEGER (int)</P>
 */
public static final String TYPE = "type";

public static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 101;
private static SecretKey secretKey; // Secure key management

static {
    try {
        secretKey = KeyGenerator.getInstance("AES").generateKey(); // Generate a secure key once
    } catch (Exception e) {
        Log.e("KeyGenerationError", "Error generating encryption key: " + e.getMessage());
    }
}

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
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(phoneNumber.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    } catch (Exception e) {
        Log.e("EncryptionError", "Error encrypting phone number: " + e.getMessage());
        return null; // Return null instead of sensitive info
    }
}

private static void storeSecurely(String encryptedPhoneNumber) {
    if (encryptedPhoneNumber != null) {
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
    } else {
        Log.e("StorageError", "Failed to store encrypted phone number: null value");
    }
}

//<End of snippet n. 0>