/*Move some constants from Device to IDevice.

Change-Id:I354b6bfb184c4168a8f3f81bb7184b40bfb2b220*/




//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Device.java b/ddmlib/src/main/java/com/android/ddmlib/Device.java
//Synthetic comment -- index 84b7da3..ab38e66 100644

//Synthetic comment -- @@ -34,8 +34,6 @@
* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {

private static final int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/IDevice.java b/ddmlib/src/main/java/com/android/ddmlib/IDevice.java
//Synthetic comment -- index b8f2dc9..b999dd8 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
public static final String PROP_BUILD_VERSION = "ro.build.version.release";
public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";
    public static final String DEVICE_MODEL_PROPERTY = "ro.product.model";
    public static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer";

public static final String PROP_DEBUGGABLE = "ro.debuggable";








