//<Beginning of snippet n. 1>
import dalvik.annotation.ToBeFixed;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.test.AndroidTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("deprecation")
@TestTargetClass(SensorManager.class)
public class SensorFeatureTest extends AndroidTestCase {
    private SensorManager mSensorManager;
    private Context mContext;

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getSensorList",
        args = {int.class}
    )
    public void testGetSensorList() throws Exception {
        Set<String> featuresLeft = getAllSensorFeatures();
        
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_ACCELEROMETER, Sensor.TYPE_ACCELEROMETER);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_COMPASS, Sensor.TYPE_MAGNETIC_FIELD);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_LIGHT, Sensor.TYPE_LIGHT);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_PROXIMITY, Sensor.TYPE_PROXIMITY);

        assertTrue("Assertions need to be added to this test for " + featuresLeft, featuresLeft.isEmpty());
    }

    private static Set<String> getAllSensorFeatures() throws IllegalArgumentException, IllegalAccessException {
        Set<String> sensorFeatures = new HashSet<>();
        Field[] fields = PackageManager.class.getFields();
        for (Field field : fields) {
            if (field.getName().startsWith("FEATURE_SENSOR_")) {
                String feature = (String) field.get(null);
                sensorFeatures.add(feature);
            }
        }
        return sensorFeatures;
    }

    private void assertFeatureForSensor(Set<String> featuresLeft, String expectedFeature, int expectedSensorType) {
        assertTrue("Features left " + featuresLeft + " to check did not include " + expectedFeature, featuresLeft.remove(expectedFeature));

        PackageManager packageManager = mContext.getPackageManager();
        boolean hasSensorFeature = packageManager.hasSystemFeature(expectedFeature);
        List<Sensor> sensors = mSensorManager.getSensorList(expectedSensorType);
        List<String> sensorNames = new ArrayList<>(sensors.size());
        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
        }
        boolean hasSensorType = !sensors.isEmpty();
        String message = "PackageManager#hasSystemFeature(" + expectedFeature + ") returns "
                + hasSensorFeature + " but SensorManager#getSensorList(" + expectedSensorType + ") shows sensors "
                + sensorNames;
        assertEquals(message, hasSensorFeature, hasSensorType);
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import dalvik.annotation.TestTargets;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@TestTargetClass(TelephonyManager.class)
public class TelephonyFeatureTest extends AndroidTestCase {
    private TelephonyManager mTelephonyManager;

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getPhoneType",
        args = {}
    )
    public void testGetPhoneType() {
        int phoneType = mTelephonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                assertTelephonyFeatures(PackageManager.FEATURE_TELEPHONY, PackageManager.FEATURE_TELEPHONY_GSM);
                break;
            case TelephonyManager.PHONE_TYPE_CDMA:
                assertTelephonyFeatures(PackageManager.FEATURE_TELEPHONY, PackageManager.FEATURE_TELEPHONY_CDMA);
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                assertTelephonyFeatures();
                break;
            default:
                handleUnexpectedPhoneType(phoneType);
        }
    }

    private void handleUnexpectedPhoneType(int phoneType) {
        // Log and continue gracefully
        Log.w("TelephonyFeatureTest", "Unexpected phone type: " + phoneType);
    }

    private void assertTelephonyFeatures(String... expectedFeaturesEnabled) {
        Set<String> enabledFeatures = new HashSet<>();
        Collections.addAll(enabledFeatures, expectedFeaturesEnabled);

        Set<String> disabledFeatures = new HashSet<>();
        Collections.addAll(disabledFeatures,
                PackageManager.FEATURE_TELEPHONY,
                PackageManager.FEATURE_TELEPHONY_GSM,
                PackageManager.FEATURE_TELEPHONY_CDMA);
        disabledFeatures.removeAll(enabledFeatures);

        PackageManager packageManager = getContext().getPackageManager();
        Set<String> availableFeatures = new HashSet<>();
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
}
//<End of snippet n. 2>