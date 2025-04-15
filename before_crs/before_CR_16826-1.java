/*Telephony: Customize voicemail number for operators

When the user reads voicemail number, it will read
is first from SIM, then from preferences, then from
customization. That allows an operator to customize
the voicemail number.

Change-Id:Icf5aad3f5608b82006ee9a97c4486791789a90e8*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 69a7a57..67fdafc 100644

//Synthetic comment -- @@ -96,6 +96,9 @@
// Key used to read/write the SIM IMSI used for storing the voice mail
public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

// Instance Variables
GsmCallTracker mCT;
GsmServiceStateTracker mSST;
//Synthetic comment -- @@ -812,11 +815,34 @@
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







