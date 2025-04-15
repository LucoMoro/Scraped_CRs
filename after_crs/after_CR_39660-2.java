/*Add setters to Device and related classes

This allows classes outside of the SdkLib package to create devices.

Change-Id:I33fa564b921f35a602564cf3cede3045b624f7d6*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java
//Synthetic comment -- index ea29171..4f26994 100644

//Synthetic comment -- @@ -17,22 +17,57 @@
package com.android.sdklib.devices;

public class Camera {
    private CameraLocation mLocation;
    private boolean mAutofocus;
    private boolean mFlash;

    /**
     * Creates a {@link Camera} with reasonable defaults.
     * 
     * The resulting {@link Camera} with be on the {@link CameraLocation#BACK} with both autofocus
     * and flash.
     */
    public Camera() {
        this(CameraLocation.BACK, true, true);
    }

    /**
     * Creates a new {@link Camera} which describes an on device camera and it's features.
     * @param location The location of the {@link Camera} on the device. Either
     * {@link CameraLocation#FRONT} or {@link CameraLocation#BACK}.
     * @param autofocus Whether the {@link Camera} can auto-focus.
     * @param flash Whether the {@link Camera} has flash.
     */
    public Camera(CameraLocation location, boolean autofocus, boolean flash) {
        mLocation = location;
        mAutofocus = autofocus;
        mFlash = flash;
    }

public CameraLocation getLocation() {
return mLocation;
}

    public void setLocation(CameraLocation cl) {
        mLocation = cl;
    }

public boolean hasAutofocus() {
return mAutofocus;
}

    public void setAutofocus(boolean hasAutofocus) {
        mAutofocus = hasAutofocus;
    }

public boolean hasFlash() {
return mFlash;
}

    public void setFlash(boolean flash) {
        mFlash = flash;
    }

/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index 3c868e8..e4da52e 100644

//Synthetic comment -- @@ -151,6 +151,23 @@
private Meta mMeta;
private State mDefaultState;

        public Builder() { }

        public Builder(Device d) {
            mName = d.getName();
            mManufacturer = d.getManufacturer();
            for (Software s : d.getAllSoftware()) {
                mSoftware.add(s.deepCopy());
            }
            for (State s : d.getAllStates()) {
                mState.add(s.deepCopy());
            }
            mSoftware.addAll(d.getAllSoftware());
            mState.addAll(d.getAllStates());
            mMeta = d.getMeta();
            mDefaultState = d.getDefaultState();
        }

public void setName(String name) {
mName = name;
}
//Synthetic comment -- @@ -175,6 +192,21 @@
mState.addAll(states);
}

        /**
         * Removes the first {@link State} with the given name
         * @param stateName The name of the {@link State} to remove.
         * @return Whether a {@link State} was removed or not.
         */
        public boolean removeState(String stateName) {
            for (int i = 0; i < mState.size(); i++) {
                if (stateName != null && stateName.equals(mState.get(i).getName())) {
                    mState.remove(i);
                    return true;
                }
            }
            return false;
        }

public void setMeta(Meta meta) {
mMeta = meta;
}
//Synthetic comment -- @@ -190,7 +222,7 @@
mMeta = new Meta();
}
for (State s : mState) {
                if (s.isDefaultState()) {
mDefaultState = s;
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceParser.java
//Synthetic comment -- index 10af722..43b63cb 100644

//Synthetic comment -- @@ -16,24 +16,6 @@

package com.android.sdklib.devices;

import com.android.annotations.Nullable;
import com.android.dvlib.DeviceSchema;
import com.android.resources.Density;
//Synthetic comment -- @@ -47,6 +29,22 @@
import com.android.resources.TouchScreen;
import com.android.resources.UiMode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DeviceParser {

private static class DeviceHandler extends DefaultHandler {
//Synthetic comment -- @@ -86,11 +84,6 @@
mMeta = new Meta();
} else if (DeviceSchema.NODE_HARDWARE.equals(localName)) {
mHardware = new Hardware();
} else if (DeviceSchema.NODE_SOFTWARE.equals(localName)) {
mSoftware = new Software();
} else if (DeviceSchema.NODE_STATE.equals(localName)) {
//Synthetic comment -- @@ -99,9 +92,9 @@
mHardware = mHardware.deepCopy();
String defaultState = attributes.getValue(DeviceSchema.ATTR_DEFAULT);
if ("true".equals(defaultState) || "1".equals(defaultState)) {
                    mState.setDefaultState(true);
}
                mState.setName(attributes.getValue(DeviceSchema.ATTR_NAME).trim());
} else if (DeviceSchema.NODE_CAMERA.equals(localName)) {
mCamera = new Camera();
} else if (DeviceSchema.NODE_RAM.equals(localName)
//Synthetic comment -- @@ -109,10 +102,10 @@
|| DeviceSchema.NODE_REMOVABLE_STORAGE.equals(localName)) {
mUnit = Storage.Unit.getEnum(attributes.getValue(DeviceSchema.ATTR_UNIT));
} else if (DeviceSchema.NODE_FRAME.equals(localName)) {
                mMeta.setFrameOffsetLandscape(new Point());
                mMeta.setFrameOffsetPortrait(new Point());
} else if (DeviceSchema.NODE_SCREEN.equals(localName)) {
                mHardware.setScreen(new Screen());
}
mStringAccumulator.setLength(0);
}
//Synthetic comment -- @@ -135,119 +128,115 @@
} else if (DeviceSchema.NODE_SOFTWARE.equals(localName)) {
mBuilder.addSoftware(mSoftware);
} else if (DeviceSchema.NODE_STATE.equals(localName)) {
                mState.setHardware(mHardware);
mBuilder.addState(mState);
} else if (DeviceSchema.NODE_SIXTY_FOUR.equals(localName)) {
                mMeta.setIconSixtyFour(new File(mParentFolder, getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_SIXTEEN.equals(localName)) {
                mMeta.setIconSixteen(new File(mParentFolder, getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_PATH.equals(localName)) {
                mMeta.setFrame(new File(mParentFolder, mStringAccumulator.toString().trim()));
} else if (DeviceSchema.NODE_PORTRAIT_X_OFFSET.equals(localName)) {
                mMeta.getFrameOffsetPortrait().x = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_PORTRAIT_Y_OFFSET.equals(localName)) {
                mMeta.getFrameOffsetPortrait().y = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_LANDSCAPE_X_OFFSET.equals(localName)) {
                mMeta.getFrameOffsetLandscape().x = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_LANDSCAPE_Y_OFFSET.equals(localName)) {
                mMeta.getFrameOffsetLandscape().y = getInteger(mStringAccumulator);
} else if (DeviceSchema.NODE_SCREEN_SIZE.equals(localName)) {
                mHardware.getScreen().setSize(ScreenSize.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_DIAGONAL_LENGTH.equals(localName)) {
                mHardware.getScreen().setDiagonalLength(getDouble(mStringAccumulator));
} else if (DeviceSchema.NODE_PIXEL_DENSITY.equals(localName)) {
                mHardware.getScreen().setPixelDensity(
                        Density.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_SCREEN_RATIO.equals(localName)) {
                mHardware.getScreen().setRatio(
                    ScreenRatio.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_X_DIMENSION.equals(localName)) {
                mHardware.getScreen().setXDimension(getInteger(mStringAccumulator));
} else if (DeviceSchema.NODE_Y_DIMENSION.equals(localName)) {
                mHardware.getScreen().setYDimension(getInteger(mStringAccumulator));
} else if (DeviceSchema.NODE_XDPI.equals(localName)) {
                mHardware.getScreen().setXdpi(getDouble(mStringAccumulator));
} else if (DeviceSchema.NODE_YDPI.equals(localName)) {
                mHardware.getScreen().setYdpi(getDouble(mStringAccumulator));
} else if (DeviceSchema.NODE_MULTITOUCH.equals(localName)) {
                mHardware.getScreen().setMultitouch(
                        Multitouch.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_MECHANISM.equals(localName)) {
                mHardware.getScreen().setMechanism(
                        TouchScreen.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_SCREEN_TYPE.equals(localName)) {
                mHardware.getScreen().setScreenType(
                        ScreenType.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_NETWORKING.equals(localName)) {
for (String n : getStringList(mStringAccumulator)) {
Network net = Network.getEnum(n);
if (net != null) {
                        mHardware.addNetwork(net);
}
}
} else if (DeviceSchema.NODE_SENSORS.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
Sensor sens = Sensor.getEnum(s);
if (sens != null) {
                        mHardware.addSensor(sens);
}
}
} else if (DeviceSchema.NODE_MIC.equals(localName)) {
                mHardware.setHasMic(getBool(mStringAccumulator));
} else if (DeviceSchema.NODE_CAMERA.equals(localName)) {
                mHardware.addCamera(mCamera);
mCamera = null;
} else if (DeviceSchema.NODE_LOCATION.equals(localName)) {
                mCamera.setLocation(CameraLocation.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_AUTOFOCUS.equals(localName)) {
                mCamera.setFlash(getBool(mStringAccumulator));
} else if (DeviceSchema.NODE_FLASH.equals(localName)) {
                mCamera.setFlash(getBool(mStringAccumulator));
} else if (DeviceSchema.NODE_KEYBOARD.equals(localName)) {
                mHardware.setKeyboard(Keyboard.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_NAV.equals(localName)) {
                mHardware.setNav(Navigation.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_RAM.equals(localName)) {
int val = getInteger(mStringAccumulator);
                mHardware.setRam(new Storage(val, mUnit));
} else if (DeviceSchema.NODE_BUTTONS.equals(localName)) {
                mHardware.setButtonType(ButtonType.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_INTERNAL_STORAGE.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
int val = Integer.parseInt(s);
                    mHardware.addInternalStorage(new Storage(val, mUnit));
}
} else if (DeviceSchema.NODE_REMOVABLE_STORAGE.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
if (s != null && !s.isEmpty()) {
int val = Integer.parseInt(s);
                        mHardware.addRemovableStorage(new Storage(val, mUnit));
}
}
} else if (DeviceSchema.NODE_CPU.equals(localName)) {
                mHardware.setCpu(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_GPU.equals(localName)) {
                mHardware.setGpu(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_ABI.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
Abi abi = Abi.getEnum(s);
if (abi != null) {
                        mHardware.addSupportedAbi(abi);
}
}
} else if (DeviceSchema.NODE_DOCK.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
UiMode d = UiMode.getEnum(s);
if (d != null) {
                        mHardware.addSupportedUiMode(d);
}
}
} else if (DeviceSchema.NODE_POWER_TYPE.equals(localName)) {
                mHardware.setChargeType(PowerType.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_API_LEVEL.equals(localName)) {
String val = getString(mStringAccumulator);
// Can be one of 5 forms:
//Synthetic comment -- @@ -259,63 +248,61 @@
int index;
if (val.charAt(0) == '-') {
if (val.length() == 1) { // -
                        mSoftware.setMinSdkLevel(0);
                        mSoftware.setMaxSdkLevel(Integer.MAX_VALUE);
} else { // -2
// Remove the front dash and any whitespace between it
// and the upper bound.
val = val.substring(1).trim();
                        mSoftware.setMinSdkLevel(0);
                        mSoftware.setMaxSdkLevel(Integer.parseInt(val));
}
} else if ((index = val.indexOf('-')) > 0) {
if (index == val.length() - 1) { // 1-
// Strip the last dash and any whitespace between it and
// the lower bound.
val = val.substring(0, val.length() - 1).trim();
                        mSoftware.setMinSdkLevel(Integer.parseInt(val));
                        mSoftware.setMaxSdkLevel(Integer.MAX_VALUE);
} else { // 1-2
String min = val.substring(0, index).trim();
String max = val.substring(index + 1);
                        mSoftware.setMinSdkLevel(Integer.parseInt(min));
                        mSoftware.setMaxSdkLevel(Integer.parseInt(max));
}
} else { // 1
int apiLevel = Integer.parseInt(val);
                    mSoftware.setMinSdkLevel(apiLevel);
                    mSoftware.setMaxSdkLevel(apiLevel);
}
} else if (DeviceSchema.NODE_LIVE_WALLPAPER_SUPPORT.equals(localName)) {
                mSoftware.setLiveWallpaperSupport(getBool(mStringAccumulator));
} else if (DeviceSchema.NODE_BLUETOOTH_PROFILES.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
BluetoothProfile profile = BluetoothProfile.getEnum(s);
if (profile != null) {
                        mSoftware.addBluetoothProfile(profile);
}
}
} else if (DeviceSchema.NODE_GL_VERSION.equals(localName)) {
// Guaranteed to be in the form [\d]\.[\d]
                mSoftware.setGlVersion(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_GL_EXTENSIONS.equals(localName)) {
                mSoftware.addAllGlExtensions(getStringList(mStringAccumulator));
} else if (DeviceSchema.NODE_DESCRIPTION.equals(localName)) {
                mState.setDescription(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_SCREEN_ORIENTATION.equals(localName)) {
                mState.setOrientation(ScreenOrientation.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_KEYBOARD_STATE.equals(localName)) {
                mState.setKeyState(KeyboardState.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_NAV_STATE.equals(localName)) {
// We have an extra state in our XML for nonav that
// NavigationState doesn't contain
String navState = getString(mStringAccumulator);
if (navState.equals("nonav")) {
                    mState.setNavState(NavigationState.HIDDEN);
} else {
                    mState.setNavState(NavigationState.getEnum(getString(mStringAccumulator)));
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java
//Synthetic comment -- index 272b9bb..8993cd6 100644

//Synthetic comment -- @@ -16,56 +16,85 @@

package com.android.sdklib.devices;

import com.android.resources.Keyboard;
import com.android.resources.Navigation;
import com.android.resources.UiMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hardware {
    private Screen mScreen;
    private Set<Network> mNetworking = new HashSet<Network>();
    private Set<Sensor> mSensors = new HashSet<Sensor>();
    private boolean mMic;
    private List<Camera> mCameras = new ArrayList<Camera>();
    private Keyboard mKeyboard;
    private Navigation mNav;
    private Storage mRam;
    private ButtonType mButtons;
    private List<Storage> mInternalStorage = new ArrayList<Storage>();
    private List<Storage> mRemovableStorage = new ArrayList<Storage>();
    private String mCpu;
    private String mGpu;
    private Set<Abi> mAbis = new HashSet<Abi>();
    private Set<UiMode> mUiModes = new HashSet<UiMode>();
    private PowerType mPluggedIn;

public Set<Network> getNetworking() {
return mNetworking;
}

    public void addNetwork(Network n) {
        mNetworking.add(n);
    }

    public void addAllNetworks(Collection<Network> ns) {
        mNetworking.addAll(ns);
    }

public Set<Sensor> getSensors() {
return mSensors;
}

    public void addSensor(Sensor sensor) {
        mSensors.add(sensor);
    }

    public void addAllSensors(Collection<Sensor> sensors) {
        mSensors.addAll(sensors);
    }

public boolean hasMic() {
return mMic;
}

    public void setHasMic(boolean hasMic) {
        mMic = hasMic;
    }

public List<Camera> getCameras() {
return mCameras;
}

    public void addCamera(Camera c) {
        mCameras.add(c);
    }

    public void addAllCameras(Collection<Camera> cs) {
        mCameras.addAll(cs);
    }

public Camera getCamera(int i) {
return mCameras.get(i);
}

public Camera getCamera(CameraLocation location) {
for (Camera c : mCameras) {
            if (location.equals(c.getLocation())) {
return c;
}
}
//Synthetic comment -- @@ -76,50 +105,114 @@
return mKeyboard;
}

    public void setKeyboard(Keyboard k) {
        mKeyboard = k;
    }

public Navigation getNav() {
return mNav;
}

    public void setNav(Navigation n) {
        mNav = n;
    }

public Storage getRam() {
return mRam;
}

    public void setRam(Storage ram) {
        mRam = ram;
    }

public ButtonType getButtonType() {
return mButtons;
}

    public void setButtonType(ButtonType bt) {
        mButtons = bt;
    }

public List<Storage> getInternalStorage() {
return mInternalStorage;
}

    public void addInternalStorage(Storage is) {
        mInternalStorage.add(is);
    }

    public void addAllInternalStorage(Collection<Storage> is) {
        mInternalStorage.addAll(is);
    }

public List<Storage> getRemovableStorage() {
return mRemovableStorage;
}

    public void addRemovableStorage(Storage rs) {
        mRemovableStorage.add(rs);
    }

    public void addAllRemovableStorage(Collection<Storage> rs) {
        mRemovableStorage.addAll(rs);
    }

public String getCpu() {
return mCpu;
}

    public void setCpu(String cpuName) {
        mCpu = cpuName;
    }

public String getGpu() {
return mGpu;
}

    public void setGpu(String gpuName) {
        mGpu = gpuName;
    }

public Set<Abi> getSupportedAbis() {
return mAbis;
}

    public void addSupportedAbi(Abi abi) {
        mAbis.add(abi);
    }

    public void addAllSupportedAbis(Collection<Abi> abis) {
        mAbis.addAll(abis);
    }

public Set<UiMode> getSupportedUiModes() {
return mUiModes;
}

    public void addSupportedUiMode(UiMode uiMode) {
        mUiModes.add(uiMode);
    }

    public void addAllSupportedUiModes(Collection<UiMode> uiModes) {
        mUiModes.addAll(uiModes);
    }

public PowerType getChargeType() {
return mPluggedIn;
}

    public void setChargeType(PowerType chargeType) {
        mPluggedIn = chargeType;
    }

public Screen getScreen() {
return mScreen;
}

    public void setScreen(Screen s) {
        mScreen = s;
    }

/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index 6e8ae73..911c286 100644

//Synthetic comment -- @@ -20,16 +20,20 @@
import java.io.File;

public class Meta {
    private File mIconSixtyFour;
    private File mIconSixteen;
    private File mFrame;
    private Point mFrameOffsetLandscape;
    private Point mFrameOffsetPortrait;

public File getIconSixtyFour() {
return mIconSixtyFour;
}

    public void setIconSixtyFour(File iconSixtyFour) {
        mIconSixtyFour = iconSixtyFour;
    }

public boolean hasIconSixtyFour() {
if (mIconSixtyFour != null && mIconSixtyFour.isFile()) {
return true;
//Synthetic comment -- @@ -42,6 +46,10 @@
return mIconSixteen;
}

    public void setIconSixteen(File iconSixteen) {
        mIconSixteen = iconSixteen;
    }

public boolean hasIconSixteen() {
if (mIconSixteen != null && mIconSixteen.isFile()) {
return true;
//Synthetic comment -- @@ -54,6 +62,10 @@
return mFrame;
}

    public void setFrame(File frame) {
        mFrame = frame;
    }

public boolean hasFrame() {
if (mFrame != null && mFrame.isFile()) {
return true;
//Synthetic comment -- @@ -66,10 +78,18 @@
return mFrameOffsetLandscape;
}

    public void setFrameOffsetLandscape(Point offset) {
        mFrameOffsetLandscape = offset;
    }

public Point getFrameOffsetPortrait() {
return mFrameOffsetPortrait;
}

    public void setFrameOffsetPortrait(Point offset) {
        mFrameOffsetPortrait = offset;
    }

@Override
public boolean equals(Object o) {
if (o == this) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java
//Synthetic comment -- index 5943988..958219d 100644

//Synthetic comment -- @@ -23,54 +23,106 @@


public class Screen {
    private ScreenSize mScreenSize;
    private double mDiagonalLength;
    private Density mPixelDensity;
    private ScreenRatio mScreenRatio;
    private int mXDimension;
    private int mYDimension;
    private double mXdpi;
    private double mYdpi;
    private Multitouch mMultitouch;
    private TouchScreen mMechanism;
    private ScreenType mScreenType;

public ScreenSize getSize() {
return mScreenSize;
}

    public void setSize(ScreenSize s) {
        mScreenSize = s;
    }

public double getDiagonalLength() {
return mDiagonalLength;
}

    public void setDiagonalLength(double diagonalLength) {
        mDiagonalLength = diagonalLength;
    }

public Density getPixelDensity() {
return mPixelDensity;
}

    public void setPixelDensity(Density pDensity) {
        mPixelDensity = pDensity;
    }

public ScreenRatio getRatio() {
return mScreenRatio;
}

    public void setRatio(ScreenRatio ratio) {
        mScreenRatio = ratio;
    }

public int getXDimension() {
return mXDimension;
}

    public void setXDimension(int xDimension) {
        mXDimension = xDimension;
    }

public int getYDimension() {
return mYDimension;
}

    public void setYDimension(int yDimension) {
        mYDimension = yDimension;
    }

public double getXdpi() {
return mXdpi;
}

    public void setXdpi(double xdpi) {
        mXdpi = xdpi;
    }

public double getYdpi() {
return mYdpi;
}

    public void setYdpi(double ydpi) {
        mYdpi = ydpi;
    }

public Multitouch getMultitouch() {
return mMultitouch;
}

    public void setMultitouch(Multitouch m) {
        mMultitouch = m;
    }

public TouchScreen getMechanism() {
return mMechanism;
}

    public void setMechanism(TouchScreen mechanism) {
        mMechanism = mechanism;
    }

public ScreenType getScreenType() {
return mScreenType;
}

    public void setScreenType(ScreenType screenType) {
        mScreenType = screenType;
    }

/**
* Returns a copy of the object that shares no state with it,
* but is initialized to equivalent values.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index 7d12298..b89860f 100644

//Synthetic comment -- @@ -16,40 +16,85 @@

package com.android.sdklib.devices;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Software {
    private int mMinSdkLevel = 0;
    private int mMaxSdkLevel = Integer.MAX_VALUE;
    private boolean mLiveWallpaperSupport;
    private Set<BluetoothProfile> mBluetoothProfiles = new HashSet<BluetoothProfile>();
    private String mGlVersion;
    private Set<String> mGlExtensions = new HashSet<String>();

public int getMinSdkLevel() {
return mMinSdkLevel;
}

    public void setMinSdkLevel(int sdkLevel) {
        mMinSdkLevel = sdkLevel;
    }

public int getMaxSdkLevel() {
return mMaxSdkLevel;
}

    public void setMaxSdkLevel(int sdkLevel) {
        mMaxSdkLevel = sdkLevel;
    }

public boolean hasLiveWallpaperSupport() {
return mLiveWallpaperSupport;
}

    public void setLiveWallpaperSupport(boolean liveWallpaperSupport) {
        mLiveWallpaperSupport = liveWallpaperSupport;
    }

public Set<BluetoothProfile> getBluetoothProfiles() {
return mBluetoothProfiles;
}

    public void addBluetoothProfile(BluetoothProfile bp) {
        mBluetoothProfiles.add(bp);
    }

    public void addAllBluetoothProfiles(Collection<BluetoothProfile> bps) {
        mBluetoothProfiles.addAll(bps);
    }

public String getGlVersion() {
return mGlVersion;
}

    public void setGlVersion(String version) {
        mGlVersion = version;
    }

public Set<String> getGlExtensions() {
return mGlExtensions;
}

    public void addGlExtension(String extension) {
        mGlExtensions.add(extension);
    }

    public void addAllGlExtensions(Collection<String> extensions) {
        mGlExtensions.addAll(extensions);
    }

    public Software deepCopy() {
        Software s = new Software();
        s.setMinSdkLevel(getMinSdkLevel());
        s.setMaxSdkLevel(getMaxSdkLevel());
        s.setLiveWallpaperSupport(hasLiveWallpaperSupport());
        s.addAllBluetoothProfiles(getBluetoothProfiles());
        s.setGlVersion(getGlVersion());
        s.addAllGlExtensions(getGlExtensions());
        return s;
    }

@Override
public boolean equals(Object o) {
if (o == this) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index bbd7b9c..eb75605 100644

//Synthetic comment -- @@ -21,42 +21,88 @@
import com.android.resources.ScreenOrientation;

public class State {
    private boolean mDefaultState;
    private String mName;
    private String mDescription;
    private ScreenOrientation mOrientation;
    private KeyboardState mKeyState;
    private NavigationState mNavState;
    private Hardware mHardwareOverride;

public boolean isDefaultState() {
return mDefaultState;
}

    public void setDefaultState(boolean defaultState) {
        mDefaultState = defaultState;
    }

public String getName() {
return mName;
}

    public void setName(String name) {
        mName = name;
    }

public String getDescription() {
return mDescription;
}

    public void setDescription(String description) {
        mDescription = description;
    }

public ScreenOrientation getOrientation() {
return mOrientation;
}

    public void setOrientation(ScreenOrientation orientation) {
        mOrientation = orientation;
    }

public KeyboardState getKeyState() {
return mKeyState;
}

    public void setKeyState(KeyboardState keyState) {
        mKeyState = keyState;
    }

public NavigationState getNavState() {
return mNavState;
}

    public void setNavState(NavigationState navState) {
        mNavState = navState;
    }

public Hardware getHardware() {
return mHardwareOverride;
}

    public void setHardware(Hardware hw) {
        mHardwareOverride = hw;
    }

    /**
     * Returns a copy of the object that shares no state with it,
     * but is initialized to equivalent values.
     *
     * @return A copy of the object.
     */
    public State deepCopy() {
        State s = new State();
        s.setDefaultState(isDefaultState());
        s.setName(getName());
        s.setDescription(getDescription());
        s.setOrientation(getOrientation());
        s.setKeyState(getKeyState());
        s.setNavState(getNavState());
        s.setHardware(getHardware().deepCopy());
        return s;
    }

@Override
public boolean equals(Object o) {
if (o == this) {







