/*Make AVDs repairable when the base device changes or is deleted

Change-Id:I3b931ee511dc157320ca8658fe421fe28a1ff387*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java
//Synthetic comment -- index 4f26994..d7d33fe 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
@Override
public int hashCode() {
int hash = 17;
        hash = 31 * hash + mLocation.hashCode();
hash = 31 * hash + (mAutofocus ? 1 : 0);
hash = 31 * hash + (mFlash ? 1 : 0);
return hash;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index e4da52e..dcd82a2 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.sdklib.devices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.android.dvlib.DeviceSchema;

/**
* Instances of this class contain the specifications for a device. Use the
* {@link Builder} class to construct a Device object, or the
//Synthetic comment -- @@ -272,10 +272,15 @@
}

@Override
public int hashCode() {
int hash = 17;
        hash = 31 * hash + mName.hashCode();
        hash = 31 * hash + mManufacturer.hashCode();
hash = 31 * hash + mSoftware.hashCode();
hash = 31 * hash + mState.hashCode();
hash = 31 * hash + mMeta.hashCode();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 9cf0378..a58a664 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.devices;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Keyboard;
//Synthetic comment -- @@ -23,7 +24,6 @@
import com.android.resources.Navigation;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.devices.Storage.Unit;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.repository.PkgProps;
//Synthetic comment -- @@ -71,6 +71,21 @@
private static final List<DevicesChangeListener> listeners =
Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
// in the same application, which forces us to parse the XML multiple times
//Synthetic comment -- @@ -104,8 +119,25 @@
* notifications when modifications to the {@link Device} list occur.
* @param listener The listener to remove.
*/
    public void unregisterListener(DevicesChangeListener listener) {
        listeners.remove(listener);
}

/**
//Synthetic comment -- @@ -290,7 +322,6 @@
public static Map<String, String> getHardwareProperties(State s) {
Hardware hw = s.getHardware();
Map<String, String> props = new HashMap<String, String>();
        props.put("hw.ramSize", Long.toString(hw.getRam().getSizeAsUnit(Unit.MiB)));
props.put("hw.mainKeys", getBooleanVal(hw.getButtonType().equals(ButtonType.HARD)));
props.put("hw.trackBall", getBooleanVal(hw.getNav().equals(Navigation.TRACKBALL)));
props.put("hw.keyboard", getBooleanVal(hw.getKeyboard().equals(Keyboard.QWERTY)));
//Synthetic comment -- @@ -302,7 +333,6 @@
props.put("hw.sensors.orientation", getBooleanVal(sensors.contains(Sensor.GYROSCOPE)));
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.lcd.density",
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
props.put("hw.sensors.proximity",
//Synthetic comment -- @@ -324,6 +354,9 @@
props.put("hw.keyboard.lid", getBooleanVal(true));
}
}
return props;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java
//Synthetic comment -- index 8993cd6..b12f11d 100644

//Synthetic comment -- @@ -282,21 +282,50 @@
public int hashCode() {
int hash = 17;
hash = 31 * hash + mScreen.hashCode();
        hash = 31 * hash + mNetworking.hashCode();
        hash = 31 * hash + mSensors.hashCode();
hash = 31 * hash + (mMic ? 1 : 0);
        hash = 31 * hash + mCameras.hashCode();
        hash = 31 * hash + mKeyboard.hashCode();
        hash = 31 * hash + mNav.hashCode();
hash = 31 * hash + mRam.hashCode();
        hash = 31 * hash + mButtons.hashCode();
hash = 31 * hash + mInternalStorage.hashCode();
hash = 31 * hash + mRemovableStorage.hashCode();
        hash = 31 * hash + mCpu.hashCode();
        hash = 31 * hash + mGpu.hashCode();
        hash = 31 * hash + mAbis.hashCode();
        hash = 31 * hash + mUiModes.hashCode();
        hash = 31 * hash + mPluggedIn.hashCode();
return hash;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index 911c286..4c19f3f 100644

//Synthetic comment -- @@ -143,19 +143,27 @@
public int hashCode() {
int hash = 17;
if(mIconSixteen != null){
            hash = 31 * hash + mIconSixteen.hashCode();
}
if(mIconSixtyFour != null){
            hash = 31 * hash + mIconSixtyFour.hashCode();
}
if(mFrame != null){
            hash = 31 * hash + mFrame.hashCode();
}
if(mFrameOffsetLandscape != null){
            hash = 31 * hash + mFrameOffsetLandscape.hashCode();
}
if(mFrameOffsetPortrait != null){
            hash = 31 * hash + mFrameOffsetPortrait.hashCode();
}
return hash;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java
//Synthetic comment -- index 958219d..a7f4334 100644

//Synthetic comment -- @@ -170,20 +170,20 @@
@Override
public int hashCode() {
int hash = 17;
        hash = 31 * hash + mScreenSize.hashCode();
long f = Double.doubleToLongBits(mDiagonalLength);
hash = 31 * hash + (int) (f ^ (f >>> 32));
        hash = 31 * hash + mPixelDensity.hashCode();
        hash = 31 * hash + mScreenRatio.hashCode();
hash = 31 * hash + mXDimension;
hash = 31 * hash + mYDimension;
f = Double.doubleToLongBits(mXdpi);
hash = 31 * hash + (int) (f ^ (f >>> 32));
f = Double.doubleToLongBits(mYdpi);
hash = 31 * hash + (int) (f ^ (f >>> 32));
        hash = 31 * hash + mMultitouch.hashCode();
        hash = 31 * hash + mMechanism.hashCode();
        hash = 31 * hash + mScreenType.hashCode();
return hash;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index a452b6e..58f13b0 100644

//Synthetic comment -- @@ -125,14 +125,23 @@
}

@Override
public int hashCode() {
int hash = 17;
hash = 31 * hash + mMinSdkLevel;
hash = 31 * hash + mMaxSdkLevel;
hash = 31 * hash + (mLiveWallpaperSupport ? 1 : 0);
        hash = 31 * hash + mBluetoothProfiles.hashCode();
        hash = 31 * hash + mGlVersion.hashCode();
        hash = 31 * hash + mGlExtensions.hashCode();
hash = 31 * hash + (mStatusBar ? 1 : 0);
return hash;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index eb75605..1dc6961 100644

//Synthetic comment -- @@ -125,12 +125,17 @@
public int hashCode() {
int hash = 17;
hash = 31 * hash + (mDefaultState ? 1 : 0);
        hash = 31 * hash + mName.hashCode();
        hash = 31 * hash + mDescription.hashCode();
        hash = 31 * hash + mOrientation.hashCode();
        hash = 31 * hash + mKeyState.hashCode();
        hash = 31 * hash + mNavState.hashCode();
hash = 31 * hash + mHardwareOverride.hashCode();
return hash;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 54e2fa7..d2ec9db 100755

//Synthetic comment -- @@ -46,7 +46,11 @@
/** Unable to parse config.ini */
ERROR_PROPERTIES,
/** System Image folder in config.ini doesn't exist */
        ERROR_IMAGE_DIR;
}

private final String mName;
//Synthetic comment -- @@ -284,6 +288,14 @@
return String.format(
"Invalid value in image.sysdir. Run 'android update avd -n %1$s'",
mName);
case OK:
assert false;
return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 1a13a5e..52ce5d6 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.util.GrabProcessOutput;
//Synthetic comment -- @@ -182,6 +184,11 @@
public final static String AVD_INI_DATA_PARTITION_SIZE = "disk.dataPartition.size";

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$
//Synthetic comment -- @@ -1389,6 +1396,20 @@
}
}

// TODO: What about missing sdcard, skins, etc?

AvdStatus status;
//Synthetic comment -- @@ -1405,6 +1426,10 @@
status = AvdStatus.ERROR_PROPERTIES;
} else if (validImageSysdir == false) {
status = AvdStatus.ERROR_IMAGE_DIR;
} else {
status = AvdStatus.OK;
}
//Synthetic comment -- @@ -1579,10 +1604,16 @@
//FIXME: display paths to empty image folders?
status = AvdStatus.ERROR_IMAGE_DIR;
}

// now write the config file
File configIniFile = new File(avd.getDataFolderPath(), CONFIG_INI);
        writeIniFile(configIniFile, properties);

// finally create a new AvdInfo for this unbroken avd and add it to the list.
// instead of creating the AvdInfo object directly we reparse it, to detect other possible
//Synthetic comment -- @@ -1595,8 +1626,7 @@
avd.getTargetHash(),
avd.getTarget(),
avd.getAbiType(),
                properties,
                status);

replaceAvd(avd, newAvd);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman1/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman1/AvdManagerPage.java
//Synthetic comment -- index 63c53e0..6408a27 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.sdkuilib.internal.repository.sdkman1;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.AvdSelector;
//Synthetic comment -- @@ -29,23 +31,29 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends UpdaterPage implements ISdkChangeListener {

private AvdSelector mAvdSelector;

private final UpdaterData mUpdaterData;

/**
* Create the composite.
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    public AvdManagerPage(Composite parent, int swtStyle, UpdaterData updaterData) {
super(parent, swtStyle);

mUpdaterData = updaterData;
mUpdaterData.addListeners(this);

createContents(this);
postCreate();  //$hide$
}
//Synthetic comment -- @@ -79,6 +87,7 @@
@Override
public void dispose() {
mUpdaterData.removeListener(this);
super.dispose();
}

//Synthetic comment -- @@ -120,6 +129,14 @@
// nothing to be done for now.
}

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 06cdc74..8a04b51 100755

//Synthetic comment -- @@ -69,6 +69,8 @@
private final UpdaterData mUpdaterData;
/** True if this window created the UpdaterData, in which case it needs to dispose it. */
private final boolean mOwnUpdaterData;

// --- UI members ---

//Synthetic comment -- @@ -94,6 +96,7 @@
mContext = context;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
mOwnUpdaterData = true;
}

/**
//Synthetic comment -- @@ -115,6 +118,7 @@
mContext = context;
mUpdaterData = updaterData;
mOwnUpdaterData = false;
}

/**
//Synthetic comment -- @@ -161,6 +165,7 @@
public void widgetDisposed(DisposeEvent e) {
ShellSizeAndPos.saveSizeAndPos(mShell, SIZE_POS_PREFIX);    //$hide$
onAndroidSdkUpdaterDispose();                               //$hide$
}
});

//Synthetic comment -- @@ -180,7 +185,7 @@

private void createContents() {

        mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData);
mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

//Synthetic comment -- @@ -353,15 +358,13 @@
Menu menuDevices = new Menu(menuBarDevices);
menuBarDevices.setMenu(menuDevices);

        final DeviceManager manager = new DeviceManager(mUpdaterData.getSdkLog());

MenuItem createDevice = new MenuItem(menuDevices, SWT.NONE);
createDevice.setText("Create New Device");
createDevice.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
DeviceCreationDialog dlg = new DeviceCreationDialog(
                        mShell, manager, mUpdaterData.getImageFactory(), null);
if (dlg.open() == Window.OK) {
setupDevices(menuBarDevices);
}
//Synthetic comment -- @@ -370,7 +373,7 @@
new MenuItem(menuDevices, SWT.SEPARATOR);

Map<String, List<Device>> devices = new HashMap<String, List<Device>>();
        for (Device d : manager.getDevices(mUpdaterData.getOsSdkRoot())) {
List<Device> l;
if (devices.containsKey(d.getManufacturer())) {
l = devices.get(d.getManufacturer());
//Synthetic comment -- @@ -393,7 +396,7 @@
@Override
public void widgetSelected(SelectionEvent e) {
DeviceCreationDialog dlg = new DeviceCreationDialog(
                                mShell, manager, mUpdaterData.getImageFactory(), d);
if(dlg.open() == Window.OK) {
setupDevices(menuBarDevices);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index baf8a26..24e3209 100644

//Synthetic comment -- @@ -878,9 +878,6 @@
return false;
}

        hwProps.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, mDeviceManufacturer.getText());
        hwProps.put(AvdManager.AVD_INI_DEVICE_NAME, mDeviceName.getText());

// Although the device has this information, some devices have more RAM than we'd want to
// allocate to an emulator.
hwProps.put(AvdManager.AVD_INI_RAM_SIZE, mRam.getText());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 58bacec..594ed21 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -66,7 +68,10 @@
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;


/**
//Synthetic comment -- @@ -1000,21 +1005,49 @@
false /*logErrorsOnly*/);
}

        // delete the AVD
        try {
            mAvdManager.updateAvd(avdInfo, log);

// display the result
if (log instanceof MessageBoxLog) {
                ((MessageBoxLog) log).displayResult(true /* success */);
}
refresh(false /*reload*/);

        } catch (IOException e) {
            log.error(e, null);
            if (log instanceof MessageBoxLog) {
                ((MessageBoxLog) log).displayResult(false /* success */);
            }
}
}

//Synthetic comment -- @@ -1200,6 +1233,8 @@
}

private boolean isAvdRepairable(AvdStatus avdStatus) {
        return avdStatus == AvdStatus.ERROR_IMAGE_DIR;
}
}







