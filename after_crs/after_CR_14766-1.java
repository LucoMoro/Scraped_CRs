/*Delete Contact based on Contact ID.

Change-Id:I06d83accc5d248642412136eb25f695f62e020b5*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..38fc982 100644

//Synthetic comment -- @@ -1147,7 +1147,9 @@
}

case MENU_ITEM_DELETE: {
                final long contactId = cursor.getLong(SUMMARY_ID_COLUMN_INDEX);
                mSelectedContactUri = Uri.withAppendedPath(Contacts.CONTENT_URI,
                        String.valueOf(contactId));
doContactDelete();
return true;
}







