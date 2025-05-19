//<Beginning of snippet n. 0>
public void createControl(Composite parent) {
    Font font = parent.getFont();

    // reload the AVDs to make sure we are up to date
    try {
        Sdk.getCurrent().getAvdManager().reloadAvds(NullLogger.getLogger());
    } catch (AndroidLocationException e1) {
        // this happens if the AVD Manager failed to find the folder in which the AVDs are
        // stored. There isn't much we can do at this point.
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object sLock = new Object();
private static final List<DevicesChangeListener> listeners =
    Collections.synchronizedList(new ArrayList<DevicesChangeListener>());
private ILogger mLog;

public static enum DeviceStatus {
    // Enum values here
}

public DeviceManager(ILogger log) {
    mLog = log;
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
public boolean unregisterListener(DevicesChangeListener listener) {
    return listeners.remove(listener);
}

public DeviceStatus getDeviceStatus() {
    // Method implementation
}

/**
 * Returns all vendor provided {@link Device}s
 * @param sdkLocation Location of the Android SDK
 * @return A list of vendor provided {@link Device}s
 */
public List<Device> getVendorDevices(String sdkLocation) {
    synchronized (sLock) {
        // Method implementation
    }
}

/**
 * Returns all user created {@link Device}s
 * @return All user created {@link Device}s
 */
public List<Device> getUserDevices() {
    synchronized (sLock) {
        try {
            // Load user devices logic here
        } catch (SAXException e) {
            // Logic for handling SAXException
            String base = userDevicesFile.getAbsoluteFile() + ".old";
            File renamedConfig = new File(base);
            int i = 0;
            while (renamedConfig.exists()) {
                renamedConfig = new File(base + "." + (i++));
            }
            mLog.error(null, "Error parsing %1$s, backing up to %2$s",
                    userDevicesFile.getAbsolutePath(), renamedConfig.getAbsolutePath());
            userDevicesFile.renameTo(renamedConfig);
        } catch (FileNotFoundException e) {
            mLog.warning("No user devices found");
        } catch (ParserConfigurationException | IOException e) {
            mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
        }
    }
}

public void dispose() {
    unregisterAllListeners();
    synchronized (sLock) {
        if (mUserDevices != null) {
            mUserDevices.clear();
        }
        if (mDefaultDevices != null) {
            mDefaultDevices.clear();
        }
        mLog = null;
    }
}

private void unregisterAllListeners() {
    synchronized (listeners) {
        for (DevicesChangeListener listener : new ArrayList<>(listeners)) {
            unregisterListener(listener);
        }
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

    public AvdManagerPage(Composite parent, DeviceManager deviceManager) {
        super(parent, SWT.NONE);
        this.mDeviceManager = deviceManager;
        if (mDeviceManager != null) {
            mDeviceManager.registerListener(this);
        }
    }

    @Override
    public void dispose() {
        if (mDeviceManager != null) {
            mDeviceManager.unregisterListener(this);
            mDeviceManager.dispose();
        }
        super.dispose();
    }
}
//<End of snippet n. 2>