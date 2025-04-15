/*Include status-bar node when writing out Device's software

Change-Id:I973d454d040ab537e10727f16f70270dc0bae7ce*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
//Synthetic comment -- index 453a4e5..34eaab5 100644

//Synthetic comment -- @@ -217,6 +217,8 @@
addElement(doc, software, DeviceSchema.NODE_BLUETOOTH_PROFILES, sw.getBluetoothProfiles());
addElement(doc, software, DeviceSchema.NODE_GL_VERSION, sw.getGlVersion());
addElement(doc, software, DeviceSchema.NODE_GL_EXTENSIONS, sw.getGlExtensions());

return software;
}







