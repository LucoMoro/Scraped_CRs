/*Make the HierarchyViewer plug-in not screw up DDMS.

The HV plug-in behaves like the standalone app and initialized
the AndroidDebugBridge with no client support (even though
this init is handled by DDMS) and would recreate the bridge
object destroying the one created by DDMS.

This completely broke DDMS.

Change-Id:Ife1187c74daef69607d508aabb1f7234507a170f*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index c62ce42..01cdf0a 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
private static InetSocketAddress sSocketAddr;

private static AndroidDebugBridge sThis;
private static boolean sClientSupport;

/** Full path to adb. */
//Synthetic comment -- @@ -173,7 +174,11 @@
* @see AndroidDebugBridge#createBridge(String, boolean)
* @see DdmPreferences
*/
    public static void init(boolean clientSupport) {
sClientSupport = clientSupport;

// Determine port and instantiate socket address.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java
//Synthetic comment -- index 8f94c65..959bf6c 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
public void start(BundleContext context) throws Exception {
super.start(context);
sPlugin = this;
        

// set the consoles.
final MessageConsole messageConsole = new MessageConsole("Hierarchy Viewer", null); //$NON-NLS-1$
//Synthetic comment -- @@ -115,9 +115,10 @@
new Thread() {
@Override
public void run() {
                director.initDebugBridge();
                director.startListenForDevices();
                director.populateDeviceSelectionModel();
}
}.start();
}
//Synthetic comment -- @@ -143,7 +144,7 @@

/**
* Returns the shared instance
     * 
* @return the shared instance
*/
public static HierarchyViewerPlugin getPlugin() {
//Synthetic comment -- @@ -152,7 +153,7 @@

/**
* Set the location of the adb executable and optionally starts adb
     * 
* @param adb location of adb
* @param startAdb flag to start adb
*/
//Synthetic comment -- @@ -177,7 +178,7 @@

/**
* Prints a message, associated with a project to the specified stream
     * 
* @param stream The stream to write to
* @param tag The tag associated to the message. Can be null
* @param message The message to print.
//Synthetic comment -- @@ -192,7 +193,7 @@

/**
* Creates a string containing the current date/time, and the tag
     * 
* @param tag The tag associated to the message. Can be null
* @return The dateTag
*/








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 63b30c0..f397b1f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.hierarchyviewerlib;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.RawImage;
//Synthetic comment -- @@ -88,6 +89,21 @@
return sDirector;
}

public void initDebugBridge() {
DeviceBridge.initDebugBridge(getAdbLocation());
}
//Synthetic comment -- @@ -633,7 +649,7 @@
}
}
}
    
public void setPixelPerfectAutoRefreshInterval(int value) {
synchronized (mPixelPerfectRefreshTimer) {
if (mPixelPerfectAutoRefreshInterval == value) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 20feeec..74fbc13 100644

//Synthetic comment -- @@ -80,6 +80,37 @@
}
}

public static void initDebugBridge(String adbLocation) {
if (sBridge == null) {
AndroidDebugBridge.init(false /* debugger support */);
//Synthetic comment -- @@ -89,6 +120,7 @@
}
}

public static void terminate() {
AndroidDebugBridge.terminate();
}
//Synthetic comment -- @@ -117,7 +149,7 @@
* <p/>
* This starts a port forwarding between a local port and a port on the
* device.
     * 
* @param device
*/
public static void setupDeviceForward(IDevice device) {







