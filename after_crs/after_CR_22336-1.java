/*Handling non telephony device case for testExtractMethods()

Non telephony devices don't need PhoneNumberUtils test. This test case
does not consider Non telephony models. The method testExtractMethods()
has been modified to handle the case of non telephony devices.

Change-Id:I3f9a7b1160a93362e0c2cb9978457dbee53c1e50*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index 23891e0..b78fc31 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.test.AndroidTestCase;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.content.pm.PackageManager;

import java.util.Locale;

//Synthetic comment -- @@ -55,6 +56,11 @@
)
})
public void testExtractMethods() {
        // Skip the test for non-telephony devices
        PackageManager packageManager = getContext().getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            return;
        }

// Test extractNetworkPortion
assertNull(PhoneNumberUtils.extractNetworkPortion(null));







