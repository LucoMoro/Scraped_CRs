//<Beginning of snippet n. 0>
final class Device implements IDevice {

private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
private DeviceMonitor mMonitor;
private static final String LOG_TAG = "Device";
private Integer mLastBatteryLevel = null;
private long mLastBatteryCheckTime = 0;

@Override
public String getName() {
    String manufacturer = getProperty("ro.product.manufacturer");
    String model = getProperty("ro.product.model");
    String serialNumber = getSerialNumber();
    
    if ((manufacturer == null || manufacturer.isEmpty()) && (model == null || model.isEmpty())) {
        return serialNumber;
    }

    StringBuilder nameBuilder = new StringBuilder();
    if (manufacturer != null && !manufacturer.isEmpty()) {
        nameBuilder.append(manufacturer);
    }
    nameBuilder.append("-");
    if (model != null && !model.isEmpty()) {
        nameBuilder.append(model);
    }
    nameBuilder.append("-").append(serialNumber);
    
    return nameBuilder.toString();
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
            IDevice device = (IDevice)element;
            return getDeviceName(device);
        }
        return null;
    }

    private String getDeviceName(IDevice device) {
        return device.getName();
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
    return device.getSerialNumber();
case 1:
    if (device.isEmulator()) {
        return device.getAvdName();
    }
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
mDeviceCombo.setItems(items.toArray(new String[0]));

int index = 0;
if (mLastUsedDevice != null) {
    index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {
//<End of snippet n. 5>