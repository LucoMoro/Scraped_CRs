//<Beginning of snippet n. 0>
final class Device implements IDevice {

private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
private DeviceMonitor mMonitor;
private Integer mLastBatteryLevel = null;
private long mLastBatteryCheckTime = 0;
private static final String LOG_TAG = "Device";

@Override
public String getName() {
    String manufacturer = getProperty(DEVICE_MANUFACTURER_PROPERTY);
    String model = getProperty(DEVICE_MODEL_PROPERTY);
    String serialNumber = getSerialNumber();
    manufacturer = cleanupStringForDisplay(manufacturer);
    model = cleanupStringForDisplay(model);

    if (manufacturer.isEmpty() && model.isEmpty()) {
        return serialNumber;
    }
    return String.format("%s-%s-%s", manufacturer, model, serialNumber);
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public String getAvdName();
public DeviceState getState();
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
private class LabelProvider implements ITableLabelProvider {
    private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == DEVICE_COL_SERIAL && element instanceof IDevice) {
            return getDeviceName((IDevice) element);
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
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
IDevice device = (IDevice)element;
switch (columnIndex) {
    case 0:
        return device.getName();
    case 1:
        return device.isEmulator() ? device.getAvdName() : null;
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
List<String> items = new ArrayList<>(devices.length);
for (IDevice d : devices) {
    items.add(d.getName());
}
mDeviceCombo.setItems(items.toArray(new String[0]));
int index = 0;
if (mLastUsedDevice != null) {
    index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {
//<End of snippet n. 5>