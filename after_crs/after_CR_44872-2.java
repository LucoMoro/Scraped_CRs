/*Telephony: Consider EF-SMS for both gsm and cdma

EF-SMS each record length is 176 for GSM and 255 for CDMA.
Consider proper length depending on phone type to avoid crash.

Spec Ref: TS 51.011 10.5.3 for GSM and C.S0023 3.4.27 for CDMA.

Change-Id:I755ca0fa97745d2f9caa30177209a2efa45df409*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccConstants.java b/src/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 847c883..48ee27c 100644

//Synthetic comment -- @@ -78,6 +78,8 @@

// SMS record length from TS 51.011 10.5.3
static public final int SMS_RECORD_LENGTH = 176;
    // SMS record length from C.S0023 3.4.27
    static public final int CDMA_SMS_RECORD_LENGTH = 255;

static final String MF_SIM = "3F00";
static final String DF_TELECOM = "7F10";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java b/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java
//Synthetic comment -- index 525bcd9..806f312 100644

//Synthetic comment -- @@ -205,7 +205,12 @@
* @return byte array for the record.
*/
protected byte[] makeSmsRecordData(int status, byte[] pdu) {
        byte[] data;
        if (PhoneConstants.PHONE_TYPE_GSM == mPhone.getPhoneType()) {
            data = new byte[IccConstants.SMS_RECORD_LENGTH];
        } else {
            data = new byte[IccConstants.CDMA_SMS_RECORD_LENGTH];
        }

// Status bits for this record.  See TS 51.011 10.5.3
data[0] = (byte)(status & 7);
//Synthetic comment -- @@ -213,7 +218,7 @@
System.arraycopy(pdu, 0, data, 1, pdu.length);

// Pad out with 0xFF's.
        for (int j = pdu.length+1; j < data.length; j++) {
data[j] = -1;
}








