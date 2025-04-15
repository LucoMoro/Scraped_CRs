/*ChangeIedd75eec: Defect 6943: Delete Contact based on Contact ID

Change-Id:I55d8a0bd472018d356b076d3ab54caa21b32c323*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..38fc982 100644

//Synthetic comment -- @@ -1147,7 +1147,9 @@
}

case MENU_ITEM_DELETE: {
                mSelectedContactUri = getContactUri(info.position);
doContactDelete();
return true;
}







