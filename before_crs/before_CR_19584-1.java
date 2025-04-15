/*Return name of newly created contact when creating a shortcut

The EXTRA_SHORTCUT_NAME was not set properly when returning a result
for a newly created contact, resulting in a shortcut without
any name. The name will now be queried for and set after the contact
has been created.

Change-Id:I41a2b2d3b9df953914782c85350aeedfc5912455*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index ac6a3a8..86e5064 100644

//Synthetic comment -- @@ -899,9 +899,12 @@
}

private String getContactDisplayName(long contactId) {
String contactName = null;
        Cursor c = getContentResolver().query(
                ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId),
new String[] {Contacts.DISPLAY_NAME}, null, null, null);
try {
if (c != null && c.moveToFirst()) {
//Synthetic comment -- @@ -1535,7 +1538,7 @@
switch (requestCode) {
case SUBACTIVITY_NEW_CONTACT:
if (resultCode == RESULT_OK) {
                    returnPickerResult(null, data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME),
data.getData(), (mMode & MODE_MASK_PICKER) != 0
? Intent.FLAG_GRANT_READ_URI_PERMISSION : 0);
}







