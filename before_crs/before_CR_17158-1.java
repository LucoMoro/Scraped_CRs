/*Fix the config manager dialog in the GLE.

Change-Id:I29296b35c6d2a42908b22b878697d137af71a796*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigManagerDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigManagerDialog.java
//Synthetic comment -- index 6bfa9da..6c47150 100644

//Synthetic comment -- @@ -49,7 +49,6 @@
import org.eclipse.swt.widgets.Tree;

import java.util.List;
import java.util.Map.Entry;

/**
* Dialog to view the layout devices with action button to create/edit/delete/copy layout devices
//Synthetic comment -- @@ -85,15 +84,15 @@
*/
private static class DeviceSelection {
public DeviceSelection(DeviceType type, LayoutDevice device,
                Entry<String, FolderConfiguration> entry) {
this.type = type;
this.device = device;
            this.entry = entry;
}

final DeviceType type;
final LayoutDevice device;
        final Entry<String, FolderConfiguration> entry;
}

private final LayoutDeviceManager mManager;
//Synthetic comment -- @@ -290,9 +289,9 @@
dlg.setXDpi(selection.device.getXDpi());
dlg.setYDpi(selection.device.getYDpi());
}
                if (selection.entry != null) {
                    dlg.setConfigName(selection.entry.getKey());
                    dlg.setConfig(selection.entry.getValue());
}

if (dlg.open() == Window.OK) {
//Synthetic comment -- @@ -337,8 +336,8 @@
dlg.setDeviceName(selection.device.getName());
dlg.setXDpi(selection.device.getXDpi());
dlg.setYDpi(selection.device.getYDpi());
                dlg.setConfigName(selection.entry.getKey());
                dlg.setConfig(selection.entry.getValue());

if (dlg.open() == Window.OK) {
String deviceName = dlg.getDeviceName();
//Synthetic comment -- @@ -352,7 +351,7 @@
dlg.getXDpi(), dlg.getYDpi());

// and add/replace the config
                    mManager.replaceUserConfiguration(d, selection.entry.getKey(), configName,
config);

mTreeViewer.refresh();
//Synthetic comment -- @@ -373,7 +372,7 @@
// if so the target device is a new device.
LayoutDevice targetDevice = selection.device;
if (selection.type == DeviceType.DEFAULT || selection.type == DeviceType.ADDON ||
                        selection.entry == null) {
// create a new device
targetDevice = mManager.addUserDevice(
selection.device.getName() + " Copy", // new name
//Synthetic comment -- @@ -384,7 +383,7 @@
String newConfigName = null; // name of the single new config. used for the select.

// are we copying the full device?
                if (selection.entry == null) {
// get the config from the origin device
List<DeviceConfig> configs = selection.device.getConfigs();

//Synthetic comment -- @@ -403,11 +402,11 @@
// only copy the config. target device is not the same as the selection, don't
// change the config name as we already changed the name of the device.
newConfigName = (selection.device != targetDevice) ?
                            selection.entry.getKey() : selection.entry.getKey() + " Copy";

// copy of the config
FolderConfiguration copy = new FolderConfiguration();
                    copy.set(selection.entry.getValue());

// and create the config
mManager.addUserConfiguration(targetDevice, newConfigName, copy);
//Synthetic comment -- @@ -427,8 +426,8 @@
public void widgetSelected(SelectionEvent e) {
DeviceSelection selection = getSelection();

                if (selection.entry != null) {
                    mManager.removeUserConfiguration(selection.device, selection.entry.getKey());
} else if (selection.device != null) {
mManager.removeUserDevice(selection.device);
}
//Synthetic comment -- @@ -437,7 +436,7 @@

// either select the device (if we removed a entry, or the top custom node if
// we removed a device)
                select(selection.entry != null ? selection.device : null, null);
}
});

//Synthetic comment -- @@ -473,17 +472,17 @@

DeviceType type = (DeviceType)pathSelection.getFirstSegment();
LayoutDevice device = null;
        Entry<String, FolderConfiguration> entry = null;
switch (pathSelection.getSegmentCount()) {
case 2: // layout device is selected
device = (LayoutDevice)pathSelection.getLastSegment();
break;
case 3: // config is selected
device = (LayoutDevice)pathSelection.getSegment(1);
                entry = (Entry<String, FolderConfiguration>)pathSelection.getLastSegment();
}

        return new DeviceSelection(type, device, entry);
}

/**
//Synthetic comment -- @@ -508,7 +507,7 @@
break;
case CUSTOM:
mNewButton.setEnabled(true); // always true to create new devices.
                    mEditButton.setEnabled(selection.entry != null); // only edit config for now

boolean enabled = selection.device != null; // need at least selected device
mDeleteButton.setEnabled(enabled);          // for delete and copy buttons







