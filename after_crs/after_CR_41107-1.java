/*Telephony: Fix decodeCallbackNumber for Cdma CMAS

Fix exception in decodeCallbackNumber for Cdma CMAS by
verifying expected param size.

Change-Id:I687a0ad424b6c1d73c0320b3a776cadf7e8ba180*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/sms/BearerData.java b/src/java/com/android/internal/telephony/cdma/sms/BearerData.java
//Synthetic comment -- index 7e7347f..ed38cd7 100755

//Synthetic comment -- @@ -1327,7 +1327,12 @@
private static boolean decodeCallbackNumber(BearerData bData, BitwiseInputStream inStream)
throws BitwiseInputStream.AccessException, CodingException
{
        final int EXPECTED_PARAM_SIZE = 1 * 8; //at least
int paramBits = inStream.read(8) * 8;
        if (paramBits < EXPECTED_PARAM_SIZE) {
            inStream.skip(paramBits);
            return false;
        }
CdmaSmsAddress addr = new CdmaSmsAddress();
addr.digitMode = inStream.read(1);
byte fieldBits = 4;







