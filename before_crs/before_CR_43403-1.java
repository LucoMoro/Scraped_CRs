/*Improve the Device menu

First, this changeset improves the device labels in the device menu to
not only list the screensize and screen type (e.g. "FWVGA"), but the
screen resolution and density category as well.

Second, the device menu by default will show one submenu for every
device manufacturer, with the corresponding devices listed within each
of those sub menus. Out of the box the default file only lists Nexus
devices, and this ends up with a bit of a suboptimal menu.

This changeset handles this scenario better by inlining all the
generic devices together in the top menu, and sorting them in reverse
order. It also places the Nexus devices in this menu, ordered by
reverse release date.

Change-Id:I5015430e2dc48306e848ce7691810349415470e0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java
//Synthetic comment -- index 8707c0e..32f8e9d 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.devices.Device;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;

//Synthetic comment -- @@ -33,15 +34,24 @@
import org.eclipse.swt.widgets.ToolItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
* The {@linkplain DeviceMenuListener} class is responsible for generating the device
* menu in the {@link ConfigurationChooser}.
*/
class DeviceMenuListener extends SelectionAdapter {
private final ConfigurationChooser mConfigChooser;
private final Device mDevice;

//Synthetic comment -- @@ -63,34 +73,48 @@
Device current = configuration.getDevice();
Menu menu = new Menu(chooser.getShell(), SWT.POP_UP);

        AvdManager avdManager = Sdk.getCurrent().getAvdManager();
        AvdInfo[] avds = avdManager.getValidAvds();
List<Device> deviceList = chooser.getDeviceList();
        boolean separatorNeeded = false;
        for (AvdInfo avd : avds) {
            for (Device device : deviceList) {
                if (device.getManufacturer().equals(avd.getDeviceManufacturer())
                        && device.getName().equals(avd.getDeviceName())) {
                    separatorNeeded = true;
                    MenuItem item = new MenuItem(menu, SWT.CHECK);
                    item.setText(avd.getName());
                    item.setSelection(current == device);

                    item.addSelectionListener(new DeviceMenuListener(chooser, device));
}
}
}

        if (separatorNeeded) {
            @SuppressWarnings("unused")
            MenuItem separator = new MenuItem(menu, SWT.SEPARATOR);
        }

        // Group the devices by manufacturer, then put them in the menu
if (!deviceList.isEmpty()) {
Map<String, List<Device>> manufacturers = new TreeMap<String, List<Device>>();
for (Device device : deviceList) {
List<Device> devices;
if (manufacturers.containsKey(device.getManufacturer())) {
devices = manufacturers.get(device.getManufacturer());
} else {
//Synthetic comment -- @@ -99,20 +123,62 @@
}
devices.add(device);
}
            for (List<Device> devices : manufacturers.values()) {
                Menu manufacturerMenu = menu;
                if (manufacturers.size() > 1) {
                    MenuItem item = new MenuItem(menu, SWT.CASCADE);
                    item.setText(devices.get(0).getManufacturer());
                    manufacturerMenu = new Menu(menu);
                    item.setMenu(manufacturerMenu);
}
                for (final Device d : devices) {
                    MenuItem deviceItem = new MenuItem(manufacturerMenu, SWT.CHECK);
                    deviceItem.setText(d.getName());
                    deviceItem.setSelection(current == d);

                    deviceItem.addSelectionListener(new DeviceMenuListener(chooser, d));
}
}
}
//Synthetic comment -- @@ -123,4 +189,88 @@
menu.setLocation(location.x, location.y);
menu.setVisible(true);
}
}







