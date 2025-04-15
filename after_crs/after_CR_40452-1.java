/*Increment count when backing up bad devices.xml

Fixes a bug where if you had devices.xml.old.0 in ~/.android, then the
DeviceManager would spin in an infinite loop because the count wouldn't
be incremented each time.

Change-Id:I525a0da120205ffb9e507bf1a83cb02f077ba76d*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index c203231..b561cbb 100644

//Synthetic comment -- @@ -198,7 +198,7 @@
File renamedConfig = new File(base);
int i = 0;
while (renamedConfig.exists()) {
                        renamedConfig = new File(base+"."+i++);
}
mLog.error(null, "Error parsing %1$s, backing up to %2$s",
userDevicesFile.getAbsolutePath(), renamedConfig.getAbsolutePath());







