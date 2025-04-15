/*Update BuildVersionTest for 2.2 and 2.2.1

Issue 12692

Change-Id:I4f349b58c9765fad77211d5770f04f2b999b3b25*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index 61e1837..eee90c4 100644

//Synthetic comment -- @@ -21,19 +21,28 @@
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
        new HashSet<String>(Arrays.asList("2.2", "2.2.1"));
    private static final int EXPECTED_SDK = 8;

public void testReleaseVersion() {
// Applications may rely on the exact release version
        assertTrue("Your Build.VERSION.RELEASE of " + Build.VERSION.RELEASE
                + " was not one of the following: " + EXPECTED_RELEASES,
                        EXPECTED_RELEASES.contains(Build.VERSION.RELEASE));

        assertEquals("" + EXPECTED_SDK, Build.VERSION.SDK);
        assertEquals(EXPECTED_SDK, Build.VERSION.SDK_INT);
}

/**







