/*Add lookup in Call logs to lookupNumberhttps://android-review.googlesource.com/35390added caller name display
to GSM phones. But whenever Call log is opened the information was overwritten,
if no contact is present with that phoen number.
This patch add a lookup function that tries to find a match in the Call log
(after other posibilities failed) and returns that information to be put in cache.

* Patch Set 2 : corrected coding style
* Patch Set 3 : - Query only incoming calls (the info we're looking for
                  if it exists is there and only there)
                - Correct phone number formatting

Change-Id:I0868d5f8341382639b5ebf9d5e616f118fc423c3*/
//Synthetic comment -- diff --git a/src/com/android/contacts/calllog/ContactInfoHelper.java b/src/com/android/contacts/calllog/ContactInfoHelper.java
//Synthetic comment -- index 90d5e8b..3737a93 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -72,6 +74,11 @@
if (phoneInfo == null || phoneInfo == ContactInfo.EMPTY) {
// Check whether the phone number has been saved as an "Internet call" number.
phoneInfo = queryContactInfoForSipAddress(number);
}
info = phoneInfo;
}
//Synthetic comment -- @@ -188,6 +195,77 @@
}

/**
* Format the given phone number
*
* @param number the number to be formatted.







