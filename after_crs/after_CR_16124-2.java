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
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
//Synthetic comment -- @@ -47,7 +48,7 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import java.util.List;
import java.util.Map.Entry;

/**
//Synthetic comment -- @@ -133,7 +134,7 @@
}
} else if (parentElement instanceof LayoutDevice) {
LayoutDevice device = (LayoutDevice)parentElement;
                return device.getConfigs().toArray();
}

return null;
//Synthetic comment -- @@ -182,8 +183,7 @@

/**
* Label provider for the {@link TreeViewer}.
     * Supported elements are {@link DeviceType}, {@link LayoutDevice}, and {@link DeviceConfig}.
*
*/
private final static class DeviceLabelProvider implements ITableLabelProvider {
//Synthetic comment -- @@ -197,11 +197,11 @@
if (columnIndex == 0) {
return ((LayoutDevice)element).getName();
}
            } else if (element instanceof DeviceConfig) {
if (columnIndex == 0) {
                    return ((DeviceConfig)element).getName();
} else {
                    return ((DeviceConfig)element).getConfig().toString();
}
}
return null;
//Synthetic comment -- @@ -386,18 +386,18 @@
// are we copying the full device?
if (selection.entry == null) {
// get the config from the origin device
                    List<DeviceConfig> configs = selection.device.getConfigs();

// and copy them in the target device
                    for (DeviceConfig config : configs) {
// we need to make a copy of the config object, or it could be modified
// in default/addon by editing the version in the new device.
FolderConfiguration copy = new FolderConfiguration();
                        copy.set(config.getConfig());

// the name can stay the same since we are copying a full device
// and the target device has its own new name.
                        mManager.addUserConfiguration(targetDevice, config.getName(), copy);
}
} else {
// only copy the config. target device is not the same as the selection, don't
//Synthetic comment -- @@ -533,12 +533,12 @@
// this is the easy case. no config to select
path = new Object[] { DeviceType.CUSTOM, device };
} else {
            // this is more complex. we have the configName, but the tree contains DeviceConfig
// Look for the entry.
            DeviceConfig match = null;
            for (DeviceConfig config : device.getConfigs()) {
                if (config.getName().equals(configName)) {
                    match = config;
break;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index c267422..ef426df 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IStyleResourceValue;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -66,13 +67,12 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
* A composite that displays the current configuration displayed in a Graphical Layout Editor.
//Synthetic comment -- @@ -222,7 +222,7 @@
for (LayoutDevice d : mDeviceList) {
if (d.getName().equals(values[0])) {
device = d;
                        FolderConfiguration config = device.getFolderConfigByName(values[1]);
if (config != null) {
configName = values[1];

//Synthetic comment -- @@ -731,9 +731,8 @@
FolderConfiguration testConfig = new FolderConfiguration();

mainloop: for (LayoutDevice device : mDeviceList) {
            for (DeviceConfig config : device.getConfigs()) {
                testConfig.set(config.getConfig());

// look on the locales.
for (int i = 0 ; i < mLocaleList.size() ; i++) {
//Synthetic comment -- @@ -748,14 +747,14 @@
// where the edited file is a best config.
if (anyDeviceMatch == null) {
anyDeviceMatch = device;
                            anyConfigMatchName = config.getName();
anyLocaleIndex = i;
}

if (isCurrentFileBestMatchFor(testConfig)) {
// this is what we want.
bestDeviceMatch = device;
                            bestConfigMatchName = config.getName();
bestLocaleIndex = i;
break mainloop;
}
//Synthetic comment -- @@ -823,7 +822,7 @@
int configIndex = mDeviceConfigCombo.getSelectionIndex();
if (configIndex != -1) {
String configName = mDeviceConfigCombo.getItem(configIndex);
            FolderConfiguration currentConfig = mState.device.getFolderConfigByName(configName);
if (mEditedConfig.isMatchFor(currentConfig)) {
currentConfigIsCompatible = true; // current config is compatible
if (needBestMatch == false || isCurrentFileBestMatchFor(currentConfig)) {
//Synthetic comment -- @@ -840,9 +839,8 @@
// first look in the current device.
String matchName = null;
int localeIndex = -1;
            mainloop: for (DeviceConfig config : mState.device.getConfigs()) {
                testConfig.set(config.getConfig());

// loop on the locales.
for (int i = 0 ; i < mLocaleList.size() ; i++) {
//Synthetic comment -- @@ -854,7 +852,7 @@

if (mEditedConfig.isMatchFor(testConfig) &&
isCurrentFileBestMatchFor(testConfig)) {
                        matchName = config.getName();
localeIndex = i;
break mainloop;
}
//Synthetic comment -- @@ -1303,13 +1301,12 @@
mDeviceCombo.select(0);

if (mDeviceList.size() > 0) {
                List<DeviceConfig> configs = mDeviceList.get(0).getConfigs();
                for (DeviceConfig config : configs) {
                    mDeviceConfigCombo.add(config.getName());
}
mDeviceConfigCombo.select(0);
                if (configs.size() == 1) {
mDeviceConfigCombo.setEnabled(false);
}
}
//Synthetic comment -- @@ -1377,7 +1374,7 @@
if (mState.device != null) {
int index = mDeviceConfigCombo.getSelectionIndex();
if (index != -1) {
                    FolderConfiguration oldConfig = mState.device.getFolderConfigByName(
mDeviceConfigCombo.getItem(index));

LayoutDevice newDevice = mDeviceList.get(deviceIndex);
//Synthetic comment -- @@ -1449,40 +1446,37 @@
* @return the name of the closest config match, or possibly null if no configs are compatible
* (this can only happen if the configs don't have a single qualifier that is the same).
*/
    private String getClosestMatch(FolderConfiguration oldConfig, List<DeviceConfig> configs) {

// create 2 lists as we're going to go through one and put the candidates in the other.
        ArrayList<DeviceConfig> list1 = new ArrayList<DeviceConfig>();
        ArrayList<DeviceConfig> list2 = new ArrayList<DeviceConfig>();

        list1.addAll(configs);

final int count = FolderConfiguration.getQualifierCount();
for (int i = 0 ; i < count ; i++) {
// compute the new candidate list by only taking configs that have
// the same i-th qualifier as the old config
            for (DeviceConfig c : list1) {
ResourceQualifier oldQualifier = oldConfig.getQualifier(i);

                FolderConfiguration folderConfig = c.getConfig();
                ResourceQualifier newQualifier = folderConfig.getQualifier(i);

if (oldQualifier == null) {
if (newQualifier == null) {
                        list2.add(c);
}
} else if (oldQualifier.equals(newQualifier)) {
                    list2.add(c);
}
}

// at any moment if the new candidate list contains only one match, its name
// is returned.
if (list2.size() == 1) {
                return list2.get(0).getName();
}

// if the list is empty, then all the new configs failed. It is considered ok, and
//Synthetic comment -- @@ -1500,7 +1494,7 @@
// (if there are more than one, then there's a duplicate config and it doesn't matter,
// we take the first one).
if (list1.size() > 0) {
            return list1.get(0).getName();
}

return null;
//Synthetic comment -- @@ -1514,22 +1508,20 @@
mDeviceConfigCombo.removeAll();

if (mState.device != null) {
int selectionIndex = 0;
int i = 0;

            for (DeviceConfig config : mState.device.getConfigs()) {
                mDeviceConfigCombo.add(config.getName());

                if (config.getName().equals(refName)) {
selectionIndex = i;
}
i++;
}

mDeviceConfigCombo.select(selectionIndex);
            mDeviceConfigCombo.setEnabled(mState.device.getConfigs().size() > 1);
}
}

//Synthetic comment -- @@ -1588,7 +1580,7 @@
// get the device config from the device/config combos.
int configIndex = mDeviceConfigCombo.getSelectionIndex();
String name = mDeviceConfigCombo.getItem(configIndex);
            FolderConfiguration config = mState.device.getFolderConfigByName(name);

// replace the config with the one from the device
mCurrentConfig.set(config);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDevice.java
//Synthetic comment -- index dd0fb74..529a454 100644

//Synthetic comment -- @@ -33,10 +33,9 @@
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Class representing a layout device.
//Synthetic comment -- @@ -58,10 +57,35 @@

private final String mName;

    /**
     * Wrapper around a {@link FolderConfiguration}.
     * <p/>This adds a name, accessible through {@link #getName()}.
     * <p/>The folder config can be accessed through {@link #getConfig()}.
     *
     */
    public final static class DeviceConfig {
        private final String mName;
        private final FolderConfiguration mConfig;

        DeviceConfig(String name, FolderConfiguration config) {
            mName = name;
            mConfig = config;
        }

        public String getName() {
            return mName;
        }

        public FolderConfiguration getConfig() {
            return mConfig;
        }
    }

    /** editable list of the config */
    private final ArrayList<DeviceConfig> mConfigs = new ArrayList<DeviceConfig>();
    /** Read-only list */
    private List<DeviceConfig> mROList;

private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

//Synthetic comment -- @@ -93,8 +117,10 @@
}

// then save all the configs.
        synchronized (mConfigs) {
            for (DeviceConfig config : mConfigs) {
                saveConfigTo(doc, deviceNode, config.getName(), config.getConfig());
            }
}
}

//Synthetic comment -- @@ -207,33 +233,90 @@
}
}

    /**
     * Adds config to the LayoutDevice.
     * <p/>This ensures that no two configurations have the same. If a config already exists
     * with the same name, the new config replaces it.
     *
     * @param name the name of the config.
     * @param config the config.
     */
void addConfig(String name, FolderConfiguration config) {
        synchronized (mConfigs) {
            _addConfig(name, config);
            _seal();
        }
}

/**
     * Adds a list of config to the LayoutDevice
     * <p/>This ensures that no two configurations have the same. If a config already exists
     * with the same name, the new config replaces it.

     * @param configs the configs to add.
     */
    void addConfigs(List<DeviceConfig> configs) {
        synchronized (mConfigs) {
            // add the configs manually one by one, to check for no duplicate.
            for (DeviceConfig config : configs) {
                String name = config.getName();

                for (DeviceConfig c : mConfigs) {
                    if (c.getName().equals(name)) {
                        mConfigs.remove(c);
                        break;
                    }
                }

                mConfigs.add(config);
            }

            _seal();
        }
    }

    /**
     * Removes a config by its name.
     * @param name the name of the config to remove.
     */
    void removeConfig(String name) {
        synchronized (mConfigs) {
            for (DeviceConfig config : mConfigs) {
                if (config.getName().equals(name)) {
                    mConfigs.remove(config);
                    _seal();
                    return;
                }
            }
        }
    }

    /**
     * Adds config to the LayoutDevice. This is to be used to add plenty of
     * configurations. It must be followed by {@link #_seal()}.
     * <p/>This ensures that no two configurations have the same. If a config already exists
     * with the same name, the new config replaces it.
     *
* @param name the name of the config
* @param config the config.
*/
void _addConfig(String name, FolderConfiguration config) {
        synchronized (mConfigs) {
            // remove config that would have the same name to ensure no duplicate
            for (DeviceConfig c : mConfigs) {
                if (c.getName().equals(name)) {
                    mConfigs.remove(c);
                    break;
                }
            }
            mConfigs.add(new DeviceConfig(name, config));
        }
}

void _seal() {
        synchronized (mConfigs) {
            mROList = Collections.unmodifiableList(mConfigs);
        }
}

void setXDpi(float xdpi) {
//Synthetic comment -- @@ -248,8 +331,43 @@
return mName;
}

    /**
     * Returns an unmodifiable list of all the {@link DeviceConfig}.
     */
    public List<DeviceConfig> getConfigs() {
        synchronized (mConfigs) {
            return mROList;
        }
    }

    /**
     * Returns a {@link DeviceConfig} by its name.
     */
    public DeviceConfig getDeviceConfigByName(String name) {
        synchronized (mConfigs) {
            for (DeviceConfig config : mConfigs) {
                if (config.getName().equals(name)) {
                    return config;
                }
            }
        }

        return null;
    }

    /**
     * Returns a {@link FolderConfiguration} by its name.
     */
    public FolderConfiguration getFolderConfigByName(String name) {
        synchronized (mConfigs) {
            for (DeviceConfig config : mConfigs) {
                if (config.getName().equals(name)) {
                    return config.getConfig();
                }
            }
        }

        return null;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutDeviceManager.java
//Synthetic comment -- index 3b19f17..60c8d95 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -37,7 +38,6 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -180,7 +180,7 @@
newDevice.setYDpi(newYDpi);

// and get the Folderconfiguration
        List<DeviceConfig> configs = device.getConfigs();
newDevice.addConfigs(configs);

// replace the old device with the new
//Synthetic comment -- @@ -191,7 +191,6 @@
return newDevice;
}

/**
* Adds or replaces a configuration in a given {@link LayoutDevice}.
* @param device the device to modify







