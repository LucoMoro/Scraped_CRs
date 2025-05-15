//<Beginning of snippet n. 0>
if (bytes == null || bytes.length < offset + 1) {
    return "";
}

boolean prependPlus = false;

if ((bytes[offset] & 0xff) == TOA_International || (bytes[offset] & 0xff) == 0x91) { // 0x91 is a common indication for international
    prependPlus = true;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
assertEquals("17005550020",
PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

b[0] = (byte) 0x91; b[1] = (byte) 0x71; b[2] = (byte) 0x00; b[3] = (byte) 0x55;
b[4] = (byte) 0x05; b[5] = (byte) 0x20; b[6] = (byte) 0xF0;
assertEquals("+17005550020",
PhoneNumberUtils.calledPartyBCDToString(b, 0, 7)); // Ensured that we are also checking for the condition where '+' should be added
//<End of snippet n. 1>