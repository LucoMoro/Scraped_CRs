//<Beginning of snippet n. 0>

public static final String VM_SIM_IMSI = "vm_sim_imsi_key";
public static final String VM_NUMBER = "vm_number_key";

GsmCallTracker mCT;
GsmServiceStateTracker mSST;
private final Object lock = new Object();

public String getVoiceMailNumber() {
    synchronized (lock) {
        if (mSIMRecords == null) {
            Log.e("VoiceMail", "SIM Records are not available.");
            return getCustomVoiceMailNumber();
        }

        String number = mSIMRecords.getVoiceMailNumber();
        if (TextUtils.isEmpty(number)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            number = sp.getString(VM_NUMBER, null);
        }
        
        if (TextUtils.isEmpty(number)) {
            number = getCustomVoiceMailNumber();
        }

        return validateVoiceMailNumber(number) ? number : null;
    }
}

private String getCustomVoiceMailNumber() {
    // Logic to retrieve the customizable voicemail number
    return "custom_voicemail_number"; // placeholder for actual implementation
}

private boolean validateVoiceMailNumber(String number) {
    // Logic to validate the voicemail number format
    return number != null && number.matches("\\+?[0-9]{1,15}"); // simple validation for a voicemail number
}

//<End of snippet n. 0>