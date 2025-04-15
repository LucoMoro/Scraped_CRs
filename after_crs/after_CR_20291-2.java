/*support humidity sensor type

Humidity Sensor

Device implementations MAY include a humidity sensor. If a device implementation includes a humidity sensor, it:
- MUST be able to deliver events at 1 Hz or greater
- MUST measure relative ambient air humidity in percent (0 to 100 %RH)
- MUST have 8-bits of accuracy or more

Change-Id:I4156842677e91de0f922284d196147ff61e71a70*/




//Synthetic comment -- diff --git a/core/java/android/hardware/Sensor.java b/core/java/android/hardware/Sensor.java
//Synthetic comment -- index f2b907b..1e83f64 100644

//Synthetic comment -- @@ -97,6 +97,13 @@
*/
public static final int TYPE_ROTATION_VECTOR = 11;

    /**
     * A constant describing a humidity sensor type.
     * See {@link android.hardware.SensorEvent SensorEvent}
     * for more details.
     */
    public static final int TYPE_HUMIDITY = 12;

/** 
* A constant describing all sensor types.
*/








//Synthetic comment -- diff --git a/core/java/android/hardware/SensorEvent.java b/core/java/android/hardware/SensorEvent.java
//Synthetic comment -- index 32ff3b3..bd7d907 100644

//Synthetic comment -- @@ -285,6 +285,14 @@
* 
* @see SensorEvent
* @see GeomagneticField
     * 
     * <h4>{@link android.hardware.Sensor#TYPE_HUMIDITY Sensor.TYPE_HUMIDITY}:
     * </h4>
     * <ul>
     * <p>
     * values[0]: Relative ambient air humidity in percent
     * </p>
     * </ul>
*/

public final float[] values;







