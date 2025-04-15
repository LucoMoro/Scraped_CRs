/*Less incomplete CTS test for DateUtils.formatElapsedTime.

Still only tests en_US, but at least we now test all code paths.

Bug:http://code.google.com/p/android/issues/detail?id=41401Bug: 7736688
Change-Id:I4d34588175a3ffeb5ead8e0ad3a829cc5bd2393b*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index 420af25..e65561e 100644

//Synthetic comment -- @@ -119,19 +119,32 @@
mBaseTime - DAY_DURATION).toString());
}

@SuppressWarnings("deprecation")
public void testFormatMethods() {
if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
return;
}

        long elapsedTime = 2 * 60 * 60;
        String expected = "2:00:00";
        assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));
        StringBuilder sb = new StringBuilder();
        assertEquals(expected, DateUtils.formatElapsedTime(sb, elapsedTime));
        assertEquals(expected, sb.toString());

Date date = new Date(109, 0, 19, 3, 30, 15);
long fixedTime = date.getTime();








