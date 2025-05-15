
//<Beginning of snippet n. 0>


// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

    // The max length of voice mail number
    private static final int VOICEMAIL_NUMBER_MAX = 20;

// Instance Variables
GsmCallTracker mCT;
GsmServiceStateTracker mSST;
}

public String getVoiceMailNumber() {
        // Try reading voice mail number from :
        // 1- SIM card
String number = mSIMRecords.getVoiceMailNumber();

if (TextUtils.isEmpty(number)) {
            // 2- preferences
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
number = sp.getString(VM_NUMBER, null);

            if (TextUtils.isEmpty(number)) {
                // 3- system properties
                number = SystemProperties.get("ro.voicemail.dialing-number");

                // Check that it is a valid number
                if (!TextUtils.isEmpty(number) &&
                    (!number.matches("([0-9+\\*#])*") ||
                            number.length() > VOICEMAIL_NUMBER_MAX)) {
                    Log.e(LOG_TAG, "System Properties get failed.("
                            + "ro.voicemail.dialing-number" + ":" + number + ")");
                    number = null;
                }
            }
        }

        // Convert empty numbers to null
        // so that applications can handle it properly
        if (TextUtils.isEmpty(number)) {
            number = null;
}
return number;
}

//<End of snippet n. 0>








