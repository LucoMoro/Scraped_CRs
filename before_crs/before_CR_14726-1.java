/*Defect 6943: Delete Contact based on Contact ID

Change-Id:Iedd75eec9075d21bbb99f6011d7cb6249ab80fee*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..38fc982 100644

//Synthetic comment -- @@ -1147,7 +1147,9 @@
}

case MENU_ITEM_DELETE: {
                mSelectedContactUri = getContactUri(info.position);
doContactDelete();
return true;
}







