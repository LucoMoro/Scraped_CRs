
//<Beginning of snippet n. 0>


* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {

private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
private DeviceMonitor mMonitor;

private static final String LOG_TAG = "Device";

/**
* Socket for the connection monitoring client connection/disconnection.
private Integer mLastBatteryLevel = null;
private long mLastBatteryCheckTime = 0;

/**
* Output receiver for "pm install package.apk" command line.
*/
mAvdName = avdName;
}

/*
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#getState()

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public String getAvdName();

/**
* Returns the state of the device.
*/
public DeviceState getState();

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


* labels and images for {@link IDevice} and {@link Client} objects.
*/
private class LabelProvider implements ITableLabelProvider {
        private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
        private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

@Override
public Image getColumnImage(Object element, int columnIndex) {
if (columnIndex == DEVICE_COL_SERIAL && element instanceof IDevice) {
IDevice device = (IDevice)element;
switch (columnIndex) {
case DEVICE_COL_SERIAL:
                        return getDeviceName(device);
case DEVICE_COL_STATE:
return getStateString(device);
case DEVICE_COL_BUILD: {
return null;
}

        private String getDeviceName(IDevice device) {
            StringBuilder sb = new StringBuilder(20);
            sb.append(device.getSerialNumber());

            if (device.isEmulator()) {
                sb.append(String.format(" [%s]", device.getAvdName()));
            } else {
                String manufacturer = device.getProperty(DEVICE_MANUFACTURER_PROPERTY);
                manufacturer = cleanupStringForDisplay(manufacturer);

                String model = device.getProperty(DEVICE_MODEL_PROPERTY);
                model = cleanupStringForDisplay(model);

                boolean hasManufacturer = manufacturer.length() > 0;
                boolean hasModel = model.length() > 0;
                if (hasManufacturer || hasModel) {
                    sb.append(" [");                        //$NON-NLS-1$
                    sb.append(manufacturer);

                    if (hasManufacturer && hasModel) {
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

@Override
public void addListener(ILabelProviderListener listener) {
// pass

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


IDevice device = (IDevice)element;
switch (columnIndex) {
case 0:
                        return device.getSerialNumber();
case 1:
if (device.isEmulator()) {
return device.getAvdName();
public DeviceChooserDialog(Shell parent, DeviceChooserResponse response, String packageName,
IAndroidTarget projectTarget) {
super(parent);
mResponse = response;
mPackageName = packageName;
mProjectTarget = projectTarget;

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();

for (IDevice device : devices) {
            String name = device.getAvdName();
            if (name == null) {
                name = device.getSerialNumber();
            }

            if (name.equals(deviceName)) {
return device;
}
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>



List<String> items = new ArrayList<String>(devices.length);
for (IDevice d : devices) {
            String name = d.getAvdName();
            if (name == null) {
                name = d.getSerialNumber();
            }
            items.add(name);
}
mDeviceCombo.setItems(items.toArray(new String[items.size()]));

int index = 0;
        if (mLastUsedDevice != null) {
index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {

//<End of snippet n. 5>








