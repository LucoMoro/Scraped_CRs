/*Defect:7261 Edit Number before call option in ContactListActivity

Change-Id:I21c0676def78ab56469bf4c904e9cf2b3656bc88*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 0e7e9a6..b2bef76 100644

//Synthetic comment -- @@ -1155,17 +1155,15 @@
Cursor c = getContentResolver().query(dataUri,
new String[] {Phone._ID, Phone.NUMBER, Phone.IS_SUPER_PRIMARY},
Data.MIMETYPE + "=?", new String[] {Phone.CONTENT_ITEM_TYPE}, null);
                if (c != null && c.moveToFirst()) {
number = c.getString(1);
                }
                c.close();
if(null != number) {
                    numberUri = Uri.fromParts("tel", number, null);
                    Intent intent = new Intent(Intent.ACTION_DIAL, numberUri);
                    startActivity(intent);
                }
return true;
}








