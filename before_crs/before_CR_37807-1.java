/*Provide human readable name for all devices.

WIP

Change-Id:I80ef7b48a4c7c8587042c1227c932251e18dba2e*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index a60742d..e406d00 100644

//Synthetic comment -- @@ -34,6 +34,8 @@
* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {

private final static int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
//Synthetic comment -- @@ -59,6 +61,7 @@
private DeviceMonitor mMonitor;

private static final String LOG_TAG = "Device";

/**
* Socket for the connection monitoring client connection/disconnection.
//Synthetic comment -- @@ -70,6 +73,8 @@
private Integer mLastBatteryLevel = null;
private long mLastBatteryCheckTime = 0;

/**
* Output receiver for "pm install package.apk" command line.
*/
//Synthetic comment -- @@ -187,6 +192,64 @@
mAvdName = avdName;
}

/*
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#getState()








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index 41637e2..d81aea9 100644

//Synthetic comment -- @@ -115,6 +115,12 @@
public String getAvdName();

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
        private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
        private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

@Override
public Image getColumnImage(Object element, int columnIndex) {
if (columnIndex == DEVICE_COL_SERIAL && element instanceof IDevice) {
//Synthetic comment -- @@ -208,7 +205,7 @@
IDevice device = (IDevice)element;
switch (columnIndex) {
case DEVICE_COL_SERIAL:
                        return getDeviceName(device);
case DEVICE_COL_STATE:
return getStateString(device);
case DEVICE_COL_BUILD: {
//Synthetic comment -- @@ -271,54 +268,6 @@
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







