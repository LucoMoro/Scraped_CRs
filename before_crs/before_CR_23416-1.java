/*Skip the test of sensor which is not supported on device

Handle accelerometer, mgnetic field sensor, and orientation sensor
as well as temperature sendor, since all of them are optional.

Change-Id:If3411c142108c0d55fd51c0abc33b0062bc2e15c*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5909a57..8aa592b

//Synthetic comment -- @@ -74,17 +74,27 @@
(SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
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







