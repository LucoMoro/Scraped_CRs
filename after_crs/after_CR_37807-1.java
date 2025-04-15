/*Provide human readable name for all devices.

WIP

Change-Id:I80ef7b48a4c7c8587042c1227c932251e18dba2e*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index a60742d..e406d00 100644

//Synthetic comment -- @@ -34,6 +34,8 @@
* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {
    private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
//Synthetic comment -- @@ -59,6 +61,7 @@
private DeviceMonitor mMonitor;

private static final String LOG_TAG = "Device";
    private static final String SEPARATOR = "-";

/**
* Socket for the connection monitoring client connection/disconnection.
//Synthetic comment -- @@ -70,6 +73,8 @@
private Integer mLastBatteryLevel = null;
private long mLastBatteryCheckTime = 0;

    private String mName;

/**
* Output receiver for "pm install package.apk" command line.
*/
//Synthetic comment -- @@ -187,6 +192,64 @@
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
            }
        }

        return getSerialNumber();
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








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index 41637e2..d81aea9 100644

//Synthetic comment -- @@ -115,6 +115,12 @@
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








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index 68f23b7..27c844d 100644

//Synthetic comment -- @@ -158,9 +158,6 @@
* labels and images for {@link IDevice} and {@link Client} objects.
*/
private class LabelProvider implements ITableLabelProvider {
@Override
public Image getColumnImage(Object element, int columnIndex) {
if (columnIndex == DEVICE_COL_SERIAL && element instanceof IDevice) {
//Synthetic comment -- @@ -208,7 +205,7 @@
IDevice device = (IDevice)element;
switch (columnIndex) {
case DEVICE_COL_SERIAL:
                        return device.getName();
case DEVICE_COL_STATE:
return getStateString(device);
case DEVICE_COL_BUILD: {
//Synthetic comment -- @@ -271,54 +268,6 @@
return null;
}

@Override
public void addListener(ILabelProviderListener listener) {
// pass







