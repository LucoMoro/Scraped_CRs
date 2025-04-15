/*Telephony: Consider EF-SMS for both gsm and cdma

EF-SMS each record length is 176 for GSM and 255 for CDMA.
Consider proper length depending on phone type to avoid crash.

Change-Id:I755ca0fa97745d2f9caa30177209a2efa45df409CRs-Fixed: 385474*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccConstants.java b/src/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 847c883..b6f45ce 100644

//Synthetic comment -- @@ -78,6 +78,7 @@

// SMS record length from TS 51.011 10.5.3
static public final int SMS_RECORD_LENGTH = 176;

static final String MF_SIM = "3F00";
static final String DF_TELECOM = "7F10";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java b/src/java/com/android/internal/telephony/IccSmsInterfaceManager.java
//Synthetic comment -- index 5fef6de..34af78a 100644

//Synthetic comment -- @@ -197,7 +197,12 @@
* @return byte array for the record.
*/
protected byte[] makeSmsRecordData(int status, byte[] pdu) {
        byte[] data = new byte[IccConstants.SMS_RECORD_LENGTH];

// Status bits for this record.  See TS 51.011 10.5.3
data[0] = (byte)(status & 7);
//Synthetic comment -- @@ -205,7 +210,7 @@
System.arraycopy(pdu, 0, data, 1, pdu.length);

// Pad out with 0xFF's.
        for (int j = pdu.length+1; j < IccConstants.SMS_RECORD_LENGTH; j++) {
data[j] = -1;
}








