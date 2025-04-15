/*fixing non-telephony device test failures

Some of the telephony tests are not-applicable to non-telephony devices.

Change-Id:I527fbd93cfc51c982df8cef94b924783f2272288*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 6c13007..9aa9b0c 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
//Synthetic comment -- @@ -444,14 +445,26 @@
private static final String ISO_COUNTRY_CODE_PATTERN = "[a-z]{2}";

public void testGetNetworkCountryIso() {
        PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getNetworkCountryIso();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            assertTrue("Country code '" + countryCode + "' did not match "
                    + ISO_COUNTRY_CODE_PATTERN,
                    Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
        } else {
            assertEquals("", countryCode);
        }
}

public void testGetSimCountryIso() {
        PackageManager packageManager = getContext().getPackageManager();
String countryCode = mTelephonyManager.getSimCountryIso();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            assertTrue("Country code '" + countryCode + "' did not match "
                    + ISO_COUNTRY_CODE_PATTERN,
                    Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
        } else {
            assertEquals("", countryCode);
        }
}
}







