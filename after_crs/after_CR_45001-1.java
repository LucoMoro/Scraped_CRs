/*Telephony: Update CF number in EF_CFIS.

When call forwarding is enabled, only status is updated in EF_CFIS. CF number
is not updated. Added support to update CF number as well.

Change-Id:Ia764b872b7837d71ffad206e37e9b261e4db7a83*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index cfcc412..26204e7 100644

//Synthetic comment -- @@ -409,6 +409,16 @@
}

/**
     * Set the voice call forwarding flag for GSM/UMTS and the like SIMs
     *
     * @param line to enable/disable
     * @param enable
     * @param number to which CFU is enabled
     */
    public void setVoiceCallForwardingFlag(int line, boolean enable, String number) {
    }

    /**
* Indicates wether SIM is in provisioned state or not.
* Overridden only if SIM can be dynamically provisioned via OTA.
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..221e86c 100644

//Synthetic comment -- @@ -125,6 +125,7 @@
private String mImei;
private String mImeiSv;
private String mVmNumber;
    private String mSetCfNumber;


// Constructors
//Synthetic comment -- @@ -949,6 +950,7 @@

Message resp;
if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
                mSetCfNumber = dialingNumber;
resp = obtainMessage(EVENT_SET_CALL_FORWARD_DONE,
isCfEnable(commandInterfaceCFAction) ? 1 : 0, 0, onComplete);
} else {
//Synthetic comment -- @@ -1264,7 +1266,7 @@
ar = (AsyncResult)msg.obj;
IccRecords r = mIccRecords.get();
if (ar.exception == null && r != null) {
                    r.setVoiceCallForwardingFlag(1, msg.arg1 == 1, mSetCfNumber);
}
onComplete = (Message) ar.userObj;
if (onComplete != null) {
//Synthetic comment -- @@ -1460,7 +1462,8 @@
} else {
for (int i = 0, s = infos.length; i < s; i++) {
if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                        r.setVoiceCallForwardingFlag(1, (infos[i].status == 1),
                            infos[i].number);
// should only have the one
break;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..a3a2055 100644

//Synthetic comment -- @@ -896,7 +896,7 @@
if ((ar.exception == null) && (msg.arg1 == 1)) {
boolean cffEnabled = (msg.arg2 == 1);
if (mIccRecords != null) {
                        mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled, dialingNumber);
}
}

//Synthetic comment -- @@ -1216,7 +1216,7 @@
== CommandsInterface.SERVICE_CLASS_VOICE) {
boolean cffEnabled = (info.status == 1);
if (mIccRecords != null) {
                mIccRecords.setVoiceCallForwardingFlag(1, cffEnabled, info.number);
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..9ef1525 100755

//Synthetic comment -- @@ -24,6 +24,8 @@
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
//Synthetic comment -- @@ -122,6 +124,13 @@
private static final int CPHS_SST_MBN_MASK = 0x30;
private static final int CPHS_SST_MBN_ENABLED = 0x30;

    // EF_CFIS related constants
    // Spec reference TS 51.011 section 10.3.46.
    private static final int CFIS_BCD_NUMBER_LENGTH_OFFSET = 2;
    private static final int CFIS_TON_NPI_OFFSET = 3;
    private static final int CFIS_ADN_CAPABILITY_ID_OFFSET = 14;
    private static final int CFIS_ADN_EXTENSION_ID_OFFSET = 15;

// ***** Event Constants

private static final int EVENT_APP_READY = 1;
//Synthetic comment -- @@ -434,6 +443,15 @@
*/
@Override
public void setVoiceCallForwardingFlag(int line, boolean enable) {
        setVoiceCallForwardingFlag(line, enable, null);
    }

    /**
     * {@inheritDoc}
     * {@hide}
     */
    @Override
    public void setVoiceCallForwardingFlag(int line, boolean enable, String dialNumber) {

if (line != 1) return; // only line 1 is supported

//Synthetic comment -- @@ -450,8 +468,18 @@
mEfCfis[1] &= 0xfe;
}

                // Update dialNumber if not empty and CFU is enabled.
                // Spec reference for EF_CFIS contents, TS 51.011 section 10.3.46.
                if (enable && !TextUtils.isEmpty(dialNumber)) {
                    Log.i(LOG_TAG,"EF_CFIS: updating cf number, " + dialNumber);
                    byte[] bcdNumber = PhoneNumberUtils.numberToCalledPartyBCD(dialNumber);

                    System.arraycopy(bcdNumber, 0, mEfCfis, CFIS_TON_NPI_OFFSET, bcdNumber.length);

                    mEfCfis[CFIS_BCD_NUMBER_LENGTH_OFFSET] = (byte) (bcdNumber.length);
                    mEfCfis[CFIS_ADN_CAPABILITY_ID_OFFSET] = (byte) 0xFF;
                    mEfCfis[CFIS_ADN_EXTENSION_ID_OFFSET] = (byte) 0xFF;
                }

mFh.updateEFLinearFixed(
EF_CFIS, 1, mEfCfis, null,







