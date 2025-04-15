/*Add listener interface for changes in device lists

Allows things like the GLE to be notified when a device is added or
removed via the AVD Manager so they can update their menus
accordingly.

Change-Id:I616234e7dba3151712cf0d6df600a9104da4f806*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 0237769..95dac9b 100644

//Synthetic comment -- @@ -67,6 +67,7 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -204,6 +205,9 @@
/** The config listener given to the constructor. Never null. */
private final IConfigListener mListener;

/** The {@link FolderConfiguration} representing the state of the UI controls */
private final FolderConfiguration mCurrentConfig = new FolderConfiguration();

//Synthetic comment -- @@ -1906,6 +1910,14 @@
menu.setVisible(true);
}
};
combo.addListener(SWT.Selection, menuListener);
}

//Synthetic comment -- @@ -2656,9 +2668,17 @@
* Loads the list of {@link Device}s and inits the UI with it.
*/
private void initDevices() {
        Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
mDeviceList = sdk.getDevices();
} else {
mDeviceList = new ArrayList<Device>();
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 4bb4344..238a176 100644

//Synthetic comment -- @@ -68,6 +68,8 @@
private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object lock = new Object();

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
//Synthetic comment -- @@ -79,6 +81,34 @@
}

/**
* Returns both vendor provided and user created {@link Device}s.
* 
* @param sdkLocation Location of the Android SDK
//Synthetic comment -- @@ -111,6 +141,7 @@
mLog.error(null, "Error reading default devices");
mDefaultDevices = new ArrayList<Device>();
}
}
}
return Collections.unmodifiableList(mDefaultDevices);
//Synthetic comment -- @@ -125,6 +156,7 @@
public List<Device> getVendorDevices(String sdkLocation) {
synchronized (lock) {
if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
//Synthetic comment -- @@ -135,6 +167,7 @@
}
}
mVendorDevices = devices;
}
}
return Collections.unmodifiableList(mVendorDevices);
//Synthetic comment -- @@ -156,6 +189,7 @@
userDevicesFile = new File(AndroidLocation.getFolder(),
SdkConstants.FN_DEVICES_XML);
mUserDevices.addAll(DeviceParser.parse(userDevicesFile));
} catch (AndroidLocationException e) {
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
} catch (SAXException e) {
//Synthetic comment -- @@ -186,6 +220,7 @@
}
mUserDevices.add(d);
}
}

public void removeUserDevice(Device d) {
//Synthetic comment -- @@ -199,6 +234,7 @@
if (userDevice.getName().equals(d.getName())
&& userDevice.getManufacturer().equals(d.getManufacturer())) {
it.remove();
break;
}

//Synthetic comment -- @@ -333,6 +369,14 @@
return new ArrayList<Device>();
}

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
List<File> extraDirs = new ArrayList<File>();







