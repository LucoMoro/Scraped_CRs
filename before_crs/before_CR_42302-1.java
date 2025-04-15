/*Deprecate those parts of DateUtils the documentation says not to use.

Applications using these fields and methods are just asking for i18n bugs.

Also @hide two int[]s that were never meant to be public.

Change-Id:I29b3a1c0c663fe344d2567df6ed3bb537270b3b7*/
//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 211453d..b276be4 100644

//Synthetic comment -- @@ -161,12 +161,17 @@
public static final int FORMAT_NO_YEAR = 0x00008;
public static final int FORMAT_SHOW_DATE = 0x00010;
public static final int FORMAT_NO_MONTH_DAY = 0x00020;
public static final int FORMAT_12HOUR = 0x00040;
public static final int FORMAT_24HOUR = 0x00080;
public static final int FORMAT_CAP_AMPM = 0x00100;
public static final int FORMAT_NO_NOON = 0x00200;
public static final int FORMAT_CAP_NOON = 0x00400;
public static final int FORMAT_NO_MIDNIGHT = 0x00800;
public static final int FORMAT_CAP_MIDNIGHT = 0x01000;
/**
* @deprecated Use
//Synthetic comment -- @@ -181,19 +186,25 @@
public static final int FORMAT_NUMERIC_DATE = 0x20000;
public static final int FORMAT_ABBREV_RELATIVE = 0x40000;
public static final int FORMAT_ABBREV_ALL = 0x80000;
public static final int FORMAT_CAP_NOON_MIDNIGHT = (FORMAT_CAP_NOON | FORMAT_CAP_MIDNIGHT);
public static final int FORMAT_NO_NOON_MIDNIGHT = (FORMAT_NO_NOON | FORMAT_NO_MIDNIGHT);

// Date and time format strings that are constant and don't need to be
// translated.
/**
* This is not actually the preferred 24-hour date format in all locales.
*/
public static final String HOUR_MINUTE_24 = "%H:%M";
public static final String MONTH_FORMAT = "%B";
/**
* This is not actually a useful month name in all locales.
*/
public static final String ABBREV_MONTH_FORMAT = "%b";
public static final String NUMERIC_MONTH_FORMAT = "%m";
public static final String MONTH_DAY_FORMAT = "%-d";
//Synthetic comment -- @@ -207,6 +218,7 @@
// The index is constructed from a bit-wise OR of the boolean values:
// {showTime, showYear, showWeekDay}.  For example, if showYear and
// showWeekDay are both true, then the index would be 3.
public static final int sameYearTable[] = {
com.android.internal.R.string.same_year_md1_md2,
com.android.internal.R.string.same_year_wday1_md1_wday2_md2,
//Synthetic comment -- @@ -233,6 +245,7 @@
// The index is constructed from a bit-wise OR of the boolean values:
// {showTime, showYear, showWeekDay}.  For example, if showYear and
// showWeekDay are both true, then the index would be 3.
public static final int sameMonthTable[] = {
com.android.internal.R.string.same_month_md1_md2,
com.android.internal.R.string.same_month_wday1_md1_wday2_md2,
//Synthetic comment -- @@ -259,7 +272,9 @@
*
* @more <p>
*       e.g. "Sunday" or "January"
*/
public static final int LENGTH_LONG = 10;

/**
//Synthetic comment -- @@ -268,7 +283,9 @@
*
* @more <p>
*       e.g. "Sun" or "Jan"
*/
public static final int LENGTH_MEDIUM = 20;

/**
//Synthetic comment -- @@ -278,14 +295,18 @@
* <p>e.g. "Su" or "Jan"
* <p>In most languages, the results returned for LENGTH_SHORT will be the same as
* the results returned for {@link #LENGTH_MEDIUM}.
*/
public static final int LENGTH_SHORT = 30;

/**
* Request an even shorter abbreviated version of the name.
* Do not use this.  Currently this will always return the same result
* as {@link #LENGTH_SHORT}.
*/
public static final int LENGTH_SHORTER = 40;

/**
//Synthetic comment -- @@ -295,7 +316,9 @@
* <p>e.g. "S", "T", "T" or "J"
* <p>In some languages, the results returned for LENGTH_SHORTEST will be the same as
* the results returned for {@link #LENGTH_SHORT}.
*/
public static final int LENGTH_SHORTEST = 50;

/**
//Synthetic comment -- @@ -309,7 +332,9 @@
*               Undefined lengths will return {@link #LENGTH_MEDIUM}
*               but may return something different in the future.
* @throws IndexOutOfBoundsException if the dayOfWeek is out of bounds.
*/
public static String getDayOfWeekString(int dayOfWeek, int abbrev) {
int[] list;
switch (abbrev) {
//Synthetic comment -- @@ -330,7 +355,9 @@
* @param ampm Either {@link Calendar#AM Calendar.AM} or {@link Calendar#PM Calendar.PM}.
* @throws IndexOutOfBoundsException if the ampm is out of bounds.
* @return Localized version of "AM" or "PM".
*/
public static String getAMPMString(int ampm) {
Resources r = Resources.getSystem();
return r.getString(sAmPm[ampm - Calendar.AM]);
//Synthetic comment -- @@ -345,7 +372,9 @@
*               Undefined lengths will return {@link #LENGTH_MEDIUM}
*               but may return something different in the future.
* @return Localized month of the year.
*/
public static String getMonthString(int month, int abbrev) {
// Note that here we use sMonthsMedium for MEDIUM, SHORT and SHORTER.
// This is a shortcut to not spam the translators with too many variations
//Synthetic comment -- @@ -378,7 +407,9 @@
*               but may return something different in the future.
* @return Localized month of the year.
* @hide Pending API council approval
*/
public static String getStandaloneMonthString(int month, int abbrev) {
// Note that here we use sMonthsMedium for MEDIUM, SHORT and SHORTER.
// This is a shortcut to not spam the translators with too many variations







