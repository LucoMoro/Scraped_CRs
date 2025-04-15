/*magnetic & orientation sensor are optional

Change-Id:I3941f3f0cb4a42dd6b9d5e791f2dff1f8867444c*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5909a57..45df732

//Synthetic comment -- @@ -79,12 +79,18 @@
assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        assertEquals(Sensor.TYPE_MAGNETIC_FIELD, sensor.getType());
        assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        assertEquals(Sensor.TYPE_ORIENTATION, sensor.getType());
        assertSensorValues(sensor);

sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
// temperature sensor is optional







