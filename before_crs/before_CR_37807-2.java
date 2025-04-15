/*Provide human readable name for all devices.

There are a number of places where we need to display a device
name. Currently, DDMS shows the serial number + manufacturer/model
if available, but in other instances, we just show the serial
number.

This CL provides a getName() method in the IDevice interface so
that all the locations which require a human readable name can
use this.

The name is typically of the form manufacturer-model-serial.
If the manufacturer/model strings are not available, then those
fields are omitted, so it will show just the serial number.

Change-Id:I80ef7b48a4c7c8587042c1227c932251e18dba2e*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index a60742d..eb98380 100644

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
//Synthetic comment -- @@ -187,6 +192,65 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java
//Synthetic comment -- index 22c9470..781deaf 100644

//Synthetic comment -- @@ -167,7 +167,7 @@
IDevice device = (IDevice)element;
switch (columnIndex) {
case 0:
                        return device.getSerialNumber();
case 1:
if (device.isEmulator()) {
return device.getAvdName();
//Synthetic comment -- @@ -261,6 +261,7 @@
public DeviceChooserDialog(Shell parent, DeviceChooserResponse response, String packageName,
IAndroidTarget projectTarget) {
super(parent);
mResponse = response;
mPackageName = packageName;
mProjectTarget = projectTarget;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 8e5e333..1085f3f 100644

//Synthetic comment -- @@ -219,12 +219,7 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/GLTraceOptionsDialog.java
//Synthetic comment -- index 4023061..ef94452 100644

//Synthetic comment -- @@ -234,16 +234,12 @@

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







