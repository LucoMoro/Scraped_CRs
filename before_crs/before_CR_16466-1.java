/*Remove @Overrides to unbreak the CTS build.

Change-Id:I1cf5667d3c3017dd0fc179006eb384ab4fd43366*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/sensors/AccelerometerTestRenderer.java b/apps/CtsVerifier/src/com/android/cts/verifier/sensors/AccelerometerTestRenderer.java
//Synthetic comment -- index d41059f..f314b63 100644

//Synthetic comment -- @@ -208,7 +208,6 @@
Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_UI);
}

    @Override
public void onAccuracyChanged(Sensor arg0, int arg1) {
// no-op
}
//Synthetic comment -- @@ -255,7 +254,6 @@
mWedge.draw(gl);
}

    @Override
public void onSensorChanged(SensorEvent event) {
if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
/*







