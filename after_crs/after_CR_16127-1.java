/*Test Sensor Feature Reporting

Bug 2817004

Check that the sensor features reported by PackageManager have
sensors reported by SensorManager. Also check that sensor
features not reported by PackageManager don't have any sensors.

Change-Id:I7df2dfd055df51f5212ef0166cfeb85cdcc4ab6f*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java
//Synthetic comment -- index 87ed3085..73c32af 100644

//Synthetic comment -- @@ -15,9 +15,15 @@
*/
package android.hardware.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//Synthetic comment -- @@ -26,12 +32,13 @@
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
//Synthetic comment -- @@ -304,4 +311,75 @@
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







