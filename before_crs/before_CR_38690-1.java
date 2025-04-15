/*Look in the profile db when querying contact for phone number

If no contact is found, do a phone lookup in the profile db when
querying contact for a phone number.

Android Bug: 23153

Change-Id:I062570efb87fb2df7665d024b41912611fb4f56f*/
//Synthetic comment -- diff --git a/src/com/android/contacts/calllog/ContactInfoHelper.java b/src/com/android/contacts/calllog/ContactInfoHelper.java
//Synthetic comment -- index 90d5e8b..e638bcb 100644

//Synthetic comment -- @@ -181,6 +181,11 @@
// The "contactNumber" is a regular phone number, so use the PhoneLookup table.
Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactNumber));
ContactInfo info = lookupContactFromUri(uri);
if (info != null && info != ContactInfo.EMPTY) {
info.formattedNumber = formatPhoneNumber(number, null, countryIso);
}







