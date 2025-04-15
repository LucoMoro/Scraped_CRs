/*Use getAssets().getLocales() to compare with en_US.

The LocaleUtils.isSupportedLocale() is listed locale from ICU data resource.
The ICU data resource is provided by Android official release and all of data resources are including en_US.

The test case below is compared string with APK resource not ICU data and APK resource string is depended on phone locale.
The test case should query locale from phone locale.

Change-Id:Ib31e9a6cd4b9697d516b14244dabe7e14bd90499*/
//Synthetic comment -- diff --git a/tests/src/android/text/format/cts/LocaleUtils.java b/tests/src/android/text/format/cts/LocaleUtils.java
//Synthetic comment -- index 789ad74..d6001c4 100644

//Synthetic comment -- @@ -16,15 +16,17 @@

package android.text.format.cts;

import java.util.Locale;

public class LocaleUtils {

/** Return whether or not the specified locale is available on the system. */
    public static boolean isSupportedLocale(Locale locale) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale availableLocale : locales) {
            if (locale.equals(availableLocale)) {
return true;
}
}








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index 91bef2d..a5dd335 100644

//Synthetic comment -- @@ -198,7 +198,7 @@
})
@SuppressWarnings("deprecation")
public void testFormatMethods() {
        if (!LocaleUtils.isSupportedLocale(Locale.US)) {
// Locale is set to US in setUp method.
return;
}







