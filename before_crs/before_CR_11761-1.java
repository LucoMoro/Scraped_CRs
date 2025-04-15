/*Update to add support for only selecting contacts with phones - requires
changes in frameworks/base/api/current.xml and
frameworks/base/core/java/android/provider/Contacts.java*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 02c70d2..5cc28c2 100644

//Synthetic comment -- @@ -140,6 +140,8 @@
static final int MODE_STREQUENT = 35 | MODE_MASK_SHOW_PHOTOS;
/** Show all contacts and pick them when clicking */
static final int MODE_PICK_CONTACT = 40 | MODE_MASK_PICKER;
/** Show all contacts as well as the option to create a new one */
static final int MODE_PICK_OR_CREATE_CONTACT = 42 | MODE_MASK_PICKER | MODE_MASK_CREATE_NEW;
/** Show all contacts and pick them when clicking, and allow creating a new contact */
//Synthetic comment -- @@ -403,7 +405,11 @@
// the Intent.
final String type = intent.resolveType(this);
if (People.CONTENT_TYPE.equals(type)) {
                mMode = MODE_PICK_CONTACT;
} else if (Phones.CONTENT_TYPE.equals(type)) {
mMode = MODE_PICK_PHONE;
} else if (ContactMethods.CONTENT_POSTAL_TYPE.equals(type)) {
//Synthetic comment -- @@ -566,6 +572,7 @@
break;

case MODE_WITH_PHONES:
empty.setText(getText(R.string.noContactsWithPhoneNumbers));
break;

//Synthetic comment -- @@ -1096,6 +1103,7 @@
startActivity(intent);
finish();
} else if (mMode == MODE_PICK_CONTACT 
|| mMode == MODE_PICK_OR_CREATE_CONTACT) {
Uri uri = ContentUris.withAppendedId(People.CONTENT_URI, id);
if (mShortcutAction != null) {
//Synthetic comment -- @@ -1285,6 +1293,7 @@
case MODE_ALL_CONTACTS:
case MODE_WITH_PHONES:
case MODE_PICK_CONTACT:
case MODE_PICK_OR_CREATE_CONTACT:
case MODE_QUERY:
case MODE_STARRED:
//Synthetic comment -- @@ -1344,6 +1353,7 @@
break;

case MODE_WITH_PHONES:
mQueryHandler.startQuery(QUERY_TOKEN, null, People.CONTENT_URI, CONTACTS_PROJECTION,
People.PRIMARY_PHONE_ID + " IS NOT NULL", null,
getSortOrder(CONTACTS_PROJECTION));
//Synthetic comment -- @@ -1440,7 +1450,8 @@
getSortOrder(CONTACTS_PROJECTION));
}

            case MODE_WITH_PHONES: {
return resolver.query(getPeopleFilterUri(filter), CONTACTS_PROJECTION,
People.PRIMARY_PHONE_ID + " IS NOT NULL", null,
getSortOrder(CONTACTS_PROJECTION));







