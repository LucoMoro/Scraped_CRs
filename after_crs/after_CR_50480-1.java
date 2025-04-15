/*Telephony: Set digit_mode to 8 bit in case when ton set to 1

C.S0005 requires that digit_mode parameter in mobile originated
cdma sms address needs to be set to 8 bit when type_of_number
parameter is present.

Change-Id:I1bfad9bff5b6e9e683bf9b4b5158ec46f4841328CRs-Fixed: 295542*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/CdmaSmsAddress.java b/src/java/com/android/internal/telephony/cdma/sms/CdmaSmsAddress.java
//Synthetic comment -- index 5f2e561..0f39ae5 100644

//Synthetic comment -- @@ -196,29 +196,44 @@
public static CdmaSmsAddress parse(String address) {
CdmaSmsAddress addr = new CdmaSmsAddress();
addr.address = address;
        addr.digitMode = DIGIT_MODE_4BIT_DTMF; // Default to 4 bit dtmf encoding
        addr.ton = TON_UNKNOWN;
        addr.numberMode = NUMBER_MODE_NOT_DATA_NETWORK;
        addr.numberPlan = NUMBERING_PLAN_UNKNOWN;
byte[] origBytes = null;

        if (address.indexOf('+') != -1) {
            // This is international phone number
            addr.digitMode = DIGIT_MODE_8BIT_CHAR;
            addr.ton = TON_INTERNATIONAL_OR_IP;
addr.numberMode = NUMBER_MODE_NOT_DATA_NETWORK;
            addr.numberPlan = NUMBERING_PLAN_ISDN_TELEPHONY;
        }
        if (address.indexOf('@') != -1) {
            // This is email address
            addr.digitMode = DIGIT_MODE_8BIT_CHAR;
            addr.ton = TON_NATIONAL_OR_EMAIL;
            addr.numberMode = NUMBER_MODE_DATA_NETWORK;
        }

        // A.S0014-C 4.2.40 states: "Prefix or escape digits shall not be included"
        String filteredAddr = filterNumericSugar(address);
        if (addr.digitMode == DIGIT_MODE_4BIT_DTMF) {
            if (filteredAddr != null) {
                origBytes = parseToDtmf(filteredAddr);
}
            if (origBytes == null) {
                // Failed to encode in 4 bit. Try in 8 bit.
                addr.digitMode = DIGIT_MODE_8BIT_CHAR;
            }
        }

        if (addr.digitMode == DIGIT_MODE_8BIT_CHAR) {
            filteredAddr = filterWhitespace(filteredAddr);
origBytes = UserData.stringToAscii(filteredAddr);
if (origBytes == null) {
return null;
}
}
addr.origBytes = origBytes;
addr.numberOfDigits = origBytes.length;







