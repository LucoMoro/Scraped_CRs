//<Beginning of snippet n. 0>
Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_UI);
}

public void onAccuracyChanged(Sensor arg0, int arg1) {
// no-op
}
mWedge.draw(gl);
}

public void onSensorChanged(SensorEvent event) {
if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
/*

//<End of snippet n. 0>