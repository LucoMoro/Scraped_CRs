/*Support for PHONE_LOOKUP in the Profile DB

Add support to do a PHONE_LOOKUP from the Profile DB, which will be used
if no contact is found.

Android Bug: 23153

Change-Id:I1a0601be2154486699d9696b33ffdfb4bbde44ab*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index 59b2170..3277ff6 100644

//Synthetic comment -- @@ -347,6 +347,7 @@
private static final int PROFILE_RAW_CONTACT_ENTITIES = 19010;
private static final int PROFILE_PHOTO = 19011;
private static final int PROFILE_DISPLAY_PHOTO = 19012;
    private static final int PROFILE_PHONE_LOOKUP = 19013;

private static final int DATA_USAGE_FEEDBACK_ID = 20001;

//Synthetic comment -- @@ -1213,6 +1214,8 @@
PROFILE_STATUS_UPDATES);
matcher.addURI(ContactsContract.AUTHORITY, "profile/raw_contact_entities",
PROFILE_RAW_CONTACT_ENTITIES);
        matcher.addURI(ContactsContract.AUTHORITY, "profile/phone_lookup/*", 
                PROFILE_PHONE_LOOKUP);

matcher.addURI(ContactsContract.AUTHORITY, "stream_items", STREAM_ITEMS);
matcher.addURI(ContactsContract.AUTHORITY, "stream_items/photo", STREAM_ITEMS_PHOTOS);
//Synthetic comment -- @@ -5737,7 +5740,8 @@
break;
}

            case PHONE_LOOKUP:
            case PROFILE_PHONE_LOOKUP: {
// Phone lookup cannot be combined with a selection
selection = null;
selectionArgs = null;
//Synthetic comment -- @@ -7486,6 +7490,7 @@
case PHONES_ID:
return Phone.CONTENT_ITEM_TYPE;
case PHONE_LOOKUP:
            case PROFILE_PHONE_LOOKUP:
return PhoneLookup.CONTENT_TYPE;
case EMAILS:
return Email.CONTENT_TYPE;
//Synthetic comment -- @@ -7563,6 +7568,7 @@
return sDataProjectionMap.getColumnNames();

case PHONE_LOOKUP:
            case PROFILE_PHONE_LOOKUP:
return sPhoneLookupProjectionMap.getColumnNames();

case AGGREGATION_EXCEPTIONS:







