/*Update to add support for only selecting contacts with phones - requires
changes in frameworks/base/api/current.xml and
frameworks/base/core/java/android/provider/Contacts.java*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 7dbc76c..19bee40 100644

//Synthetic comment -- @@ -131,6 +131,8 @@
static final int MODE_STREQUENT = 35 | MODE_MASK_SHOW_PHOTOS;
/** Show all contacts and pick them when clicking */
static final int MODE_PICK_CONTACT = 40 | MODE_MASK_PICKER;
    /** Show all contacts with phone numbers and pick them when clicking */
    static final int MODE_PICK_WITH_PHONES = 41 | MODE_MASK_PICKER;
/** Show all contacts as well as the option to create a new one */
static final int MODE_PICK_OR_CREATE_CONTACT = 42 | MODE_MASK_PICKER | MODE_MASK_CREATE_NEW;
/** Show all contacts and pick them when clicking, and allow creating a new contact */
//Synthetic comment -- @@ -367,7 +369,11 @@
// the Intent.
final String type = intent.resolveType(this);
if (People.CONTENT_TYPE.equals(type)) {
                if (intent.getBooleanExtra(People.EXTRA_WITH_PHONES_ONLY, false)) {
                    mMode = MODE_PICK_WITH_PHONES;
                } else {
                    mMode = MODE_PICK_CONTACT;
                }
} else if (Phones.CONTENT_TYPE.equals(type)) {
mMode = MODE_PICK_PHONE;
} else if (ContactMethods.CONTENT_POSTAL_TYPE.equals(type)) {
//Synthetic comment -- @@ -519,9 +525,10 @@
break;

case MODE_WITH_PHONES:
            case MODE_PICK_WITH_PHONES:
empty.setText(getText(R.string.noContactsWithPhoneNumbers));
break;
                
default:
empty.setText(getText(R.string.noContacts));
break;
//Synthetic comment -- @@ -1010,6 +1017,7 @@
startActivity(intent);
finish();
} else if (mMode == MODE_PICK_CONTACT 
                    || mMode == MODE_PICK_WITH_PHONES
|| mMode == MODE_PICK_OR_CREATE_CONTACT) {
Uri uri = ContentUris.withAppendedId(People.CONTENT_URI, id);
if (mCreateShortcut) {
//Synthetic comment -- @@ -1067,6 +1075,7 @@
case MODE_ALL_CONTACTS:
case MODE_WITH_PHONES:
case MODE_PICK_CONTACT:
            case MODE_PICK_WITH_PHONES:
case MODE_PICK_OR_CREATE_CONTACT:
case MODE_QUERY:
case MODE_STARRED:
//Synthetic comment -- @@ -1126,6 +1135,7 @@
break;

case MODE_WITH_PHONES:
            case MODE_PICK_WITH_PHONES:
mQueryHandler.startQuery(QUERY_TOKEN, null, People.CONTENT_URI, CONTACTS_PROJECTION,
People.PRIMARY_PHONE_ID + " IS NOT NULL", null,
getSortOrder(CONTACTS_PROJECTION));
//Synthetic comment -- @@ -1222,7 +1232,8 @@
getSortOrder(CONTACTS_PROJECTION));
}

            case MODE_WITH_PHONES: 
            case MODE_PICK_WITH_PHONES: {
return resolver.query(getPeopleFilterUri(filter), CONTACTS_PROJECTION,
People.PRIMARY_PHONE_ID + " IS NOT NULL", null,
getSortOrder(CONTACTS_PROJECTION));







