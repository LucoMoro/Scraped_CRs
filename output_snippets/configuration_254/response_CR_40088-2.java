//<Beginning of snippet n. 0>
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** The config listener given to the constructor. Never null. */
private final IConfigListener mListener;

/** The {@link FolderConfiguration} representing the state of the UI controls */
private final FolderConfiguration mCurrentConfig = new FolderConfiguration();

menu.setVisible(true);
}
};
combo.addListener(SWT.Selection, menuListener);
}

/**
 * Listener interface for device changes.
 */
public interface DeviceListener {
    void onDeviceAdded(Device device);
    void onDeviceRemoved(Device device);
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static List<Device> mUserDevices = new ArrayList<>();
private static List<Device> mDefaultDevices = new ArrayList<>();
private static List<Device> mVendorDevices;
private static String mVendorDevicesLocation;
private static final Object lock = new Object();
private List<DeviceListener> deviceListeners = new ArrayList<>();

/**
 * Adds a listener for device changes.
 */
public void addDeviceListener(DeviceListener listener) {
    synchronized (lock) {
        deviceListeners.add(listener);
    }
}

/**
 * Removes a listener for device changes.
 */
public void removeDeviceListener(DeviceListener listener) {
    synchronized (lock) {
        deviceListeners.remove(listener);
    }
}

/**
 * Notifies all listeners about a device addition.
 */
private void notifyDeviceAdded(Device device) {
    synchronized (lock) {
        for (DeviceListener listener : deviceListeners) {
            listener.onDeviceAdded(device);
        }
    }
}

/**
 * Notifies all listeners about a device removal.
 */
private void notifyDeviceRemoved(Device device) {
    synchronized (lock) {
        for (DeviceListener listener : deviceListeners) {
            listener.onDeviceRemoved(device);
        }
    }
}

/**
 * Returns both vendor provided and user created {@link Device}s.
 *
 * @param sdkLocation Location of the Android SDK
 */
public List<Device> getVendorDevices(String sdkLocation) {
    synchronized (lock) {
        if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
            List<Device> devices = new ArrayList<Device>();
            File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
            List<File> deviceDirs = getExtraDirs(extrasFolder);
            // Assume additional logic to populate devices from deviceDirs here
            mVendorDevices = devices;
            mVendorDevicesLocation = sdkLocation;
        }
    }
    return Collections.unmodifiableList(mVendorDevices);
}

public void addUserDevice(Device d) {
    synchronized (lock) {
        try {
            mUserDevices.add(d);
            notifyDeviceAdded(d);
        } catch (Exception e) {
            // Enhanced error handling with structured logging
            Logger.error("Failed to add user device: " + d.getName(), e);
        }
    }
}

public void removeUserDevice(Device d) {
    synchronized (lock) {
        Iterator<Device> it = mUserDevices.iterator();
        boolean deviceExists = false;
        while (it.hasNext()) {
            Device userDevice = it.next();
            if (userDevice.getName().equals(d.getName())
                && userDevice.getManufacturer().equals(d.getManufacturer())) {
                it.remove();
                notifyDeviceRemoved(userDevice);
                deviceExists = true;
                break;
            }
        }
        if (!deviceExists) {
            // Notify listeners about the unsuccessful removal attempt
            Logger.warn("Attempted to remove non-existent user device: " + d.getName());
        }
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

private List<File> getExtraDirs(File extrasFolder) {
    List<File> extraDirs = new ArrayList<File>();
    // Logic to populate extraDirs
    return extraDirs;
}
//<End of snippet n. 1>