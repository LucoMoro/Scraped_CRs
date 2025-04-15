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

private static final Pattern DEVICE_PATTERN =
Pattern.compile("^([0-9A-Za-z_-]+)$");
private static final Pattern SERIAL_NUMBER_PATTERN =
Pattern.compile("^([0-9A-Za-z]{0,20})$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {
        assertTrue(SERIAL_NUMBER_PATTERN.matcher(Build.SERIAL).matches());
assertTrue(DEVICE_PATTERN.matcher(Build.DEVICE).matches());
}
}








//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index 3ffb436..1c3ec8f 100644

//Synthetic comment -- @@ -45,6 +45,10 @@
assertEquals(EXPECTED_SDK, Build.VERSION.SDK_INT);
}

/**
* Verifies {@link Build.FINGERPRINT} follows expected format:
* <p/>
//Synthetic comment -- @@ -75,4 +79,9 @@
// no strict requirement for TAGS
//assertEquals(Build.TAGS, fingerprintSegs[5]);
}
}







