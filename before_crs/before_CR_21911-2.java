/*Sensors could be disabled by any application

In the SensorManager API unregisterListener any application could
unregister any sensor. This lets any application disable a sensor
which another application is using.
This fix restricts the applications to only be able to unregister
a sensor it has previously registered to.

Change-Id:Ief926dd29f480dd97626160810dff87a8d3ac8dc*/
//Synthetic comment -- diff --git a/core/java/android/hardware/SensorManager.java b/core/java/android/hardware/SensorManager.java
//Synthetic comment -- index aeb5d92..4b431e6 100644

//Synthetic comment -- @@ -1154,15 +1154,16 @@
for (int i=0 ; i<size ; i++) {
ListenerDelegate l = sListeners.get(i);
if (l.getListener() == listener) {
                    if (l.removeSensor(sensor) == 0) {
// if we have no more sensors enabled on this listener,
// take it off the list.
sListeners.remove(i);
}
break;
}
}
            disableSensorLocked(sensor);
}
}








