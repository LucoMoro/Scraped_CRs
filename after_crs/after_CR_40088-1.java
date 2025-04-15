/*Add listener interface for changes in device lists

Allows things like the GLE to be notified when a device is added or
removed via the AVD Manager so they can update their menus
accordingly.

Change-Id:I616234e7dba3151712cf0d6df600a9104da4f806*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 0237769..0994c89 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -1906,6 +1907,14 @@
menu.setVisible(true);
}
};

        // FIXME: Create a custom listener so that we can just update the device list, rather
        // than removing the old listener and creating a new one in it's place that will most
        // likely be nearly the same.
        Listener[] listeners = combo.getListeners(SWT.Selection);
        for (Listener listener : listeners) {
            combo.removeListener(SWT.Selection, listener);
        }
combo.addListener(SWT.Selection, menuListener);
}

//Synthetic comment -- @@ -2656,9 +2665,17 @@
* Loads the list of {@link Device}s and inits the UI with it.
*/
private void initDevices() {
        final Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
mDeviceList = sdk.getDevices();
            DeviceManager manager = sdk.getDeviceManager();
            manager.registerListener(new DeviceManager.DevicesChangeListener() {
                @Override
                public void onDevicesChange() {
                    mDeviceList = sdk.getDevices();
                    addDeviceMenuListener(mDeviceCombo);
                }
            });
} else {
mDeviceList = new ArrayList<Device>();
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 4bb4344..cb7b90f 100644

//Synthetic comment -- @@ -68,6 +68,8 @@
private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object lock = new Object();
    private static final List<DevicesChangeListener> listeners =
        Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
//Synthetic comment -- @@ -79,6 +81,38 @@
}

/**
     * Interface implemented by objects which want to know when changes occur to the {@link Device}
     * lists.
     */
    public static interface DevicesChangeListener {
        /**
         * Called after one of the {@link Device} lists has been updated.
         */
        public void onDevicesChange();
    }

    /**
     * Register a listener to be notified when the device lists are modified.
     * @param listener The listener to add
     */
    public void registerListener(DevicesChangeListener listener) {
        synchronized(listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from the notification list such that it will no longer receive
     * notifications when modifications to the {@link Device} list occur.
     * @param listener The listener to remove.
     */
    public void unregisterListener(DevicesChangeListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }

    /**
* Returns both vendor provided and user created {@link Device}s.
* 
* @param sdkLocation Location of the Android SDK
//Synthetic comment -- @@ -111,6 +145,7 @@
mLog.error(null, "Error reading default devices");
mDefaultDevices = new ArrayList<Device>();
}
                notifyListeners();
}
}
return Collections.unmodifiableList(mDefaultDevices);
//Synthetic comment -- @@ -125,6 +160,7 @@
public List<Device> getVendorDevices(String sdkLocation) {
synchronized (lock) {
if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
                mVendorDevicesLocation = sdkLocation;
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
//Synthetic comment -- @@ -135,6 +171,7 @@
}
}
mVendorDevices = devices;
                notifyListeners();
}
}
return Collections.unmodifiableList(mVendorDevices);
//Synthetic comment -- @@ -156,6 +193,7 @@
userDevicesFile = new File(AndroidLocation.getFolder(),
SdkConstants.FN_DEVICES_XML);
mUserDevices.addAll(DeviceParser.parse(userDevicesFile));
                    notifyListeners();
} catch (AndroidLocationException e) {
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
} catch (SAXException e) {
//Synthetic comment -- @@ -186,6 +224,7 @@
}
mUserDevices.add(d);
}
        notifyListeners();
}

public void removeUserDevice(Device d) {
//Synthetic comment -- @@ -199,6 +238,7 @@
if (userDevice.getName().equals(d.getName())
&& userDevice.getManufacturer().equals(d.getManufacturer())) {
it.remove();
                    notifyListeners();
break;
}

//Synthetic comment -- @@ -333,6 +373,14 @@
return new ArrayList<Device>();
}

    private void notifyListeners() {
        synchronized(listeners) {
            for (DevicesChangeListener listener : listeners) {
                listener.onDevicesChange();
            }
        }
    }

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
List<File> extraDirs = new ArrayList<File>();







