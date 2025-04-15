/*Sensors could be disabled by any application

In the SensorManager API unregisterListener any application could
unregister any sensor. This lets any application disable a sensor
which another application is using.
This fix restricts the applications to only be able to unregister
a sensor it has previously registered to.

Change-Id:Ief926dd29f480dd97626160810dff87a8d3ac8dc*/




//Synthetic comment -- diff --git a/core/java/android/hardware/SystemSensorManager.java b/core/java/android/hardware/SystemSensorManager.java
//Synthetic comment -- index 0204e94..7375e7d 100644

//Synthetic comment -- @@ -373,7 +373,8 @@
for (Sensor s : l.getSensors()) {
disableSensorLocked(s);
}
                    // Check if the ListenerDelegate has the sensor it is trying to unregister.
                    } else if (l.hasSensor(sensor) && l.removeSensor(sensor) == 0) {
// if we have no more sensors enabled on this listener,
// take it off the list.
sListeners.remove(i);







