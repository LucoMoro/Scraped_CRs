/*Fixed format string for device parsing errors

Change-Id:I52f0aa20f9fb8a4b3e61ab820972af35f95f7bf8*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 7825215..3b2d1db 100644

//Synthetic comment -- @@ -176,11 +176,11 @@
try {
return DeviceParser.parse(deviceXml);
} catch (SAXException e) {
            mLog.error(null, "Error parsing %1$", deviceXml.getAbsolutePath());
} catch (ParserConfigurationException e) {
            mLog.error(null, "Error parsing %1$", deviceXml.getAbsolutePath());
} catch (IOException e) {
            mLog.error(null, "Error reading %1$", deviceXml.getAbsolutePath());
} catch (IllegalStateException e) {
// The device builders can throw IllegalStateExceptions if
// build gets called before everything is properly setup







