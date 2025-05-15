//<Beginning of snippet n. 0>

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The config listener given to the constructor. Never null. */
private final IConfigListener mListener;

/** The {@link FolderConfiguration} representing the state of the UI controls */
private final FolderConfiguration mCurrentConfig = new FolderConfiguration();
private static List<IDeviceChangeListener> mListeners = new ArrayList<>();

menu.setVisible(true);
}
};
combo.addListener(SWT.Selection, menuListener);
}

/**
 * Listener interface for device changes.
 */
public interface IDeviceChangeListener {
    void onDeviceAdded(Device device);
    void onDeviceRemoved(Device device);
}

/**
 * Registers a device change listener.
 */
public void addDeviceListener(IDeviceChangeListener listener) {
    synchronized (mListeners) {
        mListeners.add(listener);
    }
}

/**
 * Notifies listeners of a device addition.
 */
private void notifyDeviceAdded(Device device) {
    synchronized (mListeners) {
        for (IDeviceChangeListener listener : mListeners) {
            listener.onDeviceAdded(device);
        }
    }
}

/**
 * Notifies listeners of a device removal.
 */
private void notifyDeviceRemoved(Device device) {
    synchronized (mListeners) {
        for (IDeviceChangeListener listener : mListeners) {
            listener.onDeviceRemoved(device);
        }
    }
}

/**
 * Loads the list of {@link Device}s and inits the UI with it.
 */
private void initDevices() {
    Sdk sdk = Sdk.getCurrent();
    if (sdk != null) {
        mDeviceList = sdk.getDevices();
    } else {
        mDeviceList = new ArrayList<Device>();
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

private static List<Device> mUserDevices = new ArrayList<>();
private static List<Device> mDefaultDevices = new ArrayList<>();
private static final Object lock = new Object();

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
            // ... (code to add devices from deviceDirs)
        }
    }
    return Collections.unmodifiableList(mVendorDevices);
}

public void addUserDevice(Device d) {
    mUserDevices.add(d);
    notifyDeviceAdded(d); // Notify listeners of the device addition
}

/**
 * Removes a user device from the list.
 */
public void removeUserDevice(Device d) {
    for (Iterator<Device> it = mUserDevices.iterator(); it.hasNext(); ) {
        Device userDevice = it.next();
        if (userDevice.getName().equals(d.getName())
                && userDevice.getManufacturer().equals(d.getManufacturer())) {
            it.remove();
            notifyDeviceRemoved(d); // Notify listeners of the device removal
            break;
        }
    }
}

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
    List<File> extraDirs = new ArrayList<File>();
    // ... (code to populate extraDirs)
    return extraDirs;
}

//<End of snippet n. 1>