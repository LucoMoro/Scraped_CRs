//<Beginning of snippet n. 0>

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;

public interface DeviceChangeListener {
    void onDeviceAdded(Device device);
    void onDeviceRemoved(Device device);
}

private final List<DeviceChangeListener> deviceChangeListeners = new ArrayList<>();

private final IConfigListener mListener;

/** The {@link FolderConfiguration} representing the state of the UI controls */
private final FolderConfiguration mCurrentConfig = new FolderConfiguration();

menu.setVisible(true);
}
};
combo.addListener(SWT.Selection, menuListener);
}

/* Loads the list of {@link Device}s and inits the UI with it. */
private void initDevices() {
    Sdk sdk = Sdk.getCurrent();
    List<Device> oldDeviceList = new ArrayList<>(mDeviceList);
    
    if (sdk != null) {
        mDeviceList = sdk.getDevices();
    } else {
        mDeviceList = new ArrayList<Device>();
    }

    for (Device device : mDeviceList) {
        if (!oldDeviceList.contains(device)) {
            notifyDeviceChangeListeners(device, true);
        }
    }
}

/* Updated method to notify added or removed devices */
private void notifyDeviceChangeListeners(Device device, boolean isAdded) {
    synchronized (lock) {
        for (DeviceChangeListener listener : deviceChangeListeners) {
            if (isAdded) {
                listener.onDeviceAdded(device);
            } else {
                listener.onDeviceRemoved(device);
            }
        }
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

private static List<Device> mUserDevices = new ArrayList<>();
private static List<Device> mDefaultDevices = new ArrayList<>();
private static final Object lock = new Object();

// TODO: Refactor this to look more like AvdManager so that we don't have multiple instances

public void addDeviceChangeListener(DeviceChangeListener listener) {
    synchronized (lock) {
        deviceChangeListeners.add(listener);
    }
}

public void removeDeviceChangeListener(DeviceChangeListener listener) {
    synchronized (lock) {
        deviceChangeListeners.remove(listener);
    }
}

public List<Device> getVendorDevices(String sdkLocation) {
    synchronized (lock) {
        if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
            List<Device> devices = new ArrayList<Device>();
            File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
            List<File> deviceDirs = getExtraDirs(extrasFolder);
        }
    }
    mVendorDevices = devices;
    return Collections.unmodifiableList(mVendorDevices);
}

public void removeUserDevice(Device d) {
    Iterator<Device> it = mUserDevices.iterator();
    while (it.hasNext()) {
        Device userDevice = it.next();
        if (userDevice.getName().equals(d.getName()) && userDevice.getManufacturer().equals(d.getManufacturer())) {
            it.remove();
            notifyDeviceChangeListeners(d, false);
            break;
        }
    }
}

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
    List<File> extraDirs = new ArrayList<File>();
    // Logic to fetch extra directories
    return extraDirs;
}

//<End of snippet n. 1>