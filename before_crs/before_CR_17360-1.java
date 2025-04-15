/*expand the device name pattern to include CAPS and -, as used by some OEMs

Change-Id:I8f00553b71bb536e2aec0d20735c6fe03397b2fb*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index ada90ee..e3b9dea 100644

//Synthetic comment -- @@ -107,7 +107,7 @@
}

private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9a-z_]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {







