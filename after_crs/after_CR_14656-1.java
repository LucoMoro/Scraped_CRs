/*Defect Fix:6943,6630-Deletion of Contact should be based on Contact id not on lookup.

Change-Id:I6f21c3f5207efc34c9f1dddcd7227f88fd4d9633*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..c0e9323 100644

//Synthetic comment -- @@ -1147,7 +1147,9 @@
}

case MENU_ITEM_DELETE: {
                //mSelectedContactUri = getContactUri(info.position);
                final long contactId = cursor.getLong(SUMMARY_ID_COLUMN_INDEX);
                mSelectedContactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, String.valueOf(contactId));
doContactDelete();
return true;
}







