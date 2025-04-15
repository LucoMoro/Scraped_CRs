/*Added DeviceWriter and equals for Devices

Change-Id:I6c9a162bc414d3d8252dbbb5a37bb4d2ed1d6058*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 24b9260..9e65765 100644

//Synthetic comment -- @@ -274,6 +274,11 @@
public final static String NS_RESOURCES =
"http://schemas.android.com/apk/res/android";                   //$NON-NLS-1$

/** The name of the uses-library that provides "android.test.runner" */
public final static String ANDROID_TEST_RUNNER_LIB =
"android.test.runner";                                          //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java
//Synthetic comment -- index 39cfd29..ea29171 100644

//Synthetic comment -- @@ -46,4 +46,27 @@
c.mFlash = mFlash;
return c;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index ba2187f..3c868e8 100644

//Synthetic comment -- @@ -221,4 +221,33 @@
mMeta = b.mMeta;
mDefaultState = b.mDefaultState;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index acf9af3..c279ef4 100644

//Synthetic comment -- @@ -54,9 +54,6 @@
private final static String sDeviceProfilesProp = "DeviceProfiles";
private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
+ "=" + sDeviceProfilesProp + "$");
    private static String BOOLEAN_YES = "yes";
    private static String BOOLEAN_NO = "no";

private ISdkLog mLog;
private List<Device> mVendorDevices;
private List<Device> mUserDevices;
//Synthetic comment -- @@ -146,8 +143,10 @@
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.lcd.density", Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
        props.put("hw.sensors.proximity", getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));
return props;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
new file mode 100644
//Synthetic comment -- index 0000000..3ecc55e

//Synthetic comment -- @@ -0,0 +1,290 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java
//Synthetic comment -- index b74aa5b..272b9bb 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
public PowerType getChargeType() {
return mPluggedIn;
}
    
public Screen getScreen() {
return mScreen;
}
//Synthetic comment -- @@ -123,12 +123,12 @@
/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.
     * 
* @return A copy of the object.
*/
public Hardware deepCopy() {
Hardware hw = new Hardware();
        hw.mScreen = mScreen;
hw.mNetworking = new HashSet<Network>(mNetworking);
hw.mSensors = new HashSet<Sensor>(mSensors);
// Get the constant boolean value
//Synthetic comment -- @@ -156,4 +156,54 @@
hw.mPluggedIn = mPluggedIn;
return hw;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index a773005..6e8ae73 100644

//Synthetic comment -- @@ -69,4 +69,74 @@
public Point getFrameOffsetPortrait() {
return mFrameOffsetPortrait;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java
//Synthetic comment -- index 035fc46..5943988 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
Multitouch mMultitouch;
TouchScreen mMechanism;
ScreenType mScreenType;
    
public ScreenSize getSize() {
return mScreenSize;
}
//Synthetic comment -- @@ -50,11 +50,11 @@
public int getXDimension() {
return mXDimension;
}
    
public int getYDimension() {
return mYDimension;
}
    
public double getXdpi() {
return mXdpi;
}
//Synthetic comment -- @@ -70,4 +70,68 @@
public ScreenType getScreenType() {
return mScreenType;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index eb83e4d..7d12298 100644

//Synthetic comment -- @@ -49,4 +49,34 @@
public Set<String> getGlExtensions() {
return mGlExtensions;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index b93a94d..bbd7b9c 100644

//Synthetic comment -- @@ -56,4 +56,35 @@
public Hardware getHardware() {
return mHardwareOverride;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java
//Synthetic comment -- index 9778154..b30fe6e 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.sdklib.devices;

import com.android.sdklib.devices.Storage.Unit;

public class Storage {
private long mNoBytes;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceWriterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2b8474e

//Synthetic comment -- @@ -0,0 +1,127 @@







