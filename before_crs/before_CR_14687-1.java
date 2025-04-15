/*Fix updating and deleting FDN entries with an empty alpha identifier.

- Fix AdnRecord.buildAdnString() to generate the correct record when alpha
identifier is empty. This allows the user to update an FDN entry to remove
the alpha identifier. Previously the entire entry would be deleted because
an empty record was generated here when the alpha identifier was empty,
rather than a record containing the phone number with an empty alpha tag.

- Fix IccProvider.delete() to construct an AdnRecord with "" for the alpha
identifier instead of null. This prevents a NullPointerException in
AdnRecord.isEqual() when deleting an FDN record with no alpha identifier.
See the comments onhttps://review.source.android.com//#change,13492The original version of that patch would generate a query containing
"tag=''" when the alpha identifier is empty, avoiding the NPE because
the tag is set to "". The intention of that patch was to create a query
without "tag=''" if the alpha identifier is empty, and if you do that,
you will get a NPE in AdnRecord.isEqual() without this change.

- Fix bug in IccProvider.delete() where efType was compared against local
FDN constant rather than IccConstants.EF_FDN. This would always return
false. Comparing with IccConstants.EF_FDN gives the intended behavior.

Change-Id:I0ea75d7e107c7318c9a48ae6e0a15845a718f4c0*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecord.java b/telephony/java/com/android/internal/telephony/AdnRecord.java
//Synthetic comment -- index 0896ba6..d41ab4a 100644

//Synthetic comment -- @@ -192,10 +192,8 @@
byte[] adnString = null;
int footerOffset = recordSize - FOOTER_SIZE_BYTES;

        if (number == null || number.equals("") ||
                alphaTag == null || alphaTag.equals("")) {

            Log.w(LOG_TAG, "[buildAdnString] Empty alpha tag or number");
adnString = new byte[recordSize];
for (int i = 0; i < recordSize; i++) {
adnString[i] = (byte) 0xFF;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccProvider.java b/telephony/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index 8b54ca8..0f568da 100644

//Synthetic comment -- @@ -239,7 +239,9 @@
}

// parse where clause
        String tag = null;
String number = null;
String[] emails = null;
String pin2 = null;
//Synthetic comment -- @@ -277,7 +279,7 @@
return 0;
}

        if (efType == FDN && TextUtils.isEmpty(pin2)) {
return 0;
}








