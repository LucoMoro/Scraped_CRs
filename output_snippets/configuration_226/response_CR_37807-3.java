//<Beginning of snippet n. 0>
/**
 * A Device. It can be a physical device or an emulator.
 */
final class Device implements IDevice {

    private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
    private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
    private DeviceMonitor mMonitor;

    private static final String LOG_TAG = "Device";

    private Integer mLastBatteryLevel = null;
    private long mLastBatteryCheckTime = 0;
    private String mAvdName;

    public Device(String avdName) {
        mAvdName = avdName;
    }

    @Override
    public String getName() {
        String manufacturer = getProperty(DEVICE_MANUFACTURER_PROPERTY);
        manufacturer = cleanupStringForDisplay(manufacturer);

        String model = getProperty(DEVICE_MODEL_PROPERTY);
        model = cleanupStringForDisplay(model);

        String serialNumber = getSerialNumber();

        StringBuilder sb = new StringBuilder();
        if (manufacturer.length() > 0) {
            sb.append(manufacturer);
            sb.append("-");
        }
        if (model.length() > 0) {
            sb.append(model);
            sb.append("-");
        }
        sb.append(serialNumber);

        return sb.toString();
    }
    
    // Other methods...
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public String getAvdName();

/**
 * Returns the state of the device.
 */
public DeviceState getState();

/**
 * Returns the name of the device formatted as manufacturer-model-serial.
 */
public String getName();
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
        if (element instanceof IDevice) {
            IDevice device = (IDevice)element;
            switch (columnIndex) {
                case DEVICE_COL_SERIAL:
                    return getDeviceName(device);
                case DEVICE_COL_STATE:
                    return getStateString(device);
                case DEVICE_COL_BUILD: {
                    return null;
                }
            }
        }
        return null;
    }

    private String getDeviceName(IDevice device) {
        return device.getName();
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

    @Override
    public void addListener(ILabelProviderListener listener) {
        // pass
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
IDevice device = (IDevice)element;
switch (columnIndex) {
    case 0:
        return device.getName();
    case 1:
        if (device.isEmulator()) {
            return device.getAvdName();
        }
// Other cases...
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();

for (IDevice device : devices) {
    String name = device.getName();
    if (name.equals(deviceName)) {
        return device;
    }
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
List<String> items = new ArrayList<String>(devices.length);
for (IDevice d : devices) {
    String name = d.getName();
    items.add(name);
}
mDeviceCombo.setItems(items.toArray(new String[items.size()]));

int index = 0;
if (mLastUsedDevice != null) {
    index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {
// Other logic...
}
//<End of snippet n. 5>