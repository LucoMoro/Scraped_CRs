/*Make missing devices.xml a warning rather than an error

Since most users probably won't have custom devices, but instead use the
ones we provide, it doesn't make sense to have the absence of a custom,
user-specific devices.xml be an error instead of a warning.

Change-Id:I9754be29e88377b8c2e594f1d790310da35897fc*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index d46ca5f..9cf0378 100644

//Synthetic comment -- @@ -203,6 +203,8 @@
mLog.error(null, "Error parsing %1$s, backing up to %2$s",
userDevicesFile.getAbsolutePath(), renamedConfig.getAbsolutePath());
userDevicesFile.renameTo(renamedConfig);
                } catch (FileNotFoundException e) {
                    mLog.warning("No user devices found");
} catch (ParserConfigurationException e) {
mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
} catch (IOException e) {







