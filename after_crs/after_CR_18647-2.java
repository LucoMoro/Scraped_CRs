/*Skip Test if US Locale Not Available

Bug 2951291

The test sets the default locale to US, but that may not be an
available locale. Skip the test if the US locale is not available.

Change-Id:I8502b430f696a11b14cd5d30d53b4aef5c0e5662*/




//Synthetic comment -- diff --git a/tests/src/android/text/format/cts/LocaleUtils.java b/tests/src/android/text/format/cts/LocaleUtils.java
new file mode 100644
//Synthetic comment -- index 0000000..789ad74

//Synthetic comment -- @@ -0,0 +1,33 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        return false;
    }
}








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index 22ef413..91bef2d 100644

//Synthetic comment -- @@ -198,6 +198,11 @@
})
@SuppressWarnings("deprecation")
public void testFormatMethods() {
        if (!LocaleUtils.isSupportedLocale(Locale.US)) {
            // Locale is set to US in setUp method.
            return;
        }

long elapsedTime = 2 * 60 * 60;
String expected = "2:00:00";
assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));







