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

package android.app.cts;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.Camera.Parameters;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.test.InstrumentationTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test for checking that the {@link PackageManager} is reporting the correct features.
 */
public class SystemFeaturesTest extends InstrumentationTestCase {

    private Context mContext;
    private PackageManager mPackageManager;
    private HashSet<String> mAvailableFeatures;

    private ActivityManager mActivityManager;
    private LocationManager mLocationManager;
    private SensorManager mSensorManager;
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        mContext = instrumentation.getContext();
        mPackageManager = mContext.getPackageManager();
        mAvailableFeatures = new HashSet<String>();
        if (mPackageManager.getSystemAvailableFeatures() != null) {
            for (FeatureInfo feature : mPackageManager.getSystemAvailableFeatures()) {
                mAvailableFeatures.add(feature.name);
            }
        }
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * Check for features improperly prefixed with "android." that are not defined in
     * {@link PackageManager}.
     */
    public void testFeatureNamespaces() throws IllegalArgumentException, IllegalAccessException {
        Set<String> officialFeatures = getFeatureConstantsNames("FEATURE_");
        assertFalse(officialFeatures.isEmpty());

        Set<String> notOfficialFeatures = new HashSet<String>(mAvailableFeatures);
        notOfficialFeatures.removeAll(officialFeatures);

        for (String featureName : notOfficialFeatures) {
            if (featureName != null) {
                assertFalse("Use a different namespace than 'android' for " + featureName,
                        featureName.startsWith("android"));
            }
        }
    }

    public void testCameraFeatures() {
        Camera camera = null;
        try {
            // Try getting a camera. This is unlikely to fail but implentations without a camera
            // could return null or throw an exception.
            camera = Camera.open();
            if (camera != null) {
                assertAvailable(PackageManager.FEATURE_CAMERA);

                Camera.Parameters params = camera.getParameters();
                if (!Parameters.FOCUS_MODE_FIXED.equals(params.getFocusMode())) {
                    assertAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
                } else {
                    assertNotAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
                }

                if (params.getFlashMode() != null) {
                    assertAvailable(PackageManager.FEATURE_CAMERA_FLASH);
                } else {
                    assertNotAvailable(PackageManager.FEATURE_CAMERA_FLASH);
                }
            } else {
                assertNotAvailable(PackageManager.FEATURE_CAMERA);
                assertNotAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
                assertNotAvailable(PackageManager.FEATURE_CAMERA_FLASH);
            }
        } catch (RuntimeException e) {
            assertNotAvailable(PackageManager.FEATURE_CAMERA);
            assertNotAvailable(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
            assertNotAvailable(PackageManager.FEATURE_CAMERA_FLASH);
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
    }

    public void testLiveWallpaperFeature() {
        try {
            Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            assertAvailable(PackageManager.FEATURE_LIVE_WALLPAPER);
        } catch (ActivityNotFoundException e) {
            assertNotAvailable(PackageManager.FEATURE_LIVE_WALLPAPER);
        }
    }

    public void testLocationFeatures() {
        if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            assertAvailable(PackageManager.FEATURE_LOCATION);
            assertAvailable(PackageManager.FEATURE_LOCATION_GPS);
        } else {
            assertNotAvailable(PackageManager.FEATURE_LOCATION_GPS);
        }

        if (mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            assertAvailable(PackageManager.FEATURE_LOCATION);
            assertAvailable(PackageManager.FEATURE_LOCATION_NETWORK);
        } else {
            assertNotAvailable(PackageManager.FEATURE_LOCATION_NETWORK);
        }
    }

    /**
     * Check that the sensor features reported by the PackageManager correspond to the sensors
     * returned by {@link SensorManager#getSensorList(int)}.
     */
    public void testSensorFeatures() throws Exception {
        Set<String> featuresLeft = getFeatureConstantsNames("FEATURE_SENSOR_");

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

    /** Get a list of feature constants in PackageManager matching a prefix. */
    private static Set<String> getFeatureConstantsNames(String prefix)
            throws IllegalArgumentException, IllegalAccessException {
        Set<String> features = new HashSet<String>();
        Field[] fields = PackageManager.class.getFields();
        for (Field field : fields) {
            if (field.getName().startsWith(prefix)) {
                String feature = (String) field.get(null);
                features.add(feature);
            }
        }
        return features;
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

        boolean hasSensorFeature = mPackageManager.hasSystemFeature(expectedFeature);

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

    /**
     * Check that the {@link TelephonyManager#getPhoneType()} matches the reported features.
     */
    public void testTelephonyFeatures() {
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
        PackageManager packageManager = mContext.getPackageManager();
        Set<String> availableFeatures = new HashSet<String>();
        for (FeatureInfo featureInfo : packageManager.getSystemAvailableFeatures()) {
            availableFeatures.add(featureInfo.name);
        }

        for (String feature : enabledFeatures) {
            assertAvailable(feature);
        }

        for (String feature : disabledFeatures) {
            assertNotAvailable(feature);
        }
    }

    public void testTouchScreenFeatures() {
        ConfigurationInfo configInfo = mActivityManager.getDeviceConfigurationInfo();
        if (configInfo.reqTouchScreen != Configuration.TOUCHSCREEN_NOTOUCH) {
            assertAvailable(PackageManager.FEATURE_TOUCHSCREEN);
        } else {
            assertNotAvailable(PackageManager.FEATURE_TOUCHSCREEN);
        }

        // TODO: Add tests for the other touchscreen features.
    }


    public void testWifiFeature() throws Exception {
        boolean enabled = mWifiManager.isWifiEnabled();
        try {
            // WifiManager is hard-coded to return true, but in other implementations this could
            // return false for devices that don't have WiFi.
            if (mWifiManager.setWifiEnabled(true)) {
                assertAvailable(PackageManager.FEATURE_WIFI);
            } else {
                assertNotAvailable(PackageManager.FEATURE_WIFI);
            }
        } finally {
            if (!enabled) {
                mWifiManager.setWifiEnabled(false);
            }
        }
    }

    private void assertAvailable(String feature) {
        assertTrue("PackageManager#hasSystemFeature should return true for " + feature,
                mPackageManager.hasSystemFeature(feature));
        assertTrue("PackageManager#getSystemAvailableFeatures should have " + feature,
                mAvailableFeatures.contains(feature));
    }

    private void assertNotAvailable(String feature) {
        assertFalse("PackageManager#hasSystemFeature should NOT return true for " + feature,
                mPackageManager.hasSystemFeature(feature));
        assertFalse("PackageManager#getSystemAvailableFeatures should NOT have " + feature,
                mAvailableFeatures.contains(feature));
    }
}








//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java
//Synthetic comment -- index 73c32af..136d077 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//Synthetic comment -- @@ -32,13 +31,7 @@
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.List;

@SuppressWarnings("deprecation")
@TestTargetClass(SensorManager.class)
//Synthetic comment -- @@ -311,75 +304,4 @@
assertTrue(Math.abs(orientation[2]) <= TWO_PI);
}
}
}








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index c07c557..9ecbf20 100644

//Synthetic comment -- @@ -22,8 +22,6 @@
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
//Synthetic comment -- @@ -33,9 +31,6 @@
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.regex.Pattern;

@TestTargetClass(TelephonyManager.class)
//Synthetic comment -- @@ -408,71 +403,4 @@
}
}
}
}







