
//<Beginning of snippet n. 0>


import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
/** The config listener given to the constructor. Never null. */
private final IConfigListener mListener;

/** The {@link FolderConfiguration} representing the state of the UI controls */
private final FolderConfiguration mCurrentConfig = new FolderConfiguration();

menu.setVisible(true);
}
};
combo.addListener(SWT.Selection, menuListener);
}

* Loads the list of {@link Device}s and inits the UI with it.
*/
private void initDevices() {
        Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
mDeviceList = sdk.getDevices();
} else {
mDeviceList = new ArrayList<Device>();
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object lock = new Object();

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
}

/**
* Returns both vendor provided and user created {@link Device}s.
* 
* @param sdkLocation Location of the Android SDK
mLog.error(null, "Error reading default devices");
mDefaultDevices = new ArrayList<Device>();
}
}
}
return Collections.unmodifiableList(mDefaultDevices);
public List<Device> getVendorDevices(String sdkLocation) {
synchronized (lock) {
if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
}
}
mVendorDevices = devices;
}
}
return Collections.unmodifiableList(mVendorDevices);
userDevicesFile = new File(AndroidLocation.getFolder(),
SdkConstants.FN_DEVICES_XML);
mUserDevices.addAll(DeviceParser.parse(userDevicesFile));
} catch (AndroidLocationException e) {
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
} catch (SAXException e) {
}
mUserDevices.add(d);
}
}

public void removeUserDevice(Device d) {
if (userDevice.getName().equals(d.getName())
&& userDevice.getManufacturer().equals(d.getManufacturer())) {
it.remove();
break;
}

return new ArrayList<Device>();
}

/* Returns all of DeviceProfiles in the extras/ folder */
private List<File> getExtraDirs(File extrasFolder) {
List<File> extraDirs = new ArrayList<File>();

//<End of snippet n. 1>








