/*support humidity sensor type

Change-Id:I4156842677e91de0f922284d196147ff61e71a70*/




//Synthetic comment -- diff --git a/core/java/android/hardware/Sensor.java b/core/java/android/hardware/Sensor.java
//Synthetic comment -- index f2b907b..595c7d1 100644

//Synthetic comment -- @@ -97,6 +97,13 @@
*/
public static final int TYPE_ROTATION_VECTOR = 11;

    /**
     * A constant describing a relative humidity sensor type.
     * See {@link android.hardware.SensorEvent SensorEvent}
     * for more details.
     */
    public static final int TYPE_RELATIVE_HUMIDITY = 12;

/** 
* A constant describing all sensor types.
*/








//Synthetic comment -- diff --git a/core/java/android/hardware/SensorEvent.java b/core/java/android/hardware/SensorEvent.java
//Synthetic comment -- index 32ff3b3..2031fdb 100644

//Synthetic comment -- @@ -282,7 +282,65 @@
* in the clockwise direction (mathematically speaking, it should be
* positive in the counter-clockwise direction).
* </p>
     *
     * <h4>{@link android.hardware.Sensor#TYPE_RELATIVE_HUMIDITY
     * Sensor.TYPE_RELATIVE_HUMIDITY}:</h4>
     * <ul>
     * <p>
     * values[0]: Relative ambient air humidity in percent
     * </p>
     * </ul>
     * <p>
     * When relative ambient air humidity and ambient temperature are
     * measured, the dew point and absolute humidity can be calculated.
     * </p>
     * <u>Dew Point</u>
     * <p>
     * The dew point is the temperature to which a given parcel of air must be
     * cooled, at constant barometric pressure, for water vapor to condense
     * into water.
     * </p>
     * <center><pre>
     *                    ln(RH/100%) + m&#183;t/(T<sub>n</sub>+t)
     * t<sub>d</sub>(t,RH) = T<sub>n</sub> &#183; ------------------------------
     *                 m - [ln(RH/100%) + m&#183;t/(T<sub>n</sub>+t)]
     * </pre></center>
     * <dl>
     * <dt>t<sub>d</sub></dt> <dd>dew point temperature in &deg;C</dd>
     * <dt>t</dt>             <dd>actual temperature in &deg;C</dd>
     * <dt>RH</dt>            <dd>actual relative humidity in %</dd>
     * <dt>m</dt>             <dd>17.62</dd>
     * <dt>T<sub>n</sub></dt> <dd>243.12 &deg;C</dd>
     * </dl>
     * <p>for example:</p>
     * <pre class="prettyprint">
     * h = Math.log(rh / 100.0) + (17.62 * t) / (243.12 + t);
     * td = 243.12 * h / (17.62 - h);
     * </pre>
     * <u>Absolute Humidity</u>
     * <p>
     * The absolute humidity is the mass of water vapor in a particular volume
     * of dry air. The unit is g/m<sup>3</sup>.
     * </p>
     * <center><pre>
     *                    RH/100%&#183;A&#183;exp(m&#183;t/(T<sub>n</sub>+t))
     * d<sub>v</sub>(t,RH) = 216.7 &#183; -------------------------
     *                           273.15 + t
     * </pre></center>
     * <dl>
     * <dt>d<sub>v</sub></dt> <dd>absolute humidity in g/m<sup>3</sup></dd>
     * <dt>t</dt>             <dd>actual temperature in &deg;C</dd>
     * <dt>RH</dt>            <dd>actual relative humidity in %</dd>
     * <dt>m</dt>             <dd>17.62</dd>
     * <dt>T<sub>n</sub></dt> <dd>243.12 &deg;C</dd>
     * <dt>A</dt>             <dd>6.112 hPa</dd>
     * </dl>
     * <p>for example:</p>
     * <pre class="prettyprint">
     * dv = 216.7 *
     * (rh / 100.0 * 6.112 * Math.exp(17.62 * t / (243.12 + t)) / (273.15 + t));
     * </pre>
     *
* @see SensorEvent
* @see GeomagneticField
*/







