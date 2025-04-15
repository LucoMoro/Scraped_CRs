/*hv: Fix env var used to select protocol

Change-Id:I3b5f4de2d35f6c7fd8ec27bb36c0a4c79b176138*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java
//Synthetic comment -- index efc7926..24a5a4f 100644

//Synthetic comment -- @@ -22,7 +22,7 @@

public class HvDeviceFactory {
private static final String sHvProtoEnvVar =
            System.getenv("ANDROID_HVPROTO"); //$NON-NLS-1$

public static IHvDevice create(IDevice device) {
// default to old mechanism until the new one is fully tested







