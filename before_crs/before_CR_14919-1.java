/*Defect 7261:Edit number before call

Merge commit 'I225fc0080ba7a3ca566b701bfe4315c5271b96a6' into eclair-plus-aosp

* commit 'I225fc0080ba7a3ca566b701bfe4315c5271b96a6':
  android-2.1_r1 snapshot
  Fixhttp://b/issue?id=2144841Change-Id:I40fe8aa0a26edc9e8a8a693bf2be4ab503ca0dca*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..fa2aa84 100644

//Synthetic comment -- @@ -1096,7 +1096,10 @@
// Calling contact
menu.add(0, MENU_ITEM_CALL, 0,
getString(R.string.menu_call));
            // Send SMS item
menu.add(0, MENU_ITEM_SEND_SMS, 0, getString(R.string.menu_sendSMS));
}

//Synthetic comment -- @@ -1141,6 +1144,31 @@
return true;
}

case MENU_ITEM_SEND_SMS: {
smsContact(cursor);
return true;







