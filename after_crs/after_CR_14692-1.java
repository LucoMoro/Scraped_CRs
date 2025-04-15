/*Defect 6943, 6630: Delete Contacts Should be based on Contact ID.

Change-Id:I0d927aa84f2d6a1587a9657ed5c1f68f0cadd190*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..fe474ee 100644

//Synthetic comment -- @@ -1147,7 +1147,9 @@
}

case MENU_ITEM_DELETE: {
		final long contactId = cursor.getLong(SUMMARY_ID_COLUMN_INDEX);
                mSelectedContactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, 
	                String.valueOf(contactId));
doContactDelete();
return true;
}







