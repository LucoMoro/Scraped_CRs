/*Make sensors optional

Change-Id:I44526a902fa5e7ad326e82bb2eb6d4b0f6706ca3*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorTest.java
//Synthetic comment -- index 5909a57..5ae2ee9 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -75,16 +76,33 @@
List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
assertNotNull(sensors);
Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        assertEquals(Sensor.TYPE_ACCELEROMETER, sensor.getType());
        assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        assertEquals(Sensor.TYPE_MAGNETIC_FIELD, sensor.getType());
        assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        assertEquals(Sensor.TYPE_ORIENTATION, sensor.getType());
        assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
// temperature sensor is optional







