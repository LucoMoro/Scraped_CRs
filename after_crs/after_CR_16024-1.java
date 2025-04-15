/*Add Telephony Feature Tests

Bug 2816994

Check that the device's getPhoneType return value makes sense with
the features reported by PackageManager#getSystemAvailableFeatures
and PackageManager#hasFeature.

Change-Id:Ibc5137f1339d656029c1558f6783520e24bc3393*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 9ecbf20..c07c557 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
//Synthetic comment -- @@ -31,6 +33,9 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@TestTargetClass(TelephonyManager.class)
//Synthetic comment -- @@ -403,4 +408,71 @@
}
}
}

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getPhoneType",
        args = {}
    )
    public void testGetPhoneType() {
        int phoneType = mTelephonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                assertTelephonyFeatures(PackageManager.FEATURE_TELEPHONY,
                        PackageManager.FEATURE_TELEPHONY_GSM);
                break;

            case TelephonyManager.PHONE_TYPE_CDMA:
                assertTelephonyFeatures(PackageManager.FEATURE_TELEPHONY,
                        PackageManager.FEATURE_TELEPHONY_CDMA);
                break;

            case TelephonyManager.PHONE_TYPE_NONE:
                assertTelephonyFeatures();
                break;

            default:
                throw new IllegalArgumentException("Did you add a new phone type? " + phoneType);
        }
    }

    /**
     * Checks that the given features are enabled and also that all the other telephony features
     * are disabled.
     *
     * @param expectedFeaturesEnabled that {@link PackageManager} should report
     */
    private void assertTelephonyFeatures(String... expectedFeaturesEnabled) {
        // Create sets of enabled and disabled features.
        Set<String> enabledFeatures = new HashSet<String>();
        Collections.addAll(enabledFeatures, expectedFeaturesEnabled);

        Set<String> disabledFeatures = new HashSet<String>();
        Collections.addAll(disabledFeatures,
                PackageManager.FEATURE_TELEPHONY,
                PackageManager.FEATURE_TELEPHONY_GSM,
                PackageManager.FEATURE_TELEPHONY_CDMA);
        disabledFeatures.removeAll(enabledFeatures);

        // Get all available features to test not only hasFeature but getSystemAvailableFeatures.
        PackageManager packageManager = getContext().getPackageManager();
        Set<String> availableFeatures = new HashSet<String>();
        for (FeatureInfo featureInfo : packageManager.getSystemAvailableFeatures()) {
            availableFeatures.add(featureInfo.name);
        }

        for (String feature : enabledFeatures) {
            assertTrue("PackageManager#hasSystemFeature should return true for " + feature,
                    packageManager.hasSystemFeature(feature));
            assertTrue("PackageManager#getSystemAvailableFeatures should have " + feature,
                    availableFeatures.contains(feature));
        }

        for (String feature : disabledFeatures) {
            assertFalse("PackageMangaer#hasSystemFeature should NOT return true for " + feature,
                    packageManager.hasSystemFeature(feature));
            assertFalse("PackageManage#getSystemAvailableFeatures should NOT have " + feature,
                    availableFeatures.contains(feature));
        }
    }
}







