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

public class MyAvdManager extends AvdManager implements DeviceChangeListener {
    private final IConfigListener mListener;
    private final FolderConfiguration mCurrentConfig = new FolderConfiguration();
    private List<DeviceChangeListener> listeners = new ArrayList<>();
    
    public MyAvdManager(IConfigListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDeviceAdded(Device device) {
        notifyDeviceAdded(device);
    }

    @Override
    public void onDeviceRemoved(Device device) {
        notifyDeviceRemoved(device);
    }

    public void registerListener(DeviceChangeListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(DeviceChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyDeviceAdded(Device device) {
        for (DeviceChangeListener listener : listeners) {
            listener.onDeviceAdded(device);
        }
    }

    private void notifyDeviceRemoved(Device device) {
        for (DeviceChangeListener listener : listeners) {
            listener.onDeviceRemoved(device);
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
private static List<Device> mUserDevices = new ArrayList<>();
private static List<Device> mDefaultDevices = new ArrayList<>();
private static final Object lock = new Object();

public List<Device> getVendorDevices(String sdkLocation) {
    synchronized (lock) {
        if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
            List<Device> devices = new ArrayList<Device>();
            File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
            List<File> deviceDirs = getExtraDirs(extrasFolder);
            // Load devices
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
    // Implementation of extra directories fetching
    return extraDirs;
}
//<End of snippet n. 1>