/*Add lookup in Call logs to lookupNumberhttps://android-review.googlesource.com/35390added caller name display
to GSM phones. But whenever Call log is opened the information was overwritten,
if no contact is present with that phoen number.
This patch add a lookup function that tries to find a match in the Call log
(after other posibilities failed) and returns that information to be put in cache.

* corrected coding style

Change-Id:I0868d5f8341382639b5ebf9d5e616f118fc423c3*/




//Synthetic comment -- diff --git a/src/com/android/contacts/calllog/ContactInfoHelper.java b/src/com/android/contacts/calllog/ContactInfoHelper.java
//Synthetic comment -- index 90d5e8b..a208c4d 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -72,6 +74,11 @@
if (phoneInfo == null || phoneInfo == ContactInfo.EMPTY) {
// Check whether the phone number has been saved as an "Internet call" number.
phoneInfo = queryContactInfoForSipAddress(number);

                if (phoneInfo == null || phoneInfo == ContactInfo.EMPTY) {
                    //Check contact in Calllog might we have some info there
                    phoneInfo = queryContactInfoForPhoneNumberFromCallLog(number, countryIso);
                }
}
info = phoneInfo;
}
//Synthetic comment -- @@ -188,6 +195,75 @@
}

/**
     * Determines the contact information stored in the call log for the given
     * phone number.
     * <p>
     * It returns the contact info if found.
     * <p>
     * If no contact corresponds to the given phone number, returns
     * {@link ContactInfo#EMPTY}.
     * <p>
     * If the lookup fails for some other reason, it returns null.
     */
    private ContactInfo queryContactInfoForPhoneNumberFromCallLog(String number, String countryIso) {
        final ContactInfo info;
        String contactNumber = number;
        if (!TextUtils.isEmpty(countryIso)) {
            // Normalize the number: this is needed because the PhoneLookup
            // query below does not
            // accept a country code as an input.
            String numberE164 = PhoneNumberUtils.formatNumberToE164(number, countryIso);
            if (!TextUtils.isEmpty(numberE164)) {
                // Only use it if the number could be formatted to E164.
                contactNumber = numberE164;
            }
        }

        /*
         * The "contactNumber" is a regular phone number, so use the CallLog
         * table. Assuming the most recent callLog entry for the phone number
         * has the most up to date info
         */
        Uri uri = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, Uri.encode(contactNumber));
        Cursor c = mContext.getContentResolver().query(
                uri,
                CallLogQuery._PROJECTION,
                null,
                null,
                Calls.DEFAULT_SORT_ORDER + " LIMIT 1");

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    // we found something
                    info = new ContactInfo();
                    info.lookupUri = UriUtils.parseUriOrNull(c
                            .getString(CallLogQuery.CACHED_LOOKUP_URI));
                    info.name = c.getString(CallLogQuery.CACHED_NAME);
                    info.type = c.getInt(CallLogQuery.CACHED_NUMBER_TYPE);
                    info.label = c.getString(CallLogQuery.CACHED_NUMBER_LABEL);
                    String matchedNumber = c.getString(CallLogQuery.CACHED_MATCHED_NUMBER);
                    info.number = matchedNumber == null ? c.getString(CallLogQuery.NUMBER)
                            : matchedNumber;
                    info.normalizedNumber = c.getString(CallLogQuery.CACHED_NORMALIZED_NUMBER);
                    info.photoId = c.getLong(CallLogQuery.CACHED_PHOTO_ID);
                    info.photoUri = null; // We do not cache the photo URI.
                    info.formattedNumber = c.getString(CallLogQuery.CACHED_FORMATTED_NUMBER);
                } else {
                    // nothing of interest
                    info = ContactInfo.EMPTY;
                }
            } finally {
                c.close();
            }
        } else {
            // somthing went south with the query
            info = null;
        }
        return info;
    }

    /**
* Format the given phone number
*
* @param number the number to be formatted.







