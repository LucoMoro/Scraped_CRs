//<Beginning of snippet n. 0>
/**
 * A Device. It can be a physical device or an emulator.
 */
final class Device implements IDevice {

    private final static int INSTALL_TIMEOUT = 2 * 60 * 1000; //2min
    private static final int BATTERY_TIMEOUT = 2 * 1000; //2 seconds
    private DeviceMonitor mMonitor;

    private static final String LOG_TAG = "Device";

    private Integer mLastBatteryLevel = null;
    private long mLastBatteryCheckTime = 0;

    // Implementing the method to return formatted device name
    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder(20);
        String manufacturer = cleanupStringForDisplay(getProperty(DEVICE_MANUFACTURER_PROPERTY));
        String model = cleanupStringForDisplay(getProperty(DEVICE_MODEL_PROPERTY));

        if (isEmulator()) {
            sb.append(getSerialNumber()).append(String.format(" [%s]", getAvdName()));
        } else {
            sb.append(getSerialNumber());
            if (!manufacturer.isEmpty() || !model.isEmpty()) {
                sb.append(" [");
                sb.append(manufacturer);
                if (!manufacturer.isEmpty() && !model.isEmpty()) {
                    sb.append(':');
                }
                sb.append(model);
                sb.append(']');
            }
        }

        return sb.toString();
    }

    private String cleanupStringForDisplay(String s) {
        if (s == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public String getName();

/**
 * Returns the state of the device.
 */
public DeviceState getState();
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
/**
 * labels and images for {@link IDevice} and {@link Client} objects.
 */
private class LabelProvider implements ITableLabelProvider {
    private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == DEVICE_COL_SERIAL && element instanceof IDevice) {
            IDevice device = (IDevice) element;
            switch (columnIndex) {
                case DEVICE_COL_SERIAL:
                    return getDeviceName(device);
                case DEVICE_COL_STATE:
                    return getStateString(device);
                case DEVICE_COL_BUILD:
                    return null;
            }
        }
        return null;
    }

    private String getDeviceName(IDevice device) {
        return device.getName(); // Using the new getName method
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
        // pass
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
@Override
public void addListener(ILabelProviderListener listener) {
    // pass
}

// In the appropriate UI parameter where device names are being set
IDevice device = (IDevice) element;
switch (columnIndex) {
    case 0:
        return device.getName(); // Using the new getName method
    case 1:
        return device.getName(); // Using the new getName method
}
// Implementing constructor
public DeviceChooserDialog(Shell parent, DeviceChooserResponse response, String packageName,
                           IAndroidTarget projectTarget) {
    super(parent);
    mResponse = response;
    mPackageName = packageName;
    mProjectTarget = projectTarget;
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();

for (IDevice device : devices) {
    String name = device.getName(); // Using the new getName method
    if (name.equals(deviceName)) {
        return device;
    }
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
List<String> items = new ArrayList<String>(devices.length);
for (IDevice d : devices) {
    String name = d.getName(); // Using the new getName method
    items.add(name);
}
mDeviceCombo.setItems(items.toArray(new String[0]));

int index = 0;
if (mLastUsedDevice != null) {
    index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {
//<End of snippet n. 5>