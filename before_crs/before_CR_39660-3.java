/*Add setters to Device and related classes

This allows classes outside of the SdkLib package to create devices.

Change-Id:I33fa564b921f35a602564cf3cede3045b624f7d6*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java
//Synthetic comment -- index ea29171..4f26994 100644

//Synthetic comment -- @@ -17,22 +17,57 @@
package com.android.sdklib.devices;

public class Camera {
    CameraLocation mLocation;
    boolean mAutofocus;
    boolean mFlash;

public CameraLocation getLocation() {
return mLocation;
}

public boolean hasAutofocus() {
return mAutofocus;
}

public boolean hasFlash() {
return mFlash;
}

/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index 3c868e8..e4da52e 100644

//Synthetic comment -- @@ -151,6 +151,23 @@
private Meta mMeta;
private State mDefaultState;

public void setName(String name) {
mName = name;
}
//Synthetic comment -- @@ -175,6 +192,21 @@
mState.addAll(states);
}

public void setMeta(Meta meta) {
mMeta = meta;
}
//Synthetic comment -- @@ -190,7 +222,7 @@
mMeta = new Meta();
}
for (State s : mState) {
                if (s.mDefaultState) {
mDefaultState = s;
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceParser.java
//Synthetic comment -- index 10af722..43b63cb 100644

//Synthetic comment -- @@ -16,24 +16,6 @@

package com.android.sdklib.devices;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.android.annotations.Nullable;
import com.android.dvlib.DeviceSchema;
import com.android.resources.Density;
//Synthetic comment -- @@ -47,6 +29,22 @@
import com.android.resources.TouchScreen;
import com.android.resources.UiMode;

public class DeviceParser {

private static class DeviceHandler extends DefaultHandler {
//Synthetic comment -- @@ -86,11 +84,6 @@
mMeta = new Meta();
} else if (DeviceSchema.NODE_HARDWARE.equals(localName)) {
mHardware = new Hardware();
                mHardware.mCameras = new ArrayList<Camera>();
                mHardware.mInternalStorage = new ArrayList<Storage>();
                mHardware.mRemovableStorage = new ArrayList<Storage>();
                mHardware.mAbis = new HashSet<Abi>();
                mHardware.mUiModes = new HashSet<UiMode>();
} else if (DeviceSchema.NODE_SOFTWARE.equals(localName)) {
mSoftware = new Software();
} else if (DeviceSchema.NODE_STATE.equals(localName)) {
//Synthetic comment -- @@ -99,9 +92,9 @@
mHardware = mHardware.deepCopy();
String defaultState = attributes.getValue(DeviceSchema.ATTR_DEFAULT);
if ("true".equals(defaultState) || "1".equals(defaultState)) {
                    mState.mDefaultState = true;
}
                mState.mName = attributes.getValue(DeviceSchema.ATTR_NAME).trim();
} else if (DeviceSchema.NODE_CAMERA.equals(localName)) {
mCamera = new Camera();
} else if (DeviceSchema.NODE_RAM.equals(localName)
//Synthetic comment -- @@ -109,10 +102,10 @@
|| DeviceSchema.NODE_REMOVABLE_STORAGE.equals(localName)) {
mUnit = Storage.Unit.getEnum(attributes.getValue(DeviceSchema.ATTR_UNIT));
} else if (DeviceSchema.NODE_FRAME.equals(localName)) {
                mMeta.mFrameOffsetLandscape = new Point();
                mMeta.mFrameOffsetPortrait = new Point();
} else if (DeviceSchema.NODE_SCREEN.equals(localName)) {
                mHardware.mScreen = new Screen();
}
mStringAccumulator.setLength(0);
}
//Synthetic comment -- @@ -135,119 +128,115 @@
} else if (DeviceSchema.NODE_SOFTWARE.equals(localName)) {
mBuilder.addSoftware(mSoftware);
} else if (DeviceSchema.NODE_STATE.equals(localName)) {
                mState.mHardwareOverride = mHardware;
mBuilder.addState(mState);
} else if (DeviceSchema.NODE_SIXTY_FOUR.equals(localName)) {
                mMeta.mIconSixtyFour = new File(mParentFolder, getString(mStringAccumulator));
} else if (DeviceSchema.NODE_SIXTEEN.equals(localName)) {
                mMeta.mIconSixteen = new File(mParentFolder, getString(mStringAccumulator));
} else if (DeviceSchema.NODE_PATH.equals(localName)) {
                mMeta.mFrame = new File(mParentFolder, mStringAccumulator.toString().trim());
} else if (DeviceSchema.NODE_PORTRAIT_X_OFFSET.equals(localName)) {
                mMeta.mFrameOffsetPortrait.x = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_PORTRAIT_Y_OFFSET.equals(localName)) {
                mMeta.mFrameOffsetPortrait.y = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_LANDSCAPE_X_OFFSET.equals(localName)) {
                mMeta.mFrameOffsetLandscape.x = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_LANDSCAPE_Y_OFFSET.equals(localName)) {
                mMeta.mFrameOffsetLandscape.y = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_SCREEN_SIZE.equals(localName)) {
                mHardware.mScreen.mScreenSize = ScreenSize.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_DIAGONAL_LENGTH.equals(localName)) {
                mHardware.mScreen.mDiagonalLength = getDouble(mStringAccumulator);
} else if (DeviceSchema.NODE_PIXEL_DENSITY.equals(localName)) {
                mHardware.mScreen.mPixelDensity = Density.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_SCREEN_RATIO.equals(localName)) {
                mHardware.mScreen.mScreenRatio =
                    ScreenRatio.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_X_DIMENSION.equals(localName)) {
                mHardware.mScreen.mXDimension = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_Y_DIMENSION.equals(localName)) {
                mHardware.mScreen.mYDimension = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_XDPI.equals(localName)) {
                mHardware.mScreen.mXdpi = getDouble(mStringAccumulator);
} else if (DeviceSchema.NODE_YDPI.equals(localName)) {
                mHardware.mScreen.mYdpi = getDouble(mStringAccumulator);
} else if (DeviceSchema.NODE_MULTITOUCH.equals(localName)) {
                mHardware.mScreen.mMultitouch = Multitouch.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_MECHANISM.equals(localName)) {
                mHardware.mScreen.mMechanism = TouchScreen.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_SCREEN_TYPE.equals(localName)) {
                mHardware.mScreen.mScreenType = ScreenType.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_NETWORKING.equals(localName)) {
                Set<Network> networking = new HashSet<Network>();
for (String n : getStringList(mStringAccumulator)) {
Network net = Network.getEnum(n);
if (net != null) {
                        networking.add(net);
}
}
                mHardware.mNetworking = networking;
} else if (DeviceSchema.NODE_SENSORS.equals(localName)) {
                Set<Sensor> sensors = new HashSet<Sensor>();
for (String s : getStringList(mStringAccumulator)) {
Sensor sens = Sensor.getEnum(s);
if (sens != null) {
                        sensors.add(sens);
}
}
                mHardware.mSensors = sensors;
} else if (DeviceSchema.NODE_MIC.equals(localName)) {
                mHardware.mMic = getBool(mStringAccumulator);
} else if (DeviceSchema.NODE_CAMERA.equals(localName)) {
                mHardware.mCameras.add(mCamera);
mCamera = null;
} else if (DeviceSchema.NODE_LOCATION.equals(localName)) {
                mCamera.mLocation = CameraLocation.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_AUTOFOCUS.equals(localName)) {
                mCamera.mAutofocus = getBool(mStringAccumulator);
} else if (DeviceSchema.NODE_FLASH.equals(localName)) {
                mCamera.mFlash = getBool(mStringAccumulator);
} else if (DeviceSchema.NODE_KEYBOARD.equals(localName)) {
                mHardware.mKeyboard = Keyboard.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_NAV.equals(localName)) {
                mHardware.mNav = Navigation.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_RAM.equals(localName)) {
int val = getInteger(mStringAccumulator);
                mHardware.mRam = new Storage(val, mUnit);
} else if (DeviceSchema.NODE_BUTTONS.equals(localName)) {
                mHardware.mButtons = ButtonType.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_INTERNAL_STORAGE.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
int val = Integer.parseInt(s);
                    mHardware.mInternalStorage.add(new Storage(val, mUnit));
}
} else if (DeviceSchema.NODE_REMOVABLE_STORAGE.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
if (s != null && !s.isEmpty()) {
int val = Integer.parseInt(s);
                        mHardware.mRemovableStorage.add(new Storage(val, mUnit));
}
}
} else if (DeviceSchema.NODE_CPU.equals(localName)) {
                mHardware.mCpu = getString(mStringAccumulator);
} else if (DeviceSchema.NODE_GPU.equals(localName)) {
                mHardware.mGpu = getString(mStringAccumulator);
} else if (DeviceSchema.NODE_ABI.equals(localName)) {
                Set<Abi> abis = new HashSet<Abi>();
for (String s : getStringList(mStringAccumulator)) {
Abi abi = Abi.getEnum(s);
if (abi != null) {
                        abis.add(abi);
}
}
                mHardware.mAbis = abis;
} else if (DeviceSchema.NODE_DOCK.equals(localName)) {
                Set<UiMode> uimodes = new HashSet<UiMode>();
for (String s : getStringList(mStringAccumulator)) {
UiMode d = UiMode.getEnum(s);
if (d != null) {
                        uimodes.add(d);
}
}
                mHardware.mUiModes = uimodes;
} else if (DeviceSchema.NODE_POWER_TYPE.equals(localName)) {
                mHardware.mPluggedIn = PowerType.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_API_LEVEL.equals(localName)) {
String val = getString(mStringAccumulator);
// Can be one of 5 forms:
//Synthetic comment -- @@ -259,63 +248,61 @@
int index;
if (val.charAt(0) == '-') {
if (val.length() == 1) { // -
                        mSoftware.mMinSdkLevel = 0;
                        mSoftware.mMaxSdkLevel = Integer.MAX_VALUE;
} else { // -2
// Remove the front dash and any whitespace between it
// and the upper bound.
val = val.substring(1).trim();
                        mSoftware.mMinSdkLevel = 0;
                        mSoftware.mMaxSdkLevel = Integer.parseInt(val);
}
} else if ((index = val.indexOf('-')) > 0) {
if (index == val.length() - 1) { // 1-
// Strip the last dash and any whitespace between it and
// the lower bound.
val = val.substring(0, val.length() - 1).trim();
                        mSoftware.mMinSdkLevel = Integer.parseInt(val);
                        mSoftware.mMaxSdkLevel = Integer.MAX_VALUE;
} else { // 1-2
String min = val.substring(0, index).trim();
String max = val.substring(index + 1);
                        mSoftware.mMinSdkLevel = Integer.parseInt(min);
                        mSoftware.mMaxSdkLevel = Integer.parseInt(max);
}
} else { // 1
int apiLevel = Integer.parseInt(val);
                    mSoftware.mMinSdkLevel = apiLevel;
                    mSoftware.mMaxSdkLevel = apiLevel;
}
} else if (DeviceSchema.NODE_LIVE_WALLPAPER_SUPPORT.equals(localName)) {
                mSoftware.mLiveWallpaperSupport = getBool(mStringAccumulator);
} else if (DeviceSchema.NODE_BLUETOOTH_PROFILES.equals(localName)) {
                Set<BluetoothProfile> bps = new HashSet<BluetoothProfile>();
for (String s : getStringList(mStringAccumulator)) {
BluetoothProfile profile = BluetoothProfile.getEnum(s);
if (profile != null) {
                        bps.add(profile);
}
}
                mSoftware.mBluetoothProfiles = bps;
} else if (DeviceSchema.NODE_GL_VERSION.equals(localName)) {
// Guaranteed to be in the form [\d]\.[\d]
                mSoftware.mGlVersion = getString(mStringAccumulator);
} else if (DeviceSchema.NODE_GL_EXTENSIONS.equals(localName)) {
                mSoftware.mGlExtensions = new HashSet<String>(getStringList(mStringAccumulator));
} else if (DeviceSchema.NODE_DESCRIPTION.equals(localName)) {
                mState.mDescription = getString(mStringAccumulator);
} else if (DeviceSchema.NODE_SCREEN_ORIENTATION.equals(localName)) {
                mState.mOrientation = ScreenOrientation.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_KEYBOARD_STATE.equals(localName)) {
                mState.mKeyState = KeyboardState.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_NAV_STATE.equals(localName)) {
// We have an extra state in our XML for nonav that
// NavigationState doesn't contain
String navState = getString(mStringAccumulator);
if (navState.equals("nonav")) {
                    mState.mNavState = NavigationState.HIDDEN;
} else {
                    mState.mNavState = NavigationState.getEnum(getString(mStringAccumulator));
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java
//Synthetic comment -- index 272b9bb..8993cd6 100644

//Synthetic comment -- @@ -16,56 +16,85 @@

package com.android.sdklib.devices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.android.resources.Keyboard;
import com.android.resources.Navigation;
import com.android.resources.UiMode;

public class Hardware {
    Screen mScreen;
    Set<Network> mNetworking;
    Set<Sensor> mSensors;
    boolean mMic;
    List<Camera> mCameras;
    Keyboard mKeyboard;
    Navigation mNav;
    Storage mRam;
    ButtonType mButtons;
    List<Storage> mInternalStorage;
    List<Storage> mRemovableStorage;
    String mCpu;
    String mGpu;
    Set<Abi> mAbis;
    Set<UiMode> mUiModes;
    PowerType mPluggedIn;

public Set<Network> getNetworking() {
return mNetworking;
}

public Set<Sensor> getSensors() {
return mSensors;
}

public boolean hasMic() {
return mMic;
}

public List<Camera> getCameras() {
return mCameras;
}

public Camera getCamera(int i) {
return mCameras.get(i);
}

public Camera getCamera(CameraLocation location) {
for (Camera c : mCameras) {
            if (location.equals(c.mLocation)) {
return c;
}
}
//Synthetic comment -- @@ -76,50 +105,114 @@
return mKeyboard;
}

public Navigation getNav() {
return mNav;
}

public Storage getRam() {
return mRam;
}

public ButtonType getButtonType() {
return mButtons;
}

public List<Storage> getInternalStorage() {
return mInternalStorage;
}

public List<Storage> getRemovableStorage() {
return mRemovableStorage;
}

public String getCpu() {
return mCpu;
}

public String getGpu() {
return mGpu;
}

public Set<Abi> getSupportedAbis() {
return mAbis;
}

public Set<UiMode> getSupportedUiModes() {
return mUiModes;
}

public PowerType getChargeType() {
return mPluggedIn;
}

public Screen getScreen() {
return mScreen;
}

/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index 6e8ae73..911c286 100644

//Synthetic comment -- @@ -20,16 +20,20 @@
import java.io.File;

public class Meta {
    File mIconSixtyFour;
    File mIconSixteen;
    File mFrame;
    Point mFrameOffsetLandscape;
    Point mFrameOffsetPortrait;

public File getIconSixtyFour() {
return mIconSixtyFour;
}

public boolean hasIconSixtyFour() {
if (mIconSixtyFour != null && mIconSixtyFour.isFile()) {
return true;
//Synthetic comment -- @@ -42,6 +46,10 @@
return mIconSixteen;
}

public boolean hasIconSixteen() {
if (mIconSixteen != null && mIconSixteen.isFile()) {
return true;
//Synthetic comment -- @@ -54,6 +62,10 @@
return mFrame;
}

public boolean hasFrame() {
if (mFrame != null && mFrame.isFile()) {
return true;
//Synthetic comment -- @@ -66,10 +78,18 @@
return mFrameOffsetLandscape;
}

public Point getFrameOffsetPortrait() {
return mFrameOffsetPortrait;
}

@Override
public boolean equals(Object o) {
if (o == this) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java
//Synthetic comment -- index 5943988..958219d 100644

//Synthetic comment -- @@ -23,54 +23,106 @@


public class Screen {
    ScreenSize mScreenSize;
    double mDiagonalLength;
    Density mPixelDensity;
    ScreenRatio mScreenRatio;
    int mXDimension;
    int mYDimension;
    double mXdpi;
    double mYdpi;
    Multitouch mMultitouch;
    TouchScreen mMechanism;
    ScreenType mScreenType;

public ScreenSize getSize() {
return mScreenSize;
}
public double getDiagonalLength() {
return mDiagonalLength;
}
public Density getPixelDensity() {
return mPixelDensity;
}
public ScreenRatio getRatio() {
return mScreenRatio;
}
public int getXDimension() {
return mXDimension;
}

public int getYDimension() {
return mYDimension;
}

public double getXdpi() {
return mXdpi;
}
public double getYdpi() {
return mYdpi;
}
public Multitouch getMultitouch() {
return mMultitouch;
}
public TouchScreen getMechanism() {
return mMechanism;
}
public ScreenType getScreenType() {
return mScreenType;
}

/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index 7d12298..b89860f 100644

//Synthetic comment -- @@ -16,40 +16,85 @@

package com.android.sdklib.devices;

import java.util.Set;

public class Software {
    int mMinSdkLevel = 0;
    int mMaxSdkLevel = Integer.MAX_VALUE;
    boolean mLiveWallpaperSupport;
    Set<BluetoothProfile> mBluetoothProfiles;
    String mGlVersion;
    Set<String> mGlExtensions;

public int getMinSdkLevel() {
return mMinSdkLevel;
}

public int getMaxSdkLevel() {
return mMaxSdkLevel;
}

public boolean hasLiveWallpaperSupport() {
return mLiveWallpaperSupport;
}

public Set<BluetoothProfile> getBluetoothProfiles() {
return mBluetoothProfiles;
}

public String getGlVersion() {
return mGlVersion;
}

public Set<String> getGlExtensions() {
return mGlExtensions;
}

@Override
public boolean equals(Object o) {
if (o == this) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index bbd7b9c..eb75605 100644

//Synthetic comment -- @@ -21,42 +21,88 @@
import com.android.resources.ScreenOrientation;

public class State {
    boolean mDefaultState;
    String mName;
    String mDescription;
    ScreenOrientation mOrientation;
    KeyboardState mKeyState;
    NavigationState mNavState;
    Hardware mHardwareOverride;

public boolean isDefaultState() {
return mDefaultState;
}

public String getName() {
return mName;
}

public String getDescription() {
return mDescription;
}

public ScreenOrientation getOrientation() {
return mOrientation;
}

public KeyboardState getKeyState() {
return mKeyState;
}

public NavigationState getNavState() {
return mNavState;
}

public Hardware getHardware() {
return mHardwareOverride;
}

@Override
public boolean equals(Object o) {
if (o == this) {







