/*Defect 7261 : Edit number before call option in ContactListActivity

Change-Id:I2e6203face3735605432094b42e93962770c447a*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 24c7100..b2bef76 100644

//Synthetic comment -- @@ -1096,7 +1096,10 @@
// Calling contact
menu.add(0, MENU_ITEM_CALL, 0,
getString(R.string.menu_call));
            // Edit before call
            menu.add(0, MENU_ITEM_EDIT_BEFORE_CALL, 0,
                    getString(R.string.recentCalls_editNumberBeforeCall));
            //Send SMS item
menu.add(0, MENU_ITEM_SEND_SMS, 0, getString(R.string.menu_sendSMS));
}

//Synthetic comment -- @@ -1141,6 +1144,29 @@
return true;
}

            case MENU_ITEM_EDIT_BEFORE_CALL: {
                Uri numberUri = null;
                String number = null;

                final int contactId = cursor.getInt(SUMMARY_ID_COLUMN_INDEX);
                Uri baseUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
                Uri dataUri = Uri.withAppendedPath(baseUri, Contacts.Data.CONTENT_DIRECTORY);

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

case MENU_ITEM_SEND_SMS: {
smsContact(cursor);
return true;







