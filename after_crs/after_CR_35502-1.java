/*Added check digit support for MEID

According to 3GPP2 standard, MEID could be 14 hex digits with an
optional check digit. Check digit calculation is adjusted to support
the additional format of MEID.

Change-Id:I7b925a5dc6a4c535f14cb7527ff1e10e4155aa17*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 645eb81..604c503 100644

//Synthetic comment -- @@ -317,7 +317,7 @@
}

private static void assertImeiCheckDigit(String deviceId) {
        int expectedCheckDigit = getLuhnCheckDigit(deviceId.substring(0, 14), 10);
int actualCheckDigit = Character.digit(deviceId.charAt(14), 10);
assertEquals("Incorrect check digit for " + deviceId, expectedCheckDigit, actualCheckDigit);
}
//Synthetic comment -- @@ -331,34 +331,49 @@
private static final int[] DOUBLE_DIGIT_SUM = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

/**
     * Example: DOUBLE_HEX_SUM[C] = 9 because 0xC * 2 = 0x18 => 1 + 8 = 9
     */
    private static final int[] DOUBLE_HEX_SUM = {0, 2, 4, 6, 8, 10, 12, 14, 1, 3, 5, 7, 9, 11, 13, 15};


    /**
* Calculate the check digit by starting from the right, doubling every
     * each digit/hex, summing all the digits including the doubled ones, and
     * finding a number to make the sum divisible by radix.
*
* @param deviceId not including the check digit
     * @param radix the radix
* @return the check digit
*/
    private static int getLuhnCheckDigit(String deviceId, int radix) {
int sum = 0;
int dontDoubleModulus = deviceId.length() % 2;
for (int i = deviceId.length() - 1; i >= 0; --i) {
            int digit = Character.digit(deviceId.charAt(i), radix);
if (i % 2 == dontDoubleModulus) {
sum += digit;
} else {
                if (radix == 10) {
                    sum += DOUBLE_DIGIT_SUM[digit];
                } else if (radix == 16) {
                    sum += DOUBLE_HEX_SUM[digit];
                }
}
}
        sum %= radix;
        return sum == 0 ? 0 : radix - sum;
}

private static void assertCdmaDeviceId(String deviceId) {
        // CDMA device IDs may either be a 14-hex-digit MEID with an
        // optional check digit, or an 8-hex-digit ESN.If it's an ESN,
        // it may not be a pseudo-ESN.
        if (deviceId.length() == 14 || deviceId.length() == 15) {
assertMeidFormat(deviceId);
            if (deviceId.length() == 15) {
                // if the ID is 15 digits, the 15th must be a check digit.
                assertMeidCheckDigit(deviceId);
            }
} else if (deviceId.length() == 8) {
assertHexadecimalEsnFormat(deviceId);
} else {
//Synthetic comment -- @@ -366,6 +381,16 @@
}
}

    private static void assertMeidCheckDigit(String deviceId) {
        int radix = 16;
        if (Pattern.matches("[0-9]{14}", deviceId.substring(0, 14))) {
            radix = 10;
        }
        int expectedCheckDigit = getLuhnCheckDigit(deviceId.substring(0, 14), radix);
        int actualCheckDigit = Character.digit(deviceId.charAt(14), radix);
        assertEquals("Incorrect check digit for " + deviceId, expectedCheckDigit, actualCheckDigit);
    }

private static void assertHexadecimalEsnFormat(String deviceId) {
String esnPattern = "[0-9a-fA-F]{8}";
assertTrue("ESN hex device id " + deviceId + " does not match pattern " + esnPattern,
//Synthetic comment -- @@ -375,8 +400,8 @@
}

private static void assertMeidFormat(String deviceId) {
        // MEID may include the check digit.
        String meidPattern = "[0-9a-fA-F]{14,15}";
assertTrue("MEID device id " + deviceId + " does not match pattern " + meidPattern,
Pattern.matches(meidPattern, deviceId));
}







