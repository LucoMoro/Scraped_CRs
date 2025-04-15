/*Check More Build Constants

Bug 4018016

Check that the Build constants meet the guidelines in CDD
section 3.2.2.

Change-Id:Idd6ec1a48b2a232772bf84e6d21595a20cebdbb9*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index a0b0459..0a3bebe 100644

//Synthetic comment -- @@ -106,14 +106,56 @@
}
}

    private static final Pattern BOARD_PATTERN =
        Pattern.compile("^([0-9A-Za-z.,_-]+)$");
    private static final Pattern BRAND_PATTERN =
        Pattern.compile("^([0-9A-Za-z.,_-]+)$");
private static final Pattern DEVICE_PATTERN =
Pattern.compile("^([0-9A-Za-z_-]+)$");
    private static final Pattern ID_PATTERN =
        Pattern.compile("^([0-9A-Za-z.,_-]+)$");
    private static final Pattern PRODUCT_PATTERN =
        Pattern.compile("^([0-9A-Za-z.,_-]+)$");
private static final Pattern SERIAL_NUMBER_PATTERN =
Pattern.compile("^([0-9A-Za-z]{0,20})$");
    private static final Pattern TAGS_PATTERN =
        Pattern.compile("^([0-9A-Za-z.,_-]+)$");
    private static final Pattern TYPE_PATTERN =
        Pattern.compile("^([0-9A-Za-z.,_-]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
        // Build.VERSION.* constants tested by BuildVersionTest

        assertTrue(BOARD_PATTERN.matcher(Build.BOARD).matches());

        assertTrue(BRAND_PATTERN.matcher(Build.BRAND).matches());

assertTrue(DEVICE_PATTERN.matcher(Build.DEVICE).matches());

        // Build.FINGERPRINT tested by BuildVersionTest

        assertNotEmpty(Build.HOST);

        assertTrue(ID_PATTERN.matcher(Build.ID).matches());

        assertNotEmpty(Build.MODEL);

        assertTrue(PRODUCT_PATTERN.matcher(Build.PRODUCT).matches());

        assertTrue(SERIAL_NUMBER_PATTERN.matcher(Build.SERIAL).matches());

        assertTrue(TAGS_PATTERN.matcher(Build.TAGS).matches());

        // No format requirements stated in CDD for Build.TIME

        assertTrue(TYPE_PATTERN.matcher(Build.TYPE).matches());

        assertNotEmpty(Build.USER);
    }

    private void assertNotEmpty(String value) {
        assertNotNull(value);
        assertFalse(value.isEmpty());
}
}








//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index 3ffb436..1c3ec8f 100644

//Synthetic comment -- @@ -45,6 +45,10 @@
assertEquals(EXPECTED_SDK, Build.VERSION.SDK_INT);
}

    public void testIncremental() {
        assertNotEmpty(Build.VERSION.INCREMENTAL);
    }

/**
* Verifies {@link Build.FINGERPRINT} follows expected format:
* <p/>
//Synthetic comment -- @@ -75,4 +79,9 @@
// no strict requirement for TAGS
//assertEquals(Build.TAGS, fingerprintSegs[5]);
}

    private void assertNotEmpty(String value) {
        assertNotNull(value);
        assertFalse(value.isEmpty());
    }
}







