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

public void registerDeviceChangeListener(DeviceChangeListener listener) {
    deviceChangeListeners.add(listener);
}

public void unregisterDeviceChangeListener(DeviceChangeListener listener) {
    deviceChangeListeners.remove(listener);
}

private void notifyDeviceAdded(Device device) {
    for (DeviceChangeListener listener : deviceChangeListeners) {
        listener.onDeviceAdded(device);
    }
}

private void notifyDeviceRemoved(Device device) {
    for (DeviceChangeListener listener : deviceChangeListeners) {
        listener.onDeviceRemoved(device);
    }
}

private void initDevices() {
    Sdk sdk = Sdk.getCurrent();
    if (sdk != null) {
        mDeviceList = sdk.getDevices();
        for (Device device : mDeviceList) {
            notifyDeviceAdded(device);
        }
    } else {
        mDeviceList = new ArrayList<Device>();
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

private static List<Device> mUserDevices = new ArrayList<>();
private static List<Device> mDefaultDevices = new ArrayList<>();
private static final Object lock = new Object();

public List<Device> getUserDevices() {
    return Collections.unmodifiableList(mUserDevices);
}

public List<Device> getVendorDevices(String sdkLocation) {
    synchronized (lock) {
        if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
            List<Device> devices = new ArrayList<Device>();
            File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
            List<File> deviceDirs = getExtraDirs(extrasFolder);
            // Additional code to load devices from the directories
            mVendorDevices = devices;
        }
    }
    return Collections.unmodifiableList(mVendorDevices);
}

public void addUserDevice(Device d) {
    mUserDevices.add(d);
    notifyDeviceAdded(d);
}

public void removeUserDevice(Device d) {
    Iterator<Device> it = mUserDevices.iterator();
    while (it.hasNext()) {
        Device userDevice = it.next();
        if (userDevice.getName().equals(d.getName()) && userDevice.getManufacturer().equals(d.getManufacturer())) {
            it.remove();
            notifyDeviceRemoved(userDevice);
            break;
        }
    }
}

private List<File> getExtraDirs(File extrasFolder) {
    List<File> extraDirs = new ArrayList<File>();
    // Logic to populate extraDirs
    return extraDirs;
}

//<End of snippet n. 1>