//<Beginning of snippet n. 0>
if ((bytes[offset] & 0xff) == TOA_International) {
    if ((npiValue == UNKNOWN_NPI) || (npiValue == null)) {
        prependPlus = true;
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
assertEquals("17005550020",
    PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

b[0] = (byte) 0x91; 
b[1] = (byte) 0x71; 
b[2] = (byte) 0x00; 
b[3] = (byte) 0x55;
b[4] = (byte) 0x05; 
b[5] = (byte) 0x20; 
b[6] = (byte) 0xF0;
assertEquals("+17005550020",
    PhoneNumberUtils.calledPartyBCDToString(b, 0, 7)); 
//<End of snippet n. 1>