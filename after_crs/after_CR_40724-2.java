/*Load devices defined in tools

Change-Id:Ib40dada5062e755b48bd3a62514ede14cef5ec1e*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 9fb51c8..8bf45fc 100644

//Synthetic comment -- @@ -199,6 +199,15 @@
if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
mVendorDevicesLocation = sdkLocation;
List<Device> devices = new ArrayList<Device>();

                // Load devices from tools folder
                File toolsDevices = new File(sdkLocation, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER +
                        File.separator + SdkConstants.FN_DEVICES_XML);
                if (toolsDevices.isFile()) {
                    devices.addAll(loadDevices(toolsDevices));
                }

                // Load devices from vendor extras
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
for (File deviceDir : deviceDirs) {







