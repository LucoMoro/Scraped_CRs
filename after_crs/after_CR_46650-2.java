/*Persist deletion of devices.

devices.xml was not updated when devices were being deleted.

Change-Id:Ie1c7253da1c5f64ee5d5f26b5e36b0fbc6443671*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 3662c26..37b6876 100644

//Synthetic comment -- @@ -321,15 +321,28 @@
* {@link AndroidLocation#getFolder()}.
*/
public void saveUserDevices() {
        if (mUserDevices == null) {
            return;
        }

        File userDevicesFile = null;
        try {
            userDevicesFile = new File(AndroidLocation.getFolder(),
                    SdkConstants.FN_DEVICES_XML);
        } catch (AndroidLocationException e) {
            mLog.warning("Couldn't find user directory: %1$s", e.getMessage());
            return;
        }

        if (mUserDevices.size() == 0) {
            userDevicesFile.delete();
            return;
        }

synchronized (sLock) {
            if (mUserDevices.size() > 0) {
try {
DeviceWriter.writeToXml(new FileOutputStream(userDevicesFile), mUserDevices);
} catch (FileNotFoundException e) {
mLog.warning("Couldn't open file: %1$s", e.getMessage());
} catch (ParserConfigurationException e) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index e5d94fb..6bac1ba 100755

//Synthetic comment -- @@ -713,6 +713,7 @@

if (result.get()) {
mDeviceManager.removeUserDevice(ci.mDevice);
            mDeviceManager.saveUserDevices();
onRefresh();
}
}







