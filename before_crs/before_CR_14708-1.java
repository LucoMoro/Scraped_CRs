/*Defect 7261: Edit number before call in ContactsListActivty

Change-Id:Ibc70e2a5005a1e5e011f3a1c735950aab9a6994f*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..86b96f5 100644

//Synthetic comment -- @@ -1096,6 +1096,9 @@
// Calling contact
menu.add(0, MENU_ITEM_CALL, 0,
getString(R.string.menu_call));
// Send SMS item
menu.add(0, MENU_ITEM_SEND_SMS, 0, getString(R.string.menu_sendSMS));
}
//Synthetic comment -- @@ -1140,6 +1143,32 @@
callContact(cursor);
return true;
}

case MENU_ITEM_SEND_SMS: {
smsContact(cursor);







