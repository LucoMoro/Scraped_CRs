/*Allow 2.2.3 for Build.VERSION

Bug 4128048

Change-Id:I6a8bddc8cc0eae4a6a08573163f03ab147539c37*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildVersionTest.java b/tests/tests/os/src/android/os/cts/BuildVersionTest.java
//Synthetic comment -- index bbea5b7..3ca0b4f 100644

//Synthetic comment -- @@ -32,7 +32,7 @@

private static final String LOG_TAG = "BuildVersionTest";
private static final Set<String> EXPECTED_RELEASES =
        new HashSet<String>(Arrays.asList("2.2", "2.2.1", "2.2.2"));
private static final int EXPECTED_SDK = 8;

public void testReleaseVersion() {







