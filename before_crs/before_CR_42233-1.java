/*Inline devices if there is only one manufacturer

Currently the device pulldown shows a single item,
"Generic", which itself contains the various generic
devices. This CL tweaks the display code such that if
there is just a single manufacturer, the items are
inlined in the top level menu.

Change-Id:If150becaa7dfe225f9ec8e861be1d67ede4f30c4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 9287787..9c3aa8e 100644

//Synthetic comment -- @@ -68,8 +68,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.State;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.repository.PkgProps;
//Synthetic comment -- @@ -1885,12 +1885,16 @@
devices.add(device);
}
for (List<Device> devices : manufacturers.values()) {
                        MenuItem item = new MenuItem(menu, SWT.CASCADE);
                        item.setText(devices.get(0).getManufacturer());
                        Menu manufacturerMenu = new Menu(menu);
                        item.setMenu(manufacturerMenu);
for (final Device d : devices) {
                            MenuItem deviceItem = new MenuItem(manufacturerMenu, SWT.CHECK);
deviceItem.setText(d.getName());
deviceItem.setSelection(current == d);

//Synthetic comment -- @@ -1904,7 +1908,6 @@
});
}
}

}

// TODO - how do I dispose of this?







