/*support humidity sensor type

Change-Id:I4156842677e91de0f922284d196147ff61e71a70*/
//Synthetic comment -- diff --git a/core/java/android/hardware/Sensor.java b/core/java/android/hardware/Sensor.java
//Synthetic comment -- index f2b907b..595c7d1 100644

//Synthetic comment -- @@ -97,6 +97,13 @@
*/
public static final int TYPE_ROTATION_VECTOR = 11;

/** 
* A constant describing all sensor types.
*/








//Synthetic comment -- diff --git a/core/java/android/hardware/SensorEvent.java b/core/java/android/hardware/SensorEvent.java
//Synthetic comment -- index 32ff3b3..fc922db 100644

//Synthetic comment -- @@ -285,6 +285,14 @@
* 
* @see SensorEvent
* @see GeomagneticField
*/

public final float[] values;







