
//<Beginning of snippet n. 0>


* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {
    private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
private DeviceMonitor mMonitor;

private static final String LOG_TAG = "Device";
    private static final char SEPARATOR = '-';

/**
* Socket for the connection monitoring client connection/disconnection.
private Integer mLastBatteryLevel = null;
private long mLastBatteryCheckTime = 0;

    private String mName;

/**
* Output receiver for "pm install package.apk" command line.
*/
mAvdName = avdName;
}

    @Override
    public String getName() {
        if (mName == null) {
            mName = constructName();
        }

        return mName;
    }

    private String constructName() {
        if (isEmulator()) {
            String avdName = getAvdName();
            if (avdName != null) {
                return String.format("%s [%s]", avdName, getSerialNumber());
            } else {
                return getSerialNumber();
            }
        } else {
            String manufacturer = cleanupStringForDisplay(
                    getProperty(DEVICE_MANUFACTURER_PROPERTY));
            String model = cleanupStringForDisplay(
                    getProperty(DEVICE_MODEL_PROPERTY));

            StringBuilder sb = new StringBuilder(20);

            if (manufacturer != null) {
                sb.append(manufacturer);
                sb.append(SEPARATOR);
            }

            if (model != null) {
                sb.append(model);
                sb.append(SEPARATOR);
            }

            sb.append(getSerialNumber());
            return sb.toString();
        }
    }

    private String cleanupStringForDisplay(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append('_');
            }
        }

        return sb.toString();
    }

/*
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#getState()

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public String getAvdName();

/**
     * Returns a (humanized) name for this device. Typically this is the AVD name for AVD's, and
     * a combination of the manufacturer name, model name & serial number for devices.
     */
    public String getName();

    /**
* Returns the state of the device.
*/
public DeviceState getState();

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


* labels and images for {@link IDevice} and {@link Client} objects.
*/
private class LabelProvider implements ITableLabelProvider {
@Override
public Image getColumnImage(Object element, int columnIndex) {
if (columnIndex == DEVICE_COL_SERIAL && element instanceof IDevice) {
IDevice device = (IDevice)element;
switch (columnIndex) {
case DEVICE_COL_SERIAL:
                        return device.getName();
case DEVICE_COL_STATE:
return getStateString(device);
case DEVICE_COL_BUILD: {
return null;
}

@Override
public void addListener(ILabelProviderListener listener) {
// pass

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


IDevice device = (IDevice)element;
switch (columnIndex) {
case 0:
                        return device.getName();
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
            if (device.getName().equals(deviceName)) {
return device;
}
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>



List<String> items = new ArrayList<String>(devices.length);
for (IDevice d : devices) {
            items.add(d.getName());
}
mDeviceCombo.setItems(items.toArray(new String[items.size()]));

int index = 0;
        if (items.contains(mLastUsedDevice)) {
index = items.indexOf(mLastUsedDevice);
}
if (index >= 0 && index < items.size()) {

//<End of snippet n. 5>








