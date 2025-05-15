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

/** Listener interface for device changes */
public interface DeviceChangeListener {
    void onDeviceAdded(Device device);
    void onDeviceRemoved(Device device);
}

/** List of registered listeners */
private final List<DeviceChangeListener> listeners = new ArrayList<>();

/** Synchronization lock */
private static final Object listenerLock = new Object();

menu.setVisible(true);
}
};
combo.addListener(SWT.Selection, menuListener);
}

/* Loads the list of {@link Device}s and inits the UI with it. */
private void initDevices() {
    Sdk sdk = Sdk.getCurrent();
    if (sdk != null) {
        mDeviceList = sdk.getDevices();
    } else {
        mDeviceList = new ArrayList<Device>();
    }
//<End of snippet n. 0>




```java
//<Beginning of snippet n. 1>

private static List<Device> mUserDevices = new ArrayList<>();
private static List<Device> mDefaultDevices;
private static final Object lock = new Object();

/** Returns both vendor provided and user created {@link Device}s. */
public List<Device> getUserDevices() {
    synchronized (lock) {
        return Collections.unmodifiableList(mUserDevices);
    }
}

public void addUserDevice(Device d) {
    synchronized (lock) {
        mUserDevices.add(d);
        notifyDeviceAdded(d);
    }
}

public void removeUserDevice(Device d) {
    synchronized (lock) {
        for (Iterator<Device> it = mUserDevices.iterator(); it.hasNext();) {
            Device userDevice = it.next();
            if (userDevice.getName().equals(d.getName()) &&
                userDevice.getManufacturer().equals(d.getManufacturer())) {
                it.remove();
                notifyDeviceRemoved(d);
                break;
            }
        }
    }
}

/** Register a listener for device changes */
public void addListener(DeviceChangeListener listener) {
    synchronized (listenerLock) {
        listeners.add(listener);
    }
}

/** Unregister a listener for device changes */
public void removeListener(DeviceChangeListener listener) {
    synchronized (listenerLock) {
        listeners.remove(listener);
    }
}

/** Notify all listeners of device addition */
private void notifyDeviceAdded(Device device) {
    synchronized (listenerLock) {
        for (DeviceChangeListener listener : listeners) {
            listener.onDeviceAdded(device);
        }
    }
}

/** Notify all listeners of device removal */
private void notifyDeviceRemoved(Device device) {
    synchronized (listenerLock) {
        for (DeviceChangeListener listener : listeners) {
            listener.onDeviceRemoved(device);
        }
    }
}

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
    List<File> extraDirs = new ArrayList<File>();
//<End of snippet n. 1>
