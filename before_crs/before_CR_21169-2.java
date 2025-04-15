/*Bypass testFormatMethods if device is not using en_US resource.

Change-Id:I915e80cbca27eef563a7242b71a164be9ed90401*/
//Synthetic comment -- diff --git a/tests/src/android/text/format/cts/LocaleUtils.java b/tests/src/android/text/format/cts/LocaleUtils.java
//Synthetic comment -- index d6001c4..131d745 100644

//Synthetic comment -- @@ -22,14 +22,10 @@

public class LocaleUtils {

    /** Return whether or not the specified locale is available on the system. */
    public static boolean isSupportedLocale(Context context, Locale locale) {
        String[] locales = context.getAssets().getLocales();
        for (String availableLocale : locales) {
            if (locale.toString().equals(availableLocale)) {
                return true;
            }
        }
        return false;
}
}








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index a5dd335..6d034b9 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
private static final long HOUR_DURATION = 2 * 60 * 60 * 1000;
private static final long DAY_DURATION = 5 * 24 * 60 * 60 * 1000;
private long mBaseTime;
    private Locale mDefaultLocale;
private Context mContext;

@Override
//Synthetic comment -- @@ -45,18 +44,6 @@
super.setUp();
mContext = getContext();
mBaseTime = System.currentTimeMillis();
        mDefaultLocale = Locale.getDefault();
        if (!mDefaultLocale.equals(Locale.US)) {
            Locale.setDefault(Locale.US);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (!Locale.getDefault().equals(mDefaultLocale)) {
            Locale.setDefault(mDefaultLocale);
        }
        super.tearDown();
}

@TestTargetNew(
//Synthetic comment -- @@ -65,6 +52,10 @@
args = {int.class, int.class}
)
public void testGetDayOfWeekString() {
assertEquals("Sunday",
DateUtils.getDayOfWeekString(Calendar.SUNDAY, DateUtils.LENGTH_LONG));
assertEquals("Sun",
//Synthetic comment -- @@ -86,6 +77,9 @@
args = {int.class, int.class}
)
public void testGetMonthString() {
assertEquals("January", DateUtils.getMonthString(Calendar.JANUARY, DateUtils.LENGTH_LONG));
assertEquals("Jan",
DateUtils.getMonthString(Calendar.JANUARY, DateUtils.LENGTH_MEDIUM));
//Synthetic comment -- @@ -104,6 +98,9 @@
args = {int.class}
)
public void testGetAMPMString() {
assertEquals("am", DateUtils.getAMPMString(Calendar.AM));
assertEquals("pm", DateUtils.getAMPMString(Calendar.PM));
}
//Synthetic comment -- @@ -142,6 +139,9 @@
})

public void testGetSpanString() {
assertEquals("0 minutes ago",
DateUtils.getRelativeTimeSpanString(mBaseTime - MIN_DURATION).toString());
assertEquals("in 0 minutes",
//Synthetic comment -- @@ -198,8 +198,7 @@
})
@SuppressWarnings("deprecation")
public void testFormatMethods() {
        if (!LocaleUtils.isSupportedLocale(mContext, Locale.US)) {
            // Locale is set to US in setUp method.
return;
}








