//<Beginning of snippet n. 0>
public void createControl(Composite parent) {
    Font font = parent.getFont();
    
    // reload the AVDs to make sure we are up to date
    try {
        Sdk.getCurrent().getAvdManager().reloadAvds(NullLogger.getLogger());
    } catch (AndroidLocationException e1) {
        mLog.warning("Failed to reload AVDs: %s", e1.getMessage());
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object sLock = new Object();
private static final List<DevicesChangeListener> listeners =
       Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

public static enum DeviceStatus {
    // Add relevant statuses here
}

public DeviceManager(ILogger log) {
    mLog = log;
}

public void registerListener(DevicesChangeListener listener) {
    listeners.add(listener);
}

public boolean unregisterListener(DevicesChangeListener listener) {
    return listeners.remove(listener);
}

public DeviceStatus getDeviceStatus() {
    // Implementation needed
}

/**
 * Returns all vendor provided {@link Device}s
 *
 * @param sdkLocation Location of the Android SDK
 * @return A list of vendor provided {@link Device}s
 */
public List<Device> getVendorDevices(String sdkLocation) {
    synchronized (sLock) {
        // Implementation required
        return new ArrayList<>(); // Placeholder return statement
    }
}

/**
 * Returns all user created {@link Device}s
 *
 * @return All user created {@link Device}s
 */
public List<Device> getUserDevices() {
    synchronized (sLock) {
        List<Device> userDevices = new ArrayList<>();
        try {
            // Load user devices logic
        } catch (Exception e) {
            mLog.warning("Couldn't load user devices: %s", e.getMessage());
        }
        return userDevices;
    }
}

public void dispose() {
    synchronized (sLock) {
        // Unregister all listeners
        synchronized (listeners) {
            for (DevicesChangeListener listener : new ArrayList<>(listeners)) {
                unregisterListener(listener);
            }
        }
        // Additional resource cleanup logic can go here
    }
}

private void notifyListeners() {
    synchronized (listeners) {
        for (DevicesChangeListener listener : listeners) {
            listener.onDevicesChange();
        }
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import com.android.sdkuilib.repository.ISdkChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * thus composed of the {@link AvdManagerWindowImpl1} (the window shell itself) and this
 * page displays the actually list of AVDs and various action buttons.
 */
public class AvdManagerPage extends Composite implements ISdkChangeListener, DevicesChangeListener {

    private AvdSelector mAvdSelector;
    private DeviceManager mDeviceManager;

    @Override
    public void dispose() {
        if (mUpdaterData != null) {
            mUpdaterData.removeListener(this);
        }
        if (mDeviceManager != null) {
            mDeviceManager.unregisterListener(this);
            mDeviceManager.dispose();
        }
    }
}
//<End of snippet n. 2>