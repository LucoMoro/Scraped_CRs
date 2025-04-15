/*Replace Maps with UserDictionarySettings to trigger database writes

Change-Id:I318b7418db5f77c9fa45f811b397c63a5e09a31dSigned-off-by: sammi_ms <Sammi_MS@asus.com>*/




//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/SqliteJournalLeakTest.java b/tests/tests/security/src/android/security/cts/SqliteJournalLeakTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 1f8b81b6..f89cb4c

//Synthetic comment -- @@ -81,10 +81,9 @@
webIntent.setData(Uri.parse("http:///localhost"));
webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
getContext().startActivity(webIntent);
        Intent dictIntent = new Intent("android.settings.USER_DICTIONARY_SETTINGS");
        dictIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(dictIntent);
}

private void checkDatabases(String suffix) {







