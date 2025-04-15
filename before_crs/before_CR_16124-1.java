/*Don't rely on map to store layout device configs.

Relying on maps means that we don't control the order of the configs
in the UI. Mac/Windows show a different order already, and it's just
luck that the Mac one matches the content of the XML.

Now we keep the order from the XML and display it as-is in the UI.

Change-Id:I900f330a4ea9059fe27df2bca6c3a7a770737ae4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigManagerDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigManagerDialog.java
//Synthetic comment -- index 8b585dd..6bfa9da 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
//Synthetic comment -- @@ -47,7 +48,7 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import java.util.Map;
import java.util.Map.Entry;

/**
//Synthetic comment -- @@ -133,7 +134,7 @@
}
} else if (parentElement instanceof LayoutDevice) {
LayoutDevice device = (LayoutDevice)parentElement;
                return device.getConfigs().entrySet().toArray();
}

return null;
//Synthetic comment -- @@ -182,8 +183,7 @@

/**
* Label provider for the {@link TreeViewer}.
     * Supported elements are {@link DeviceType}, {@link LayoutDevice}, and {@link Entry} (where
     * the key is a {@link String} object, and the value is a {@link FolderConfiguration} object).
*
*/
private final static class DeviceLabelProvider implements ITableLabelProvider {
//Synthetic comment -- @@ -197,11 +197,11 @@
if (columnIndex == 0) {
return ((LayoutDevice)element).getName();
}
            } else if (element instanceof Entry<?, ?>) {
if (columnIndex == 0) {
                    return (String)((Entry<?,?>)element).getKey();
} else {
                    return ((Entry<?,?>)element).getValue().toString();
}
}
return null;
//Synthetic comment -- @@ -386,18 +386,18 @@
// are we copying the full device?
if (selection.entry == null) {
// get the config from the origin device
                    Map<String, FolderConfiguration> configs = selection.device.getConfigs();

// and copy them in the target device
                    for (Entry<String, FolderConfiguration> entry : configs.entrySet()) {
// we need to make a copy of the config object, or it could be modified
// in default/addon by editing the version in the new device.
FolderConfiguration copy = new FolderConfiguration();
                        copy.set(entry.getValue());

// the name can stay the same since we are copying a full device
// and the target device has its own new name.
                        mManager.addUserConfiguration(targetDevice, entry.getKey(), copy);
}
} else {
// only copy the config. target device is not the same as the selection, don't
//Synthetic comment -- @@ -533,12 +533,12 @@
// this is the easy case. no config to select
path = new Object[] { DeviceType.CUSTOM, device };
} else {
            // this is more complex. we have the configName, but the tree contains Entry<?,?>
// Look for the entry.
            Entry<?, ?> match = null;
            for (Entry<?, ?> entry : device.getConfigs().entrySet()) {
                if (entry.getKey().equals(configName)) {
                    match = entry;
break;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index c267422..ef426df 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IStyleResourceValue;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -66,13 +67,12 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;

/**
* A composite that displays the current configuration displayed in a Graphical Layout Editor.
//Synthetic comment -- @@ -222,7 +222,7 @@
for (LayoutDevice d : mDeviceList) {
if (d.getName().equals(values[0])) {
device = d;
                        FolderConfiguration config = device.getConfigs().get(values[1]);
if (config != null) {
configName = values[1];

//Synthetic comment -- @@ -731,9 +731,8 @@
FolderConfiguration testConfig = new FolderConfiguration();

mainloop: for (LayoutDevice device : mDeviceList) {
            for (Entry<String, FolderConfiguration> entry :
                    device.getConfigs().entrySet()) {
                testConfig.set(entry.getValue());

// look on the locales.
for (int i = 0 ; i < mLocaleList.size() ; i++) {
//Synthetic comment -- @@ -748,14 +747,14 @@
// where the edited file is a best config.
if (anyDeviceMatch == null) {
anyDeviceMatch = device;
                            anyConfigMatchName = entry.getKey();
anyLocaleIndex = i;
}

if (isCurrentFileBestMatchFor(testConfig)) {
// this is what we want.
bestDeviceMatch = device;
                            bestConfigMatchName = entry.getKey();
bestLocaleIndex = i;
break mainloop;
}
//Synthetic comment -- @@ -823,7 +822,7 @@
int configIndex = mDeviceConfigCombo.getSelectionIndex();
if (configIndex != -1) {
String configName = mDeviceConfigCombo.getItem(configIndex);
            FolderConfiguration currentConfig = mState.device.getConfigs().get(configName);
if (mEditedConfig.isMatchFor(currentConfig)) {
currentConfigIsCompatible = true; // current config is compatible
if (needBestMatch == false || isCurrentFileBestMatchFor(currentConfig)) {
//Synthetic comment -- @@ -840,9 +839,8 @@
// first look in the current device.
String matchName = null;
int localeIndex = -1;
            Map<String, FolderConfiguration> configs = mState.device.getConfigs();
            mainloop: for (Entry<String, FolderConfiguration> entry : configs.entrySet()) {
                testConfig.set(entry.getValue());

// loop on the locales.
for (int i = 0 ; i < mLocaleList.size() ; i++) {
//Synthetic comment -- @@ -854,7 +852,7 @@

if (mEditedConfig.isMatchFor(testConfig) &&
isCurrentFileBestMatchFor(testConfig)) {
                        matchName = entry.getKey();
localeIndex = i;
break mainloop;
}
//Synthetic comment -- @@ -1303,13 +1301,12 @@
mDeviceCombo.select(0);

if (mDeviceList.size() > 0) {
                Map<String, FolderConfiguration> configs = mDeviceList.get(0).getConfigs();
                Set<String> configNames = configs.keySet();
                for (String name : configNames) {
                    mDeviceConfigCombo.add(name);
}
mDeviceConfigCombo.select(0);
                if (configNames.size() == 1) {
mDeviceConfigCombo.setEnabled(false);
}
}
//Synthetic comment -- @@ -1377,7 +1374,7 @@
if (mState.device != null) {
int index = mDeviceConfigCombo.getSelectionIndex();
if (index != -1) {
                    FolderConfiguration oldConfig = mState.device.getConfigs().get(
mDeviceConfigCombo.getItem(index));

LayoutDevice newDevice = mDeviceList.get(deviceIndex);
//Synthetic comment -- @@ -1449,40 +1446,37 @@
* @return the name of the closest config match, or possibly null if no configs are compatible
* (this can only happen if the configs don't have a single qualifier that is the same).
*/
    private String getClosestMatch(FolderConfiguration oldConfig,
            Map<String, FolderConfiguration> configs) {

// create 2 lists as we're going to go through one and put the candidates in the other.
        ArrayList<Entry<String, FolderConfiguration>> list1 =
            new ArrayList<Entry<String,FolderConfiguration>>();
        ArrayList<Entry<String, FolderConfiguration>> list2 =
            new ArrayList<Entry<String,FolderConfiguration>>();

        list1.addAll(configs.entrySet());

final int count = FolderConfiguration.getQualifierCount();
for (int i = 0 ; i < count ; i++) {
// compute the new candidate list by only taking configs that have
// the same i-th qualifier as the old config
            for (Entry<String, FolderConfiguration> entry : list1) {
ResourceQualifier oldQualifier = oldConfig.getQualifier(i);

                FolderConfiguration config = entry.getValue();
                ResourceQualifier newQualifier = config.getQualifier(i);

if (oldQualifier == null) {
if (newQualifier == null) {
                        list2.add(entry);
}
} else if (oldQualifier.equals(newQualifier)) {
                    list2.add(entry);
}
}

// at any moment if the new candidate list contains only one match, its name
// is returned.
if (list2.size() == 1) {
                return list2.get(0).getKey();
}

// if the list is empty, then all the new configs failed. It is considered ok, and
//Synthetic comment -- @@ -1500,7 +1494,7 @@
// (if there are more than one, then there's a duplicate config and it doesn't matter,
// we take the first one).
if (list1.size() > 0) {
            return list1.get(0).getKey();
}

return null;
//Synthetic comment -- @@ -1514,22 +1508,20 @@
mDeviceConfigCombo.removeAll();

if (mState.device != null) {
            Set<String> configNames = mState.device.getConfigs().keySet();

int selectionIndex = 0;
int i = 0;

            for (String name : configNames) {
                mDeviceConfigCombo.add(name);

                if (name.equals(refName)) {
selectionIndex = i;
}
i++;
}

mDeviceConfigCombo.select(selectionIndex);
            mDeviceConfigCombo.setEnabled(configNames.size() > 1);
}
}

//Synthetic comment -- @@ -1588,7 +1580,7 @@
// get the device config from the device/config combos.
int configIndex = mDeviceConfigCombo.getSelectionIndex();
String name = mDeviceConfigCombo.getItem(configIndex);
            FolderConfiguration config = mState.device.getConfigs().get(name);

// replace the config with the one from the device
mCurrentConfig.set(config);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java
//Synthetic comment -- index dd0fb74..db5e4d8 100644

//Synthetic comment -- @@ -33,10 +33,9 @@
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
* Class representing a layout device.
//Synthetic comment -- @@ -58,10 +57,29 @@

private final String mName;

    /** editable map of the config */
    private Map<String, FolderConfiguration> mEditMap = new HashMap<String, FolderConfiguration>();
    /** unmodifiable map returned by {@link #getConfigs()}. */
    private Map<String, FolderConfiguration> mMap;
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

//Synthetic comment -- @@ -93,8 +111,10 @@
}

// then save all the configs.
        for (Entry<String, FolderConfiguration> entry : mEditMap.entrySet()) {
            saveConfigTo(doc, deviceNode, entry.getKey(), entry.getValue());
}
}

//Synthetic comment -- @@ -208,32 +228,68 @@
}

void addConfig(String name, FolderConfiguration config) {
        mEditMap.put(name, config);
        _seal();
}

    void addConfigs(Map<String, FolderConfiguration> configs) {
        mEditMap.putAll(configs);
        _seal();
}

void removeConfig(String name) {
        mEditMap.remove(name);
        _seal();
}

/**
     * Adds config to the LayoutDevice. This is to be used to add plenty of configurations.
     * It must be followed by {@link #_seal()}.
* @param name the name of the config
* @param config the config.
*/
void _addConfig(String name, FolderConfiguration config) {
        mEditMap.put(name, config);
}

void _seal() {
        mMap = Collections.unmodifiableMap(mEditMap);
}

void setXDpi(float xdpi) {
//Synthetic comment -- @@ -248,8 +304,34 @@
return mName;
}

    public Map<String, FolderConfiguration> getConfigs() {
        return mMap;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceManager.java
//Synthetic comment -- index 3b19f17..60c8d95 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -37,7 +38,6 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -180,7 +180,7 @@
newDevice.setYDpi(newYDpi);

// and get the Folderconfiguration
        Map<String, FolderConfiguration> configs = device.getConfigs();
newDevice.addConfigs(configs);

// replace the old device with the new
//Synthetic comment -- @@ -191,7 +191,6 @@
return newDevice;
}


/**
* Adds or replaces a configuration in a given {@link LayoutDevice}.
* @param device the device to modify







