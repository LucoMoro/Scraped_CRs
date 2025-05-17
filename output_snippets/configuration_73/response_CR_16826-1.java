//<Beginning of snippet n. 0>

// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

// Instance Variables
GsmCallTracker mCT;
GsmServiceStateTracker mSST;
List<SIMRecord> mSIMRecords = new ArrayList<>(); // Initialize to prevent NullPointerException

public String getVoiceMailNumber() {
    String number = null;

    if (mSIMRecords != null && !mSIMRecords.isEmpty()) {
        number = mSIMRecords.get(0).getVoiceMailNumber(); // Assuming we are checking only the first SIMRecord
    }

    // Check for operator-specific customization logic here if applicable

    if (TextUtils.isEmpty(number)) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        number = sp.getString(VM_NUMBER, null); // Getting from preferences with nullable fallback
        
        if (TextUtils.isEmpty(number)) {
            // Notify user for all sources returning empty
            notifyUser("No voicemail number found. Using default voicemail number.");
            number = "default_voicemail_number"; // Set to a default value
        } else {
            // Notify user of the source
            notifyUser("Using voicemail number retrieved from preferences: " + number);
        }
    } else {
        // Notify user of the source
        notifyUser("Using voicemail number retrieved from SIM records: " + number);
    }

    return number;
}

//<End of snippet n. 0>