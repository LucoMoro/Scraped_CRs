/*Added device functionality to the AVD manager

Change-Id:Ib67a7b8f123302ee75eefcb45e9cac233f026f28*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Abi.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Abi.java
//Synthetic comment -- index bb1fca3..8089842 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package com.android.sdklib.devices;

public enum Abi {
    ARMEABI("armeabi"),
    ARMEABI_V7A("armeabi-v7a"),
    X86("x86"),
    MIPS("mips");

private final String mValue;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 7e83b20..7825215 100644

//Synthetic comment -- @@ -22,7 +22,10 @@
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -30,11 +33,16 @@

import org.xml.sax.SAXException;

import com.android.annotations.Nullable;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.repository.PkgProps;

/**
//Synthetic comment -- @@ -45,6 +53,8 @@
private final static String sDeviceProfilesProp = "DeviceProfiles";
private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
+ "=" + sDeviceProfilesProp + "$");

private ISdkLog mLog;
private List<Device> mVendorDevices;
//Synthetic comment -- @@ -75,7 +85,7 @@
* @return A list of vendor provided {@link Device}s
*/
public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (mVendorDevices) {
if (mVendorDevices == null) {
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
//Synthetic comment -- @@ -98,7 +108,7 @@
* @return All user created {@link Device}s
*/
public List<Device> getUserDevices() {
        synchronized (mUserDevices) {
if (mUserDevices == null) {
// User devices should be saved out to
// $HOME/.android/devices.xml
//Synthetic comment -- @@ -115,6 +125,53 @@
return mUserDevices;
}

private Collection<Device> loadDevices(File deviceXml) {
try {
return DeviceParser.parse(deviceXml);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java
//Synthetic comment -- index 705434c..9778154 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.devices;

public class Storage {
private long mNoBytes;

//Synthetic comment -- @@ -74,7 +76,7 @@
KiB("KiB", 1024),
MiB("MiB", 1024 * 1024),
GiB("GiB", 1024 * 1024 * 1024),
        TiB("TiB", 1024 * 1024 * 1024 * 1024);

private String mValue;
/** The number of bytes needed to have one of the given unit */
//Synthetic comment -- @@ -103,4 +105,21 @@
return mValue;
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 5d40a40..e42254f 100644

//Synthetic comment -- @@ -16,22 +16,15 @@

package com.android.sdkuilib.internal.widgets;

import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;
import com.android.util.Pair;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
//Synthetic comment -- @@ -71,13 +64,27 @@
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;

/**
* AVD creation or edit dialog.
//Synthetic comment -- @@ -99,6 +106,8 @@
private final ArrayList<String> mEditedProperties = new ArrayList<String>();
private final ImageFactory mImageFactory;
private final ISdkLog mSdkLog;
/**
* The original AvdInfo if we're editing an existing AVD.
* Null when we're creating a new AVD.
//Synthetic comment -- @@ -111,6 +120,8 @@
private Combo mAbiTypeCombo;
private String mAbiType;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
private Combo mSdCardSizeCombo;
//Synthetic comment -- @@ -230,6 +241,8 @@
mEditAvdInfo = editAvdInfo;

File hardwareDefs = null;

SdkManager sdkMan = avdManager.getSdkManager();
if (sdkMan != null) {
//Synthetic comment -- @@ -237,6 +250,7 @@
if (sdkPath != null) {
hardwareDefs = new File (sdkPath + File.separator +
SdkConstants.OS_SDK_TOOLS_LIB_FOLDER, SdkConstants.FN_HARDWARE_INI);
}
}

//Synthetic comment -- @@ -303,10 +317,32 @@
super.widgetSelected(e);
reloadSkinCombo();
reloadAbiTypeCombo();
validatePage();
}
});

//ABI group
label = new Label(parent, SWT.NONE);
label.setText("CPU/ABI:");
//Synthetic comment -- @@ -729,6 +765,7 @@

IAndroidTarget target = mEditAvdInfo.getTarget();
if (target != null && !mCurrentTargets.isEmpty()) {
// Try to select the target in the target combo.
// This will fail if the AVD needs to be repaired.
//
//Synthetic comment -- @@ -852,6 +889,67 @@
mHardwareViewer.refresh();
}

@Override
protected void okPressed() {
if (createAvd()) {
//Synthetic comment -- @@ -895,6 +993,8 @@
}
}

private void reloadTargetCombo() {
String selected = null;
int index = mTargetCombo.getSelectionIndex();
//Synthetic comment -- @@ -940,6 +1040,14 @@
reloadSkinCombo();
}

private void reloadSkinCombo() {
String selected = null;
int index = mSkinCombo.getSelectionIndex();







