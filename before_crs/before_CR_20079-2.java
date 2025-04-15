/*Remove Private API Tests for PhoneNumberUtils

Issue 13363

compareLoosely and compareStrictly are private APIs that should not be
tested in CTS.

Change-Id:I41d5139f0dbe47c32e66014a0523260c4340de64*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index dad3dd6..57e610b 100644

//Synthetic comment -- @@ -159,84 +159,6 @@
}
}

    /**
     * Tests which must be successful both in compareLoosely() and in compareStrictly().
     */
    private void testCompareCommon(final boolean strict) {
        assertTrue(PhoneNumberUtils.compare(null, null, strict));
        assertFalse(PhoneNumberUtils.compare(null, "", strict));
        assertFalse(PhoneNumberUtils.compare("", null, strict));
        assertFalse(PhoneNumberUtils.compare("", "", strict));

        assertTrue(PhoneNumberUtils.compare("911", "911", strict));
        assertFalse(PhoneNumberUtils.compare("911", "18005550911", strict));

        assertTrue(PhoneNumberUtils.compare("+17005554141", "+17005554141", strict));
        assertTrue(PhoneNumberUtils.compare("+17005554141", "+1 (700).555-4141", strict));
        assertTrue(PhoneNumberUtils.compare("+17005554141", "7005554141", strict));
        assertFalse(PhoneNumberUtils.compare("+1 999 7005554141", "+1 7005554141", strict));
        assertTrue(PhoneNumberUtils.compare("011 1 7005554141", "7005554141", strict));
        assertFalse(PhoneNumberUtils.compare("011 11 7005554141", "+17005554141", strict));
        assertTrue(PhoneNumberUtils.compare("+44 207 792 3490", "0 207 792 3490", strict));
        assertFalse(PhoneNumberUtils.compare("+44 207 792 3490", "00 207 792 3490", strict));

        // MMI header should be ignored
        assertFalse(PhoneNumberUtils.compare("+17005554141", "**31#17005554141", strict));
        assertFalse(PhoneNumberUtils.compare("+44 207 792 3490", "+44 (0) 207 792 3490", strict));
        assertFalse(PhoneNumberUtils.compare("+44 207 792 3490", "010 44 207 792 3490", strict));
        assertFalse(PhoneNumberUtils.compare("+44 207 792 3490", "0011 44 207 792 3490", strict));
        assertTrue(PhoneNumberUtils.compare("+444 207 792 3490", "0 207 792 3490", strict));

        // make sure SMS short code comparison for numbers less than 7 digits work correctly.
        // For example, "404-04" and "40404" should match because the dialable portion for both
        // numbers are the same.
        assertTrue(PhoneNumberUtils.compare("404-04", "40404", strict));
    }

    @TestTargetNew(
      level = TestLevel.COMPLETE,
      method = "compare",
      args = {String.class, String.class}
    )
    public void testCompareLoosely() {
        testCompareCommon(false);

        assertTrue(PhoneNumberUtils.compareLoosely("17005554141", "5554141"));
        assertTrue(PhoneNumberUtils.compareLoosely("+17005554141", "**31#+17005554141"));
        assertFalse(PhoneNumberUtils.compareLoosely("+7(095)9100766", "8(095)9100766"));
    }

    @TestTargetNew(
      level = TestLevel.COMPLETE,
      method = "compare",
      args = {String.class, String.class}
    )
    public void testCompareStrictly() {
        testCompareCommon(true);

        // This must be true, since
        // - +7 is russian country calling code
        // - 8 is russian trunk prefix, which should be omitted when being compared to
        //   the number with country calling code.
        // - so, this comparation becomes same as comparation between
        //   "(095)9100766" v.s."(095)9100766", which is definitely true.
        assertTrue(PhoneNumberUtils.compareStrictly("+7(095)9100766", "8(095)9100766"));

        // Test broken caller ID seen on call from Thailand to the US.
        assertTrue(PhoneNumberUtils.compareStrictly("+66811234567", "166811234567"));

        // This is not related to Thailand case. NAMP "1" + region code "661".
        assertTrue(PhoneNumberUtils.compareStrictly("16610001234", "6610001234"));

        assertFalse(PhoneNumberUtils.compareStrictly("080-1234-5678", "+819012345678"));
        assertTrue(PhoneNumberUtils.compareStrictly("650-000-3456", "16500003456"));

        // Phone numbers with Ascii characters, which are common in some countries
        assertFalse(PhoneNumberUtils.compareStrictly("abcd", "bcde"));
        assertTrue(PhoneNumberUtils.compareStrictly("1-800-flowers", "800-flowers"));
        assertFalse(PhoneNumberUtils.compareStrictly("1-800-flowers", "1-800-abcdefg"));
    }

@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,







