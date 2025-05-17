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

// Listener interface
public interface DeviceListChangeListener {
    void onDeviceAdded(Device device);
    void onDeviceRemoved(Device device);
}

public class DeviceManager {

    private final IConfigListener mListener;
    private final FolderConfiguration mCurrentConfig = new FolderConfiguration();
    private static List<Device> mUserDevices = new ArrayList<>();
    private static List<Device> mDefaultDevices = new ArrayList<>();
    private static final Object lock = new Object();
    private List<DeviceListChangeListener> listeners = new ArrayList<>();

    // Method to add listener
    public void addDeviceListChangeListener(DeviceListChangeListener listener) {
        synchronized (lock) {
            listeners.add(listener);
        }
    }

    // Method to remove listener
    public void removeDeviceListChangeListener(DeviceListChangeListener listener) {
        synchronized (lock) {
            listeners.remove(listener);
        }
    }

    private void notifyDeviceAdded(Device device) {
        synchronized (lock) {
            for (DeviceListChangeListener listener : listeners) {
                listener.onDeviceAdded(device);
            }
        }
    }

    private void notifyDeviceRemoved(Device device) {
        synchronized (lock) {
            for (DeviceListChangeListener listener : listeners) {
                listener.onDeviceRemoved(device);
            }
        }
    }

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


    public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (lock) {
            if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
                List<Device> devices = new ArrayList<Device>();
                File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
                List<File> deviceDirs = getExtraDirs(extrasFolder);
                // Assume logic to populate devices from deviceDirs

                mVendorDevices = devices;
            }
        }
        return Collections.unmodifiableList(mVendorDevices);
    }

    public void addUserDevice(Device d) {
        synchronized (lock) {
            mUserDevices.add(d);
            notifyDeviceAdded(d);
        }
    }

    public void removeUserDevice(Device d) {
        synchronized (lock) {
            Iterator<Device> it = mUserDevices.iterator();
            while (it.hasNext()) {
                Device userDevice = it.next();
                if (userDevice.getName().equals(d.getName())
                        && userDevice.getManufacturer().equals(d.getManufacturer())) {
                    it.remove();
                    notifyDeviceRemoved(d);
                    break;
                }
            }
        }
    }

    private List<File> getExtraDirs(File extrasFolder) {
        List<File> extraDirs = new ArrayList<File>();
        // Assume logic to populate extraDirs
        return extraDirs;
    }

//<End of snippet n. 1>