/*Avd Creation Dialog: Enable sdcard if user asks for it.

Change-Id:Ia5a57139cf76cec559c557011a2acee2af58457a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index b2abb0f..27feb00 100644

//Synthetic comment -- @@ -341,20 +341,28 @@
public static Map<String, String> getHardwareProperties(State s) {
Hardware hw = s.getHardware();
Map<String, String> props = new HashMap<String, String>();
        props.put("hw.mainKeys", getBooleanVal(hw.getButtonType().equals(ButtonType.HARD)));
        props.put("hw.trackBall", getBooleanVal(hw.getNav().equals(Navigation.TRACKBALL)));
        props.put("hw.keyboard", getBooleanVal(hw.getKeyboard().equals(Keyboard.QWERTY)));
        props.put("hw.dPad", getBooleanVal(hw.getNav().equals(Navigation.DPAD)));
Set<Sensor> sensors = hw.getSensors();
        props.put("hw.gps", getBooleanVal(sensors.contains(Sensor.GPS)));
        props.put("hw.battery", getBooleanVal(hw.getChargeType().equals(PowerType.BATTERY)));
        props.put("hw.accelerometer", getBooleanVal(sensors.contains(Sensor.ACCELEROMETER)));
        props.put("hw.sensors.orientation", getBooleanVal(sensors.contains(Sensor.GYROSCOPE)));
        props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
        props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.lcd.density",
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
        props.put("hw.sensors.proximity",
getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));
return props;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 33156b4..02241ef 100644

//Synthetic comment -- @@ -30,6 +30,43 @@
import java.util.regex.Pattern;

public class HardwareProperties {
private final static Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

//Synthetic comment -- @@ -46,8 +83,8 @@
/** Comma-separate values for a property of type "enum" */
private final static String HW_PROP_ENUM = "enum";              //$NON-NLS-1$

    private final static String BOOLEAN_YES = "yes";
    private final static String BOOLEAN_NO = "no";
public final static String[] BOOLEAN_VALUES = new String[] { BOOLEAN_YES, BOOLEAN_NO };
public final static Pattern DISKSIZE_PATTERN = Pattern.compile("\\d+[MK]B"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 9310a42..93c0fe5 100644

//Synthetic comment -- @@ -868,7 +868,7 @@

Map<String, String> hwProps = DeviceManager.getHardwareProperties(device);
if (mGpuEmulation.getSelection()) {
            hwProps.put(AvdManager.AVD_INI_GPU_EMULATION, HardwareProperties.BOOLEAN_VALUES[0]);
}

File avdFolder = null;
//Synthetic comment -- @@ -906,6 +906,10 @@
mBackCamera.getText().toLowerCase());
}

AvdInfo avdInfo = mAvdManager.createAvd(avdFolder,
avdName,
target,







