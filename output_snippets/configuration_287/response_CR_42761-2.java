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

public static enum DeviceStatus {
    // ...
}

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances in the same application, which forces us to parse the XML multiple times
// when we don't to.

private ILogger mLog;

public DeviceManager(ILogger log) {
    mLog = log;
}

/**
 * Register a listener to be notified when the device lists are modified.
 *
 * @param listener The listener to add
 */
public void registerListener(DevicesChangeListener listener) {
    listeners.add(listener);
}

/**
 * Removes a listener from the notification list such that it will no longer receive
 * notifications when modifications to the {@link Device} list occur.
 *
 * @param listener The listener to remove.
 */
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
        // Implementation needed
        return null; // Placeholder
    }
}

/**
 * Returns all user created {@link Device}s
 *
 * @return All user created {@link Device}s
 */
public List<Device> getUserDevices() {
    synchronized (sLock) {
        try {
            // Implementation for loading user devices
        } catch (SAXException e) {
            // Probably an old config file which we don't want to overwrite.
            // FIXME: userDevicesFile is likely null at this point and below.
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
        } catch (ParserConfigurationException e) {
            mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
        } catch (IOException e) {
            mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
        }
    }
    return mUserDevices; // Placeholder return
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
 * Thus composed of the {@link AvdManagerWindowImpl1} (the window shell itself) and this
 * page displays the actual list of AVDs and various action buttons.
 */
public class AvdManagerPage extends Composite implements ISdkChangeListener, DevicesChangeListener {

    private AvdSelector mAvdSelector;
    private DeviceManager mDeviceManager;

    @Override
    public void dispose() {
        mUpdaterData.removeListener(this);
        if (mDeviceManager != null) {
            mDeviceManager.unregisterListener(this);
            mDeviceManager.dispose(); // Added disposal of DeviceManager
        }
        super.dispose(); // Ensure proper disposal of the parent class
    }
}

//<End of snippet n. 2>