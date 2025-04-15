/*Changed the way that date format selector is shown.
Changed the way that date format selector is shown excluding ambiguity when day and month have same value.
- i.e. 01/01/2012 (mm-DD-YYYY) and 01/01/2012 (DD-mm-YYYY)
Now it displays 31/12/2012 for DD-mm-YYYY and 12/31/2012 for mm-DD-YYYY.

Change-Id:I27434c9d5713491950d4f345dccf65d647d399cf*/




//Synthetic comment -- diff --git a/src/com/android/settings/DateTimeSettings.java b/src/com/android/settings/DateTimeSettings.java
//Synthetic comment -- index 30d4f0a..8c91c30 100644

//Synthetic comment -- @@ -118,6 +118,11 @@
if (currentFormat == null) {
currentFormat = "";
}

        // Prevents duplicated values on date format selector.
        mDummyDate.set(Calendar.DAY_OF_MONTH, 31);
        mDummyDate.set(Calendar.MONTH, Calendar.DECEMBER);

for (int i = 0; i < formattedDates.length; i++) {
String formatted =
DateFormat.getDateFormatForSetting(getActivity(), dateFormats[i])







