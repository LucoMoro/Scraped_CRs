/*Bypass testFormatMethods if device is not using en_US resource.

Change-Id:I915e80cbca27eef563a7242b71a164be9ed90401*/




//Synthetic comment -- diff --git a/tests/src/android/text/format/cts/LocaleUtils.java b/tests/src/android/text/format/cts/LocaleUtils.java
//Synthetic comment -- index d6001c4..131d745 100644

//Synthetic comment -- @@ -22,14 +22,10 @@

public class LocaleUtils {

    /** Return whether or not the given locale is the device's current locale. */
    public static boolean isCurrentLocale(Context context, Locale locale) {
        // TODO: Change the locale on the device using public API if it becomes available.
        Locale currentLocale = context.getResources().getConfiguration().locale;
        return locale.equals(currentLocale);
}
}








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index a5dd335..6d034b9 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
private static final long HOUR_DURATION = 2 * 60 * 60 * 1000;
private static final long DAY_DURATION = 5 * 24 * 60 * 60 * 1000;
private long mBaseTime;
private Context mContext;

@Override
//Synthetic comment -- @@ -45,18 +44,6 @@
super.setUp();
mContext = getContext();
mBaseTime = System.currentTimeMillis();
}

@TestTargetNew(
//Synthetic comment -- @@ -65,6 +52,10 @@
args = {int.class, int.class}
)
public void testGetDayOfWeekString() {
        if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
            return;
        }

assertEquals("Sunday",
DateUtils.getDayOfWeekString(Calendar.SUNDAY, DateUtils.LENGTH_LONG));
assertEquals("Sun",
//Synthetic comment -- @@ -86,6 +77,9 @@
args = {int.class, int.class}
)
public void testGetMonthString() {
        if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
            return;
        }
assertEquals("January", DateUtils.getMonthString(Calendar.JANUARY, DateUtils.LENGTH_LONG));
assertEquals("Jan",
DateUtils.getMonthString(Calendar.JANUARY, DateUtils.LENGTH_MEDIUM));
//Synthetic comment -- @@ -104,6 +98,9 @@
args = {int.class}
)
public void testGetAMPMString() {
        if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
            return;
        }
assertEquals("am", DateUtils.getAMPMString(Calendar.AM));
assertEquals("pm", DateUtils.getAMPMString(Calendar.PM));
}
//Synthetic comment -- @@ -142,6 +139,9 @@
})

public void testGetSpanString() {
        if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
            return;
        }
assertEquals("0 minutes ago",
DateUtils.getRelativeTimeSpanString(mBaseTime - MIN_DURATION).toString());
assertEquals("in 0 minutes",
//Synthetic comment -- @@ -198,8 +198,7 @@
})
@SuppressWarnings("deprecation")
public void testFormatMethods() {
        if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
return;
}








