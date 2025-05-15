
//<Beginning of snippet n. 0>


// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

// Instance Variables
GsmCallTracker mCT;
GsmServiceStateTracker mSST;
}

public String getVoiceMailNumber() {
        // Read from the SIM. If its null, try reading from the shared preference area.
String number = mSIMRecords.getVoiceMailNumber();
if (TextUtils.isEmpty(number)) {
SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
number = sp.getString(VM_NUMBER, null);
}
return number;
}

//<End of snippet n. 0>








