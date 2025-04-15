/*Use getAssets().getLocales() to compare with en_US.

The LocaleUtils.isSupportedLocale() is listed locale from ICU data resource.
The ICU data resource is provided by Android official release and all of data resources are including en_US.

The test case below is compared string with APK resource not ICU data and APK resource string is depended on phone locale.
The test case should query locale from phone locale.

Change-Id:I94135b2f362db4f4bc9bf1d20a311b7077776202*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index 91bef2d..b075ea5 100644

//Synthetic comment -- @@ -203,6 +203,16 @@
return;
}

        String[] locales = getContext().getAssets().getLocales();
        int i = 0;
        for (i=0;i<locales.length;i++) {
            if (locales[i].equals("en_US"))
                break;
        }

        if (i==locales.length)
            return;

long elapsedTime = 2 * 60 * 60;
String expected = "2:00:00";
assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));







