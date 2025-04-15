/*Allow Uppercase and Dashes in Build.DEVICE

Change-Id:I07f24ec7f3fc74aaeaa55eae1f0bb097af59f291*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index ada90ee..62abc5a 100644

//Synthetic comment -- @@ -107,7 +107,7 @@
}

private static final Pattern DEVICE_PATTERN =
        Pattern.compile("^([0-9a-z_]+)$");

/** Tests that check for valid values of constants in Build. */
public void testBuildConstants() {







