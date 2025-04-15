/*Update BuildVersionTest for 2.3 and 2.3.1

Bug 3097462

Change-Id:I52b9fbe0288619e906a657a8ee5992b975b3ad67*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index 7225363..09b1569 100644

//Synthetic comment -- @@ -21,18 +21,26 @@
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

@TestTargetClass(Build.VERSION.class)
public class BuildVersionTest extends TestCase {

private static final String LOG_TAG = "BuildVersionTest";
    private static final Set<String> EXPECTED_RELEASES =
        new HashSet<String>(Arrays.asList("2.3", "2.3.1"));
private static final int EXPECTED_SDK = 9;

public void testReleaseVersion() {
// Applications may rely on the exact release version
        assertTrue("Your Build.VERSION.RELEASE of " + Build.VERSION.RELEASE
                + " was not one of the following: " + EXPECTED_RELEASES,
                        EXPECTED_RELEASES.contains(Build.VERSION.RELEASE));

assertEquals("" + EXPECTED_SDK, Build.VERSION.SDK);
assertEquals(EXPECTED_SDK, Build.VERSION.SDK_INT);
}







