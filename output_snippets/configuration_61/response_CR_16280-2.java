//<Beginning of snippet n. 1>
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.test.AndroidTestCase;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SensorFeaturesTest extends AndroidTestCase {
    private Context mContext;
    private SensorManager mSensorManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
        if (mContext != null) {
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        }
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getSensorList",
        args = {int.class}
    )
    public void testGetSensorList() throws Exception {
        if (mSensorManager == null) {
            fail("SensorManager is not initialized");
        }
        Set<String> featuresLeft = getAllSensorFeatures();
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_ACCELEROMETER, Sensor.TYPE_ACCELEROMETER);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_COMPASS, Sensor.TYPE_MAGNETIC_FIELD);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_LIGHT, Sensor.TYPE_LIGHT);
        assertFeatureForSensor(featuresLeft, PackageManager.FEATURE_SENSOR_PROXIMITY, Sensor.TYPE_PROXIMITY);
        for (String feature : featuresLeft) {
            assertTrue("Unexpected feature found: " + feature, feature != null);
        }
        for (String unexpectedFeature : featuresLeft) {
            assertFalse("Unexpected feature found: " + unexpectedFeature, unexpectedFeature != null);
        }
    }

    private Set<String> getAllSensorFeatures() throws IllegalArgumentException, IllegalAccessException {
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
        boolean hasSensorType = !sensors.isEmpty();
        String message = "PackageManager#hasSystemFeature(" + expectedFeature + ") returns " + hasSensorFeature + 
                         " but SensorManager#getSensorList(" + expectedSensorType + ") shows sensors " + sensors;
        assertEquals(message, hasSensorFeature, hasSensorType);
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
import android.util.Log;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TelephonyFeaturesTest extends AndroidTestCase {
    private TelephonyManager mTelephonyManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getContext();
        if (context != null) {
            mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getPhoneType",
        args = {}
    )
    public void testGetPhoneType() {
        if (mTelephonyManager == null) {
            fail("TelephonyManager is not initialized");
        }
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
                Log.w("TelephonyFeaturesTest", "Unexpected phone type: " + phoneType);
                break;
        }
    }

    private void assertTelephonyFeatures(String... expectedFeaturesEnabled) {
        Set<String> enabledFeatures = new HashSet<>();
        Collections.addAll(enabledFeatures, expectedFeaturesEnabled);
        Set<String> disabledFeatures = new HashSet<>();
        Collections.addAll(disabledFeatures, PackageManager.FEATURE_TELEPHONY, 
                             PackageManager.FEATURE_TELEPHONY_GSM, PackageManager.FEATURE_TELEPHONY_CDMA);
        disabledFeatures.removeAll(enabledFeatures);
        PackageManager packageManager = getContext().getPackageManager();
        Set<String> availableFeatures = new HashSet<>();
        for (FeatureInfo featureInfo : packageManager.getSystemAvailableFeatures()) {
            if (featureInfo.name != null) {
                availableFeatures.add(featureInfo.name);
            }
        }
        for (String feature : enabledFeatures) {
            assertTrue("PackageManager#hasSystemFeature should return true for " + feature, packageManager.hasSystemFeature(feature));
            assertTrue("PackageManager#getSystemAvailableFeatures should have " + feature, availableFeatures.contains(feature));
        }
        for (String feature : disabledFeatures) {
            assertFalse("PackageManager#hasSystemFeature should NOT return true for " + feature, packageManager.hasSystemFeature(feature));
            assertFalse("PackageManager#getSystemAvailableFeatures should NOT have " + feature, availableFeatures.contains(feature));
        }
    }
}
//<End of snippet n. 2>