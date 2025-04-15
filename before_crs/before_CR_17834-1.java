/*CTS Test: expected release version updated.

[Comment]
Test: android.os.cts.BuildVersionTest#testReleaseVersion

The build platform release is set to 2.2.1 and the test is
looking for an expected release of 2.2.
So the cts test case was failing.

This patch will update the expected release version to 2.2.1.

Change-Id:I6581689fb71e7add987d9caa1160a07932c3db81Signed-off-by: Solaiyappan Saravanan <saravanan.s@ti.com>*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index e353be9..61e1837 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
public class BuildVersionTest extends TestCase {

private static final String LOG_TAG = "BuildVersionTest";
    private static final String EXPECTED_RELEASE = "2.2";
private static final String EXPECTED_SDK = "8";

public void testReleaseVersion() {







