/*Avd Creation Dialog: Enable sdcard if user asks for it.

Change-Id:Ia5a57139cf76cec559c557011a2acee2af58457a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index b2abb0f..27feb00 100644

//Synthetic comment -- @@ -341,20 +341,28 @@
public static Map<String, String> getHardwareProperties(State s) {
Hardware hw = s.getHardware();
Map<String, String> props = new HashMap<String, String>();
        props.put(HardwareProperties.HW_MAINKEYS,
                getBooleanVal(hw.getButtonType().equals(ButtonType.HARD)));
        props.put(HardwareProperties.HW_TRACKBALL,
                getBooleanVal(hw.getNav().equals(Navigation.TRACKBALL)));
        props.put(HardwareProperties.HW_KEYBOARD,
                getBooleanVal(hw.getKeyboard().equals(Keyboard.QWERTY)));
        props.put(HardwareProperties.HW_DPAD,
                getBooleanVal(hw.getNav().equals(Navigation.DPAD)));

Set<Sensor> sensors = hw.getSensors();
        props.put(HardwareProperties.HW_GPS, getBooleanVal(sensors.contains(Sensor.GPS)));
        props.put(HardwareProperties.HW_BATTERY,
                getBooleanVal(hw.getChargeType().equals(PowerType.BATTERY)));
        props.put(HardwareProperties.HW_ACCELEROMETER,
                getBooleanVal(sensors.contains(Sensor.ACCELEROMETER)));
        props.put(HardwareProperties.HW_ORIENTATION_SENSOR,
                getBooleanVal(sensors.contains(Sensor.GYROSCOPE)));
        props.put(HardwareProperties.HW_AUDIO_INPUT, getBooleanVal(hw.hasMic()));
        props.put(HardwareProperties.HW_SDCARD, getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put(HardwareProperties.HW_LCD_DENSITY,
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
        props.put(HardwareProperties.HW_PROXIMITY_SENSOR,
getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));
return props;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 33156b4..02241ef 100644

//Synthetic comment -- @@ -30,6 +30,43 @@
import java.util.regex.Pattern;

public class HardwareProperties {
    /** AVD/config.ini key for whether hardware buttons are present. */
    public static final String HW_MAINKEYS = "hw.mainKeys";

    /** AVD/config.ini key indicating whether trackball is present. */
    public static final String HW_TRACKBALL = "hw.trackBall";

    /** AVD/config.ini key indicating whether qwerty keyboard is present. */
    public static final String HW_KEYBOARD = "hw.keyboard";

    /** AVD/config.ini key indicating whether dpad is present. */
    public static final String HW_DPAD = "hw.dPad";

    /** AVD/config.ini key indicating whether gps is present. */
    public static final String HW_GPS = "hw.gps";

    /** AVD/config.ini key indicating whether the device is running on battery. */
    public static final String HW_BATTERY = "hw.battery";

    /** AVD/config.ini key indicating whether accelerometer is present. */
    public static final String HW_ACCELEROMETER = "hw.accelerometer";

    /** AVD/config.ini key indicating whether gyroscope is present. */
    public static final String HW_ORIENTATION_SENSOR = "hw.sensors.orientation";

    /** AVD/config.ini key indicating whether h/w mic is present. */
    public static final String HW_AUDIO_INPUT = "hw.audioInput";

    /** AVD/config.ini key indicating whether sdcard is present. */
    public static final String HW_SDCARD = "hw.sdCard";

    /** AVD/config.ini key for LCD density. */
    public static final String HW_LCD_DENSITY = "hw.lcd.density";

    /** AVD/config.ini key indicating whether proximity sensor present. */
    public static final String HW_PROXIMITY_SENSOR = "hw.sensors.proximity";


private final static Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

//Synthetic comment -- @@ -46,8 +83,8 @@
/** Comma-separate values for a property of type "enum" */
private final static String HW_PROP_ENUM = "enum";              //$NON-NLS-1$

    public final static String BOOLEAN_YES = "yes";
    public final static String BOOLEAN_NO = "no";
public final static String[] BOOLEAN_VALUES = new String[] { BOOLEAN_YES, BOOLEAN_NO };
public final static Pattern DISKSIZE_PATTERN = Pattern.compile("\\d+[MK]B"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 9310a42..93c0fe5 100644

//Synthetic comment -- @@ -868,7 +868,7 @@

Map<String, String> hwProps = DeviceManager.getHardwareProperties(device);
if (mGpuEmulation.getSelection()) {
            hwProps.put(AvdManager.AVD_INI_GPU_EMULATION, HardwareProperties.BOOLEAN_YES);
}

File avdFolder = null;
//Synthetic comment -- @@ -906,6 +906,10 @@
mBackCamera.getText().toLowerCase());
}

        if (sdName != null) {
            hwProps.put(HardwareProperties.HW_SDCARD, HardwareProperties.BOOLEAN_YES);
        }

AvdInfo avdInfo = mAvdManager.createAvd(avdFolder,
avdName,
target,







