/*Revert "Allow 2.2.3 for Build.VERSION"

This reverts commit 520d02bd49ab943071e7cec70ef9173185f6f845.*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index 3ca0b4f..bbea5b7 100644

//Synthetic comment -- @@ -32,7 +32,7 @@

private static final String LOG_TAG = "BuildVersionTest";
private static final Set<String> EXPECTED_RELEASES =
        new HashSet<String>(Arrays.asList("2.2", "2.2.1", "2.2.2", "2.2.3"));
private static final int EXPECTED_SDK = 8;

public void testReleaseVersion() {







