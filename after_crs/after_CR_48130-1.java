/*Less incomplete CTS test for DateUtils.formatElapsedTime.

Still only tests en_US, but at least we now test all code paths.

Bug:http://code.google.com/p/android/issues/detail?id=41401Bug: 7736688
Change-Id:I4d34588175a3ffeb5ead8e0ad3a829cc5bd2393b*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index 420af25..e65561e 100644

//Synthetic comment -- @@ -119,19 +119,32 @@
mBaseTime - DAY_DURATION).toString());
}

    public void test_formatElapsedTime() {
        if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
            return;
        }

        long MINUTES = 60;
        long HOURS = 60 * MINUTES;
        test_formatElapsedTime("02:01", 2 * MINUTES + 1);
        test_formatElapsedTime("3:02:01", 3 * HOURS + 2 * MINUTES + 1);
        // http://code.google.com/p/android/issues/detail?id=41401
        test_formatElapsedTime("123:02:01", 123 * HOURS + 2 * MINUTES + 1);
    }

    private void test_formatElapsedTime(String expected, long elapsedTime) {
        assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));
        StringBuilder sb = new StringBuilder();
        assertEquals(expected, DateUtils.formatElapsedTime(sb, elapsedTime));
        assertEquals(expected, sb.toString());
    }

@SuppressWarnings("deprecation")
public void testFormatMethods() {
if (!LocaleUtils.isCurrentLocale(mContext, Locale.US)) {
return;
}

Date date = new Date(109, 0, 19, 3, 30, 15);
long fixedTime = date.getTime();








