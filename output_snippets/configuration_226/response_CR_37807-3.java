//<Beginning of snippet n. 0>
/**
 * A Device. It can be a physical device or an emulator.
 */
final class Device implements IDevice {

    private final static int INSTALL_TIMEOUT = 2 * 60 * 1000; // 2min
    private static final int BATTERY_TIMEOUT = 2 * 1000; // 2 seconds
    private DeviceMonitor mMonitor;

    private static final String LOG_TAG = "Device";

    /**
     * Socket for the connection monitoring client connection/disconnection.
     */
    private Integer mLastBatteryLevel = null;
    private long mLastBatteryCheckTime = 0;

    private String mAvdName;

    public Device(String avdName) {
        mAvdName = avdName;
    }
  
    // Other methods not shown...
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public String getAvdName();

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
                case DEVICE_COL_BUILD: {
                    return null;
                }
            }
        }
        return null; // Ensure all code paths return a value
    }

    private String getDeviceName(IDevice device) {
        String manufacturer = device.getManufacturer();
        String model = device.getModel();
        String serialNumber = device.getSerialNumber();
        
        String name;
        if (manufacturer != null && model != null) {
            name = String.format("%s-%s", manufacturer, model);
        } else if (manufacturer != null) {
            name = manufacturer;
        } else if (model != null) {
            name = model;
        } else {
            name = serialNumber;
        }

        if (device.isEmulator()) {
            name += String.format(" [%s]", device.getAvdName());
        }
        return name;
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
        // pass
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
@Override
public Image getColumnImage(Object element, int columnIndex) {
    IDevice device = (IDevice) element;
    switch (columnIndex) {
        case 0:
            return getDeviceName(device);
        case 1:
            if (device.isEmulator()) {
                return device.getAvdName();
            }
    }
    return null; // Ensure all code paths return a value
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();

for (IDevice device : devices) {
    String name = device.getName() != null ? device.getName() : device.getSerialNumber();
    if (name.equals(deviceName)) {
        return device;
    }
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
List<String> items = new ArrayList<String>(devices.length);
for (IDevice d : devices) {
    String name = d.getName() != null ? d.getName() : d.getSerialNumber();
    items.add(name);
}
mDeviceCombo.setItems(items.toArray(new String[0]));

int index = 0;
if (mLastUsedDevice != null) {
    index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {
    mDeviceCombo.select(index);
}
//<End of snippet n. 5>