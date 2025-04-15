/*Remove Maps database writes in SqliteJournalLeakTest

Remove Maps database writes since Maps database is not in the permission check list and this testcase fails without Maps installed.

Change-Id:I318b7418db5f77c9fa45f811b397c63a5e09a31dSigned-off-by: sammi_ms <Sammi_MS@asus.com>*/




//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/SqliteJournalLeakTest.java b/tests/tests/security/src/android/security/cts/SqliteJournalLeakTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 1f8b81b6..74aeb85

//Synthetic comment -- @@ -81,10 +81,6 @@
webIntent.setData(Uri.parse("http:///localhost"));
webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
getContext().startActivity(webIntent);
}

private void checkDatabases(String suffix) {







