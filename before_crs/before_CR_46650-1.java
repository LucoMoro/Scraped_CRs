/*Persist deletion of devices.

devices.xml was not updated when devices were being deleted.

Change-Id:Ie1c7253da1c5f64ee5d5f26b5e36b0fbc6443671*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 3662c26..976760f 100644

//Synthetic comment -- @@ -322,7 +322,7 @@
*/
public void saveUserDevices() {
synchronized (sLock) {
            if (mUserDevices != null && mUserDevices.size() != 0) {
File userDevicesFile;
try {
userDevicesFile = new File(AndroidLocation.getFolder(),








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index e5d94fb..6bac1ba 100755

//Synthetic comment -- @@ -713,6 +713,7 @@

if (result.get()) {
mDeviceManager.removeUserDevice(ci.mDevice);
onRefresh();
}
}







