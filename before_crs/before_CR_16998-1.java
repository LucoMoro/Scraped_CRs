/*Fix SensorTest Orientation Sensor Checks

Issue 10185

Don't test the orientation sensor if it is not present.

Change-Id:Iaaaa7410866a84ee23f34d9fb247da671cd9e202*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorTest.java
//Synthetic comment -- index 5909a57..2980dfd 100644

//Synthetic comment -- @@ -16,17 +16,19 @@

package android.hardware.cts;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Sensor.class)
public class SensorTest extends AndroidTestCase {

//Synthetic comment -- @@ -83,11 +85,12 @@
assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        assertEquals(Sensor.TYPE_ORIENTATION, sensor.getType());
        assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        // temperature sensor is optional
if (sensor != null) {
assertEquals(Sensor.TYPE_TEMPERATURE, sensor.getType());
assertSensorValues(sensor);







