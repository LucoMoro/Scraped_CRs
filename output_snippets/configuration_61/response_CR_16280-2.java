//<Beginning of snippet n. 1>


import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.test.AndroidTestCase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("deprecation")
@TestTargetClass(SensorManager.class)
public class SensorManagerTest extends AndroidTestCase {

    private PackageManager mPackageManager;
    private SensorManager mSensorManager;

    // Additional setup for context and sensor manager
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
        mPackageManager = mContext.getPackageManager();
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
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
        // Testing for non-existent sensor feature
        assertFalse("Extra features to check should be empty", featuresLeft.isEmpty());
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
     * @param featuresLeft to check for coverage
     * @param expectedFeature that the PackageManager may report
     * @param expectedSensorType that that {@link SensorManager#getSensorList(int)} may have
     */
    private void assertFeatureForSensor(Set<String> featuresLeft, String expectedFeature,
            int expectedSensorType) {
        assertTrue("Features left " + featuresLeft + " to check did not include "
                + expectedFeature, featuresLeft.remove(expectedFeature));

        boolean hasSensorFeature = mPackageManager.hasSystemFeature(expectedFeature);

        List<Sensor> sensors = mSensorManager.getSensorList(expectedSensorType);
        boolean hasSensorType = !sensors.isEmpty();

        String message = "PackageManager#hasSystemFeature(" + expectedFeature + ") returns "
                + hasSensorFeature + " but SensorManager#getSensorList(" + expectedSensorType + ") shows sensors " 
                + sensors.toString();

        assertEquals(message, hasSensorFeature, hasSensorType);
    }

    // Sanity test for unsupported features
    public void testUnsupportedFeatures() {
        String unsupportedFeature = "FEATURE_SENSOR_UNSUPPORTED";
        boolean hasFeature = mPackageManager.hasSystemFeature(unsupportedFeature);
        assertFalse("Unsupported feature should not be present: " + unsupportedFeature, hasFeature);
    }
}

//<End of snippet n. 1>


//<Beginning of snippet n. 2>


import dalvik.annotation.TestTargets;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@TestTargetClass(TelephonyManager.class)
public class TelephonyManagerTest extends AndroidTestCase {

    private TelephonyManager mTelephonyManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
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
                throw new IllegalArgumentException("Unknown phone type: " + phoneType);
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
            assertFalse("PackageManager#hasSystemFeature should NOT return true for " + feature,
                    packageManager.hasSystemFeature(feature));
            assertFalse("PackageManager#getSystemAvailableFeatures should NOT have " + feature,
                    availableFeatures.contains(feature));
        }
    }
    
    // Sanity test for unsupported telephony feature
    public void testUnsupportedTelephonyFeatures() {
        String unsupportedFeature = "FEATURE_TELEPHONY_UNSUPPORTED";
        boolean hasFeature = mTelephonyManager.hasSystemFeature(unsupportedFeature);
        assertFalse("Unsupported telephony feature should not be present: " + unsupportedFeature, hasFeature);
    }
}

//<End of snippet n. 2>