/*Some PackageManager Feature Tests

Bug 2817090, 2817037, 2817010, 2817022, 2817050

Some sanity tests to check that the PackageManager is reporting
the correct features. It won't catch everything, and some of
the tests like for checking for the existence of WiFi or
Camera don't make a whole lot of sense... Finally, test that
features not defined by the SDK aren't using the android
namespace.

Moved feature tests for sensors and telephony into this class
to centralize all these feature tests. Thanks to George for
nearly all the code here.

Change-Id:I67e1e3ca8ccd8bfe46422f97abf571aecd28ad06*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
new file mode 100644
//Synthetic comment -- index 0000000..58575ee

//Synthetic comment -- @@ -0,0 +1,328 @@








//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java
//Synthetic comment -- index 73c32af..136d077 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//Synthetic comment -- @@ -32,13 +31,7 @@
import android.test.AndroidTestCase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
@TestTargetClass(SensorManager.class)
//Synthetic comment -- @@ -311,75 +304,4 @@
assertTrue(Math.abs(orientation[2]) <= TWO_PI);
}
}

    /**
     * Check that the sensor features reported by the PackageManager correspond to the sensors
     * returned by {@link SensorManager#getSensorList(int)}.
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getSensorList",
        args = {int.class}
    )
    public void testGetSensorList() throws Exception {
        Set<String> featuresLeft = getAllSensorFeatures();

        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_ACCELEROMETER,
                Sensor.TYPE_ACCELEROMETER);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_COMPASS,
                Sensor.TYPE_MAGNETIC_FIELD);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_LIGHT,
                Sensor.TYPE_LIGHT);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_PROXIMITY,
                Sensor.TYPE_PROXIMITY);

        assertTrue("Assertions need to be added to this test for " + featuresLeft,
                featuresLeft.isEmpty());
    }

    /** Get a list of all the sensor features to make sure the test checks them all. */
    private static Set<String> getAllSensorFeatures()
            throws IllegalArgumentException, IllegalAccessException {
        Set<String> sensorFeatures = new HashSet<String>();
        Field[] fields = PackageManager.class.getFields();
        for (Field field : fields) {
            if (field.getName().startsWith("FEATURE_SENSOR_")) {
                String feature = (String) field.get(null);
                sensorFeatures.add(feature);
            }
        }
        return sensorFeatures;
    }

    /**
     * Check that if the PackageManager declares a sensor feature that the device has at least
     * one sensor that matches that feature. Also check that if a PackageManager does not declare
     * a sensor that the device also does not have such a sensor.
     *
     * @param featuresLeft to check in order to make sure the test covers all sensor features
     * @param expectedFeature that the PackageManager may report
     * @param expectedSensorType that that {@link SensorManager#getSensorList(int)} may have
     */
    private void assertFeatureForSensor(Set<String> featuresLeft, String expectedFeature,
            int expectedSensorType) {
        assertTrue("Features left " + featuresLeft + " to check did not include "
                + expectedFeature, featuresLeft.remove(expectedFeature));

        PackageManager packageManager = mContext.getPackageManager();
        boolean hasSensorFeature = packageManager.hasSystemFeature(expectedFeature);

        List<Sensor> sensors = mSensorManager.getSensorList(expectedSensorType);
        List<String> sensorNames = new ArrayList<String>(sensors.size());
        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
        }
        boolean hasSensorType = !sensors.isEmpty();

        String message = "PackageManager#hasSystemFeature(" + expectedFeature + ") returns "
                + hasSensorFeature
                + " but SensorManager#getSensorList(" + expectedSensorType + ") shows sensors "
                + sensorNames;

        assertEquals(message, hasSensorFeature, hasSensorType);
    }
}








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index c07c557..9ecbf20 100644

//Synthetic comment -- @@ -22,8 +22,6 @@
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
//Synthetic comment -- @@ -33,9 +31,6 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@TestTargetClass(TelephonyManager.class)
//Synthetic comment -- @@ -408,71 +403,4 @@
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







