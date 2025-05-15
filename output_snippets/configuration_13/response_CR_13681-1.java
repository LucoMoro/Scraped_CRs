//<Beginning of snippet n. 0>
return "";
}

if ((bytes[offset] & 0xff) == TOA_International) {
    prependPlus = true;
} else if (unknownNpiValue) {
    prependPlus = (bytes[offset] & 0xff) == TOA_ADN; // Ensure we prepend '+' for ADN records with unknown NPI
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
assertEquals("17005550020",
PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

b[0] = (byte) 0x91; b[1] = (byte) 0x71; b[2] = (byte) 0x00; b[3] = (byte) 0x55;
b[4] = (byte) 0x05; b[5] = (byte) 0x20; b[6] = (byte) 0xF0;
assertEquals("+17005550020", PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

// Additional test case for unknown NPI values
b[0] = (byte) 0x91; // Example byte for unknown NPI
b[1] = (byte) 0xFF; // Simulate unknown NPI
b[2] = (byte) 0x00; // Continue test pattern
b[3] = (byte) 0x55;
b[4] = (byte) 0x05; 
b[5] = (byte) 0x20; 
b[6] = (byte) 0xF0;
assertEquals("+17005550020", PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

//<End of snippet n. 1>