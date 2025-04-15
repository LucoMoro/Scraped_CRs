/*Update BuildVersionTest for 2.3.3

Bug 3486016

Change-Id:I682b74b6730bfae682391ffed5382248a2d98895*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index 8855492..3ffb436 100644

//Synthetic comment -- @@ -32,8 +32,8 @@

private static final String LOG_TAG = "BuildVersionTest";
private static final Set<String> EXPECTED_RELEASES =
        new HashSet<String>(Arrays.asList("2.3", "2.3.1", "2.3.2"));
    private static final int EXPECTED_SDK = 9;

public void testReleaseVersion() {
// Applications may rely on the exact release version







