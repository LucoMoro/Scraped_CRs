
//<Beginning of snippet n. 0>


return "";
}

        //Only TON field should be taken in concideration
        if ((bytes[offset] & 0xf0) == (TOA_International & 0xf0)) {
prependPlus = true;
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


assertEquals("17005550020",
PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

        b[0] = (byte) 0x80; b[1] = (byte) 0x71; b[2] = (byte) 0x00; b[3] = (byte) 0x55;
        b[4] = (byte) 0x05; b[5] = (byte) 0x20; b[6] = (byte) 0xF0;
        assertEquals("17005550020",
            PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

        b[0] = (byte) 0x90; b[1] = (byte) 0x71; b[2] = (byte) 0x00; b[3] = (byte) 0x55;
        b[4] = (byte) 0x05; b[5] = (byte) 0x20; b[6] = (byte) 0xF0;
        assertEquals("+17005550020",
            PhoneNumberUtils.calledPartyBCDToString(b, 0, 7));

b[0] = (byte) 0x91; b[1] = (byte) 0x71; b[2] = (byte) 0x00; b[3] = (byte) 0x55;
b[4] = (byte) 0x05; b[5] = (byte) 0x20; b[6] = (byte) 0xF0;
assertEquals("+17005550020",

//<End of snippet n. 1>








