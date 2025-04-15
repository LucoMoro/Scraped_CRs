/*am 3289d8fe: reconcile main tree with open-source eclair

Merge commit 'Iedd75eec9075d21bbb99f6011d7cb6249ab80fee'
into eclair-plus-aosp

* commit 'Iedd75eec9075d21bbb99f6011d7cb6249ab80fee':
  android-2.1_r1 snapshot
  Fixhttp://b/issue?id=2144841Change-Id:I0b3f3b5628662da75eb7700ce557f202f82e0df6*/




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







