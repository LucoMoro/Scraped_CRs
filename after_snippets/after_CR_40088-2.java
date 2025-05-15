
//<Beginning of snippet n. 0>


import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
/** The config listener given to the constructor. Never null. */
private final IConfigListener mListener;

    /** The device menu listener, so we can remove it when the device lists are updated */
    private Listener mDeviceListener;

/** The {@link FolderConfiguration} representing the state of the UI controls */
private final FolderConfiguration mCurrentConfig = new FolderConfiguration();

menu.setVisible(true);
}
};

        for (Listener listener : combo.getListeners(SWT.Selection)) {
            if (listener.equals(mDeviceListener)) {
                combo.removeListener(SWT.Selection, listener);
                break;
            }
        }
        mDeviceListener = menuListener;
combo.addListener(SWT.Selection, menuListener);
}

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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object lock = new Object();
    private static final List<DevicesChangeListener> listeners =
        Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
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
        listeners.add(listener);
    }

    /**
     * Removes a listener from the notification list such that it will no longer receive
     * notifications when modifications to the {@link Device} list occur.
     * @param listener The listener to remove.
     */
    public void unregisterListener(DevicesChangeListener listener) {
        listeners.remove(listener);
    }

    /**
* Returns both vendor provided and user created {@link Device}s.
* 
* @param sdkLocation Location of the Android SDK
mLog.error(null, "Error reading default devices");
mDefaultDevices = new ArrayList<Device>();
}
                notifyListeners();
}
}
return Collections.unmodifiableList(mDefaultDevices);
public List<Device> getVendorDevices(String sdkLocation) {
synchronized (lock) {
if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
                mVendorDevicesLocation = sdkLocation;
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
}
}
mVendorDevices = devices;
                notifyListeners();
}
}
return Collections.unmodifiableList(mVendorDevices);
userDevicesFile = new File(AndroidLocation.getFolder(),
SdkConstants.FN_DEVICES_XML);
mUserDevices.addAll(DeviceParser.parse(userDevicesFile));
                    notifyListeners();
} catch (AndroidLocationException e) {
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
} catch (SAXException e) {
}
mUserDevices.add(d);
}
        notifyListeners();
}

public void removeUserDevice(Device d) {
if (userDevice.getName().equals(d.getName())
&& userDevice.getManufacturer().equals(d.getManufacturer())) {
it.remove();
                    notifyListeners();
break;
}

return new ArrayList<Device>();
}

    private void notifyListeners() {
        synchronized (listeners) {
            for (DevicesChangeListener listener : listeners) {
                listener.onDevicesChange();
            }
        }
    }

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
List<File> extraDirs = new ArrayList<File>();

//<End of snippet n. 1>








