/*Properly dispose DeviceManager from AvdManager.

Similar issue fixed by patch d1daeae66a0ebdf53076038dc4a1fe099c4141fb
but this time for the AvdManager.

Change-Id:I10af39d364e427d593078f8bfa23bba3ba7e1558*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 27feb00..9bf62c0 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
private static final Object sLock = new Object();
    private static final List<DevicesChangeListener> listeners =
Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

public static enum DeviceStatus {
//Synthetic comment -- @@ -87,10 +87,8 @@
}

// TODO: Refactor this to look more like AvdManager so that we don't have
    // multiple instances
    // in the same application, which forces us to parse the XML multiple times
    // when we don't
    // to.
public DeviceManager(ILogger log) {
mLog = log;
}
//Synthetic comment -- @@ -108,19 +106,23 @@

/**
* Register a listener to be notified when the device lists are modified.
     * @param listener The listener to add
*/
public void registerListener(DevicesChangeListener listener) {
        listeners.add(listener);
}

/**
* Removes a listener from the notification list such that it will no longer receive
* notifications when modifications to the {@link Device} list occur.
* @param listener The listener to remove.
*/
public boolean unregisterListener(DevicesChangeListener listener) {
        return listeners.remove(listener);
}

public DeviceStatus getDeviceStatus(
//Synthetic comment -- @@ -189,10 +191,10 @@
}

/**
     * Returns all vendor provided {@link Device}s
*
* @param sdkLocation Location of the Android SDK
     * @return A list of vendor provided {@link Device}s
*/
public List<Device> getVendorDevices(String sdkLocation) {
synchronized (sLock) {
//Synthetic comment -- @@ -224,9 +226,9 @@
}

/**
     * Returns all user created {@link Device}s
*
     * @return All user created {@link Device}s
*/
public List<Device> getUserDevices() {
synchronized (sLock) {
//Synthetic comment -- @@ -244,22 +246,25 @@
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
} catch (SAXException e) {
// Probably an old config file which we don't want to overwrite.
                    // FIXME: userDevicesFile is likely null at this point and below.
                    String base = userDevicesFile.getAbsoluteFile()+".old";
                    File renamedConfig = new File(base);
                    int i = 0;
                    while (renamedConfig.exists()) {
                        renamedConfig = new File(base + "." + (i++));
}
                    mLog.error(null, "Error parsing %1$s, backing up to %2$s",
                            userDevicesFile.getAbsolutePath(), renamedConfig.getAbsolutePath());
                    userDevicesFile.renameTo(renamedConfig);
} catch (FileNotFoundException e) {
mLog.warning("No user devices found");
} catch (ParserConfigurationException e) {
                    mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
} catch (IOException e) {
                    mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
}
}
}
//Synthetic comment -- @@ -421,8 +426,8 @@
}

private void notifyListeners() {
        synchronized (listeners) {
            for (DevicesChangeListener listener : listeners) {
listener.onDevicesChange();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index fc8e2bf..19a4765 100755

//Synthetic comment -- @@ -26,6 +26,8 @@
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -40,7 +42,8 @@
* thus composed of the {@link AvdManagerWindowImpl1} (the window shell itself) and this
* page displays the actually list of AVDs and various action buttons.
*/
public class AvdManagerPage extends UpdaterPage implements ISdkChangeListener, DevicesChangeListener {

private AvdSelector mAvdSelector;

//Synthetic comment -- @@ -94,6 +97,11 @@
}

@Override
public void dispose() {
mUpdaterData.removeListener(this);
mDeviceManager.unregisterListener(this);







