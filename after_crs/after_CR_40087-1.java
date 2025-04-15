/*Rename bad device configs

Rather than overwriting bad device configs, and possibly losing
something important to the user, we move them to devices.xml.old
(or devices.xml.old.0, devices.xml.old.1, etc).

Change-Id:Ide992b36e9645bc15fce040ddf06e91e6076aad9*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 61d8307..4bb4344 100644

//Synthetic comment -- @@ -151,12 +151,28 @@
// User devices should be saved out to
// $HOME/.android/devices.xml
mUserDevices = new ArrayList<Device>();
                File userDevicesFile = null;
try {
                    userDevicesFile = new File(AndroidLocation.getFolder(),
SdkConstants.FN_DEVICES_XML);
                    mUserDevices.addAll(DeviceParser.parse(userDevicesFile));
} catch (AndroidLocationException e) {
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
                } catch (SAXException e) {
                    // Probably an old config file which we don't want to overwrite.
                    String base = userDevicesFile.getAbsoluteFile()+".old";
                    File renamedConfig = new File(base);
                    int i = 0;
                    while (renamedConfig.exists()) {
                        renamedConfig = new File(base+"."+i);
                    }
                    mLog.error(null, "Error parsing %1$s, backing up to %2$s",
                            userDevicesFile.getAbsolutePath(), renamedConfig.getAbsolutePath());
                    userDevicesFile.renameTo(renamedConfig);
                } catch (ParserConfigurationException e) {
                    mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
                } catch (IOException e) {
                    mLog.error(null, "Error parsing %1$s", userDevicesFile.getAbsolutePath());
}
}
}







