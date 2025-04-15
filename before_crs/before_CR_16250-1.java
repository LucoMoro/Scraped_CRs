/*MMS: Test repo upload.

Change-Id:I4de3d1e8417c6d3d2e4211fdc0b5d5218043d871*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 7cd685b..5113c47 100644

//Synthetic comment -- @@ -828,7 +828,7 @@
}

private boolean haveEmailContact(String emailAddress) {
        Cursor cursor = SqliteWrapper.query(this, getContentResolver(),
Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(emailAddress)),
new String[] { Contacts.DISPLAY_NAME }, null, null, null);








