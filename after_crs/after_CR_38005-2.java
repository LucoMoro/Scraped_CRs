/*Added device functionality to the AVD manager

Change-Id:Ib67a7b8f123302ee75eefcb45e9cac233f026f28*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Abi.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Abi.java
//Synthetic comment -- index bb1fca3..8089842 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package com.android.sdklib.devices;

import com.android.sdklib.SdkConstants;

public enum Abi {
    ARMEABI(SdkConstants.ABI_ARMEABI),
    ARMEABI_V7A(SdkConstants.ABI_ARMEABI_V7A),
    X86(SdkConstants.ABI_INTEL_ATOM),
    MIPS(SdkConstants.ABI_MIPS);

private final String mValue;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 7e83b20..7825215 100644

//Synthetic comment -- @@ -22,7 +22,10 @@
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -30,11 +33,16 @@

import org.xml.sax.SAXException;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.devices.Storage.Unit;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.repository.PkgProps;

/**
//Synthetic comment -- @@ -45,6 +53,8 @@
private final static String sDeviceProfilesProp = "DeviceProfiles";
private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
+ "=" + sDeviceProfilesProp + "$");
    private static String BOOLEAN_YES = "yes";
    private static String BOOLEAN_NO = "no";

private ISdkLog mLog;
private List<Device> mVendorDevices;
//Synthetic comment -- @@ -75,7 +85,7 @@
* @return A list of vendor provided {@link Device}s
*/
public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (this) {
if (mVendorDevices == null) {
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
//Synthetic comment -- @@ -98,7 +108,7 @@
* @return All user created {@link Device}s
*/
public List<Device> getUserDevices() {
        synchronized (this) {
if (mUserDevices == null) {
// User devices should be saved out to
// $HOME/.android/devices.xml
//Synthetic comment -- @@ -115,6 +125,53 @@
return mUserDevices;
}

    /**
     * Returns hardware properties (defined in hardware.ini) as a {@link Map}.
     * @param The {@link State} from which to derive the hardware properties.
     * @return A {@link Map} of hardware properties.
     */
    public static Map<String, String> getHardwareProperties(State s) {
        Hardware hw = s.getHardware();
        Map<String, String> props = new HashMap<String, String>();
        props.put("hw.ramSize", Long.toString(hw.getRam().getSizeAsUnit(Unit.MiB)));
        props.put("hw.mainKeys", getBooleanVal(hw.getButtonType().equals(ButtonType.HARD)));
        props.put("hw.trackBall", getBooleanVal(hw.getNav().equals(Navigation.TRACKBALL)));
        props.put("hw.keyboard", getBooleanVal(hw.getKeyboard().equals(Keyboard.QWERTY)));
        props.put("hw.dPad", getBooleanVal(hw.getNav().equals(Navigation.DPAD)));
        Set<Sensor> sensors = hw.getSensors();
        props.put("hw.gps", getBooleanVal(sensors.contains(Sensor.GPS)));
        props.put("hw.battery", getBooleanVal(hw.getChargeType().equals(PowerType.BATTERY)));
        props.put("hw.accelerometer", getBooleanVal(sensors.contains(Sensor.ACCELEROMETER)));
        props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
        props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.lcd.density", Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
        props.put("hw.sensors.proximity", getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));
        return props;
    }

    /**
     * Returns the hardware properties defined in {@link AvdManager.HARDWARE_INI} as a {@link Map}.
     * @param The {@link Device} from which to derive the hardware properties.
     * @return A {@link Map} of hardware properties.
     */
    public static Map<String, String> getHardwareProperties(Device d) {
        Map<String, String> props = getHardwareProperties(d.getDefaultState());
        for (State s : d.getAllStates()) {
            if (s.getKeyState().equals(KeyboardState.HIDDEN)){
                props.put("hw.keyboard.lid", getBooleanVal(true));
            }
        }
        return getHardwareProperties(d.getDefaultState());
    }

    private static String getBooleanVal(boolean bool) {
        if (bool) {
            return HardwareProperties.BOOLEAN_VALUES[0];
        }
        return HardwareProperties.BOOLEAN_VALUES[1];
    }

private Collection<Device> loadDevices(File deviceXml) {
try {
return DeviceParser.parse(deviceXml);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Storage.java
//Synthetic comment -- index 705434c..9778154 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.devices;

import com.android.sdklib.devices.Storage.Unit;

public class Storage {
private long mNoBytes;

//Synthetic comment -- @@ -74,7 +76,7 @@
KiB("KiB", 1024),
MiB("MiB", 1024 * 1024),
GiB("GiB", 1024 * 1024 * 1024),
        TiB("TiB", 1024l * 1024l * 1024l * 1024l);

private String mValue;
/** The number of bytes needed to have one of the given unit */
//Synthetic comment -- @@ -103,4 +105,21 @@
return mValue;
}
}

    /**
     * Finds the largest {@link Unit} which can display the storage value as a positive integer
     * with no loss of accuracy.
     * @return The most appropriate {@link Unit}.
     */
    public Unit getApproriateUnits() {
        Unit optimalUnit = Unit.B;
        for(Unit unit : Unit.values()) {
            if(mNoBytes % unit.getNumberOfBytes() == 0) {
                optimalUnit = unit;
            } else {
                break;
            }
        }
        return optimalUnit;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4e54da0..2334893 100644

//Synthetic comment -- @@ -16,22 +16,15 @@

package com.android.sdkuilib.internal.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
//Synthetic comment -- @@ -71,13 +64,27 @@
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.devices.Abi;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Storage;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;
import com.android.util.Pair;

/**
* AVD creation or edit dialog.
//Synthetic comment -- @@ -99,6 +106,8 @@
private final ArrayList<String> mEditedProperties = new ArrayList<String>();
private final ImageFactory mImageFactory;
private final ISdkLog mSdkLog;
    private final DeviceManager mDeviceManager;
    private final List<Device> mDeviceList = new ArrayList<Device>();
/**
* The original AvdInfo if we're editing an existing AVD.
* Null when we're creating a new AVD.
//Synthetic comment -- @@ -111,6 +120,8 @@
private Combo mAbiTypeCombo;
private String mAbiType;

    private Combo mDeviceCombo;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
private Combo mSdCardSizeCombo;
//Synthetic comment -- @@ -230,6 +241,8 @@
mEditAvdInfo = editAvdInfo;

File hardwareDefs = null;
        mDeviceManager = new DeviceManager(log);
        mDeviceList.addAll(mDeviceManager.getUserDevices());

SdkManager sdkMan = avdManager.getSdkManager();
if (sdkMan != null) {
//Synthetic comment -- @@ -237,6 +250,7 @@
if (sdkPath != null) {
hardwareDefs = new File (sdkPath + File.separator +
SdkConstants.OS_SDK_TOOLS_LIB_FOLDER, SdkConstants.FN_HARDWARE_INI);
                mDeviceList.addAll(mDeviceManager.getVendorDevices(sdkPath));
}
}

//Synthetic comment -- @@ -303,10 +317,32 @@
super.widgetSelected(e);
reloadSkinCombo();
reloadAbiTypeCombo();
                reloadDeviceCombo();
validatePage();
}
});

        // Device Selection
        label = new Label(parent, SWT.NONE);
        label.setText("Device:");
        tooltip = "The device to base the AVD on. This is an optional setting and will merely " +
                "prefill the settings so they match the selected device as closely as possible.";
        label.setToolTipText(tooltip);
        mDeviceCombo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
        mDeviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mDeviceCombo.setToolTipText(tooltip);
        mDeviceCombo.setEnabled(false);
        mDeviceCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                prefillWithDeviceConfig();
                validatePage();
            }


        });

//ABI group
label = new Label(parent, SWT.NONE);
label.setText("CPU/ABI:");
//Synthetic comment -- @@ -700,6 +736,7 @@

IAndroidTarget target = mEditAvdInfo.getTarget();
if (target != null && !mCurrentTargets.isEmpty()) {
            mDeviceCombo.setEnabled(true);
// Try to select the target in the target combo.
// This will fail if the AVD needs to be repaired.
//
//Synthetic comment -- @@ -823,6 +860,67 @@
mHardwareViewer.refresh();
}

    // Sets all of the other options based on the device currently selected in mDeviceCombo
    private void prefillWithDeviceConfig() {
        int index = mDeviceCombo.getSelectionIndex();
        if (index >= 0 && index < mDeviceList.size()){
            Device d = mDeviceList.get(index);
            Hardware hw = d.getDefaultHardware();

            // Try setting the CPU/ABI
            if (mAbiTypeCombo.isEnabled()) {
                Set<Abi> abis = hw.getSupportedAbis();
                // This is O(n*m), but the two lists should be sufficiently small.
                for (Abi abi : abis) {
                    for (int i = 0; i < mAbiTypeCombo.getItemCount(); i++) {
                        if (mAbiTypeCombo.getItem(i)
                                .equals(AvdInfo.getPrettyAbiType(abi.toString()))){
                            mAbiTypeCombo.select(i);
                        }
                    }
                }
            }

            // Set the SD card size
            if (hw.getRemovableStorage().size() > 0){
                Storage card = hw.getRemovableStorage().get(0);
                enableSdCardWidgets(true);
                mSdCardSizeRadio.setSelection(true);
                mSdCardFileRadio.setSelection(false);
                Storage.Unit unit = card.getApproriateUnits();
                // Storage.Unit supports TiB and Bytes, but the AVD creator doesn't, so round
                // them to the nearest values.
                if (unit.equals(Storage.Unit.TiB)) {
                    unit = Storage.Unit.GiB;
                } else if (unit.equals(Storage.Unit.B)) {
                    unit = Storage.Unit.KiB;
                }
                for(int i = 0; i < mSdCardSizeCombo.getItemCount(); i++){
                    String u = mSdCardSizeCombo.getItem(i).trim();
                    if (unit.equals(Storage.Unit.getEnum(u))) {
                        mSdCardSizeCombo.select(i);
                        break;
                    }
                }
                mSdCardSize.setText(Long.toString(card.getSizeAsUnit(unit)));

            }

            // Set the screen resolution
            mSkinListRadio.setSelection(false);
            mSkinSizeRadio.setSelection(true);
            mSkinCombo.setEnabled(false);
            mSkinSizeWidth.setEnabled(true);
            mSkinSizeWidth.setText(Integer.toString(hw.getScreen().getXDimension()));
            mSkinSizeHeight.setEnabled(true);
            mSkinSizeHeight.setText(Integer.toString(hw.getScreen().getYDimension()));

            mProperties.putAll(DeviceManager.getHardwareProperties(d));
            mHardwareViewer.refresh();

        }
    }

@Override
protected void okPressed() {
if (createAvd()) {
//Synthetic comment -- @@ -866,6 +964,8 @@
}
}



private void reloadTargetCombo() {
String selected = null;
int index = mTargetCombo.getSelectionIndex();
//Synthetic comment -- @@ -911,6 +1011,14 @@
reloadSkinCombo();
}


    private void reloadDeviceCombo() {
        for (Device d : mDeviceList) {
            mDeviceCombo.add(d.getManufacturer() + " " + d.getName());
        }
        mDeviceCombo.setEnabled(true);
    }

private void reloadSkinCombo() {
String selected = null;
int index = mSkinCombo.getSelectionIndex();







