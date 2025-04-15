/*Fix StringTest#testCaseMapping_en_US.

I checked the Unicode SpecialCasing rules, and this behavior is correct.

Bug: 3325799
Change-Id:If9a565cd6e25edc60bb4334ad5ce5b0b014df545*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/StringTest.java b/luni/src/test/java/libcore/java/lang/StringTest.java
//Synthetic comment -- index 99dba49..2c7d3d4 100644

//Synthetic comment -- @@ -228,6 +228,7 @@
static String literal = "[5058, 9962, 1563, 5744]";
}

    private static final String COMBINING_DOT_ABOVE = "\u0307";
private static final String LATIN_CAPITAL_I = "I";
private static final String LATIN_CAPITAL_I_WITH_DOT_ABOVE = "\u0130";
private static final String LATIN_SMALL_I = "i";
//Synthetic comment -- @@ -265,8 +266,9 @@
assertEquals(LATIN_SMALL_DOTLESS_I, LATIN_SMALL_DOTLESS_I.toLowerCase(enUs));

assertEquals(LATIN_CAPITAL_I, LATIN_SMALL_DOTLESS_I.toUpperCase(enUs));
        // http://b/3325799: the RI fails this because it's using an obsolete version of the Unicode rules.
        // Android correctly preserves canonical equivalence. (See the separate test for tr_TR.)
        assertEquals(LATIN_SMALL_I + COMBINING_DOT_ABOVE, LATIN_CAPITAL_I_WITH_DOT_ABOVE.toLowerCase(enUs));
}

public void testEqualsIgnoreCase_tr_TR() {







