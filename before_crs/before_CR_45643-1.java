/*BannedFilesTest: Detect devices vulnerable to the cmdclient privilege escalation bug.

Change-Id:I76ad31829b48c84b8d02d0f76b78477eace5eaa1*/
//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/BannedFilesTest.java b/tests/tests/security/src/android/security/cts/BannedFilesTest.java
//Synthetic comment -- index b4deed5..ada9aaf 100644

//Synthetic comment -- @@ -22,6 +22,18 @@

public class BannedFilesTest extends TestCase {

public void testNoSyncAgent() {
assertNotSetugid("/system/bin/sync_agent");
}







