/*Add More Packages Required by CTS to Whitelist.

Change-Id:I57bae4943e1c171ae1ebca98cd8ea35025967c9b*/
//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/PackageSignatureTest.java b/tests/tests/security/src/android/security/cts/PackageSignatureTest.java
//Synthetic comment -- index cff19af..4dddd5d 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -71,11 +72,22 @@
return wellKnownSignatures;
}

private boolean isWhitelistedPackage(String packageName) {
// Don't check the signatures of CTS test packages on the device.
// devicesetup is the APK CTS loads to collect information needed in the final report
return packageName.startsWith("com.android.cts")
                || packageName.equalsIgnoreCase("android.tests.devicesetup");
}

private static final int DEFAULT_BUFFER_BYTES = 1024 * 4;







