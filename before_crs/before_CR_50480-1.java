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
        addr.ton = CdmaSmsAddress.TON_UNKNOWN;
byte[] origBytes = null;
        String filteredAddr = filterNumericSugar(address);
        if (filteredAddr != null) {
            origBytes = parseToDtmf(filteredAddr);
        }
        if (origBytes != null) {
            addr.digitMode = DIGIT_MODE_4BIT_DTMF;
addr.numberMode = NUMBER_MODE_NOT_DATA_NETWORK;
            if (address.indexOf('+') != -1) {
                addr.ton = TON_INTERNATIONAL_OR_IP;
}
        } else {
            filteredAddr = filterWhitespace(address);
origBytes = UserData.stringToAscii(filteredAddr);
if (origBytes == null) {
return null;
}
            addr.digitMode = DIGIT_MODE_8BIT_CHAR;
            addr.numberMode = NUMBER_MODE_DATA_NETWORK;
            if (address.indexOf('@') != -1) {
                addr.ton = TON_NATIONAL_OR_EMAIL;
            }
}
addr.origBytes = origBytes;
addr.numberOfDigits = origBytes.length;







