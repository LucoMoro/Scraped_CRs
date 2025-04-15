/*Defect 7261 : Edit number before call option in ContactListActivity

Change-Id:I2e6203face3735605432094b42e93962770c447a*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..b2bef76 100644

//Synthetic comment -- @@ -1096,7 +1096,10 @@
// Calling contact
menu.add(0, MENU_ITEM_CALL, 0,
getString(R.string.menu_call));
            // Send SMS item
menu.add(0, MENU_ITEM_SEND_SMS, 0, getString(R.string.menu_sendSMS));
}

//Synthetic comment -- @@ -1141,6 +1144,29 @@
return true;
}

case MENU_ITEM_SEND_SMS: {
smsContact(cursor);
return true;







