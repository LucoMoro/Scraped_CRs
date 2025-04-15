/*SDK Lib: fix some javadoc references.

Fixes some warnings due to obsolete javadoc references,
mostly classes that have moved around.

Change-Id:I38179f84d42b033f6f1132db2759d00e5a301fa5*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index ff18c8b..626e61f 100644

//Synthetic comment -- @@ -605,7 +605,6 @@
* Adds the resources from a source folder to a given {@link IArchiveBuilder}
* @param sourceFolder the source folder.
* @throws ApkCreationException if an error occurred
     * @throws SealedApkException if the APK is already sealed.
* @throws DuplicateFileException if a file conflicts with another already added to the APK
*                                   at the same location inside the APK archive.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index af9dee3..b2abb0f 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
private String mVendorDevicesLocation = "";
private static List<Device> mUserDevices;
private static List<Device> mDefaultDevices;
    private static final Object lock = new Object();
private static final List<DevicesChangeListener> listeners =
Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

//Synthetic comment -- @@ -151,7 +151,7 @@

/**
* Returns both vendor provided and user created {@link Device}s.
     * 
* @param sdkLocation Location of the Android SDK
* @return A list of both vendor and user provided {@link Device}s
*/
//Synthetic comment -- @@ -164,11 +164,11 @@

/**
* Gets the {@link List} of {@link Device}s packaged with the SDK.
     * 
* @return The {@link List} of default {@link Device}s
*/
public List<Device> getDefaultDevices() {
        synchronized (lock) {
if (mDefaultDevices == null) {
try {
mDefaultDevices = DeviceParser.parse(
//Synthetic comment -- @@ -190,12 +190,12 @@

/**
* Returns all vendor provided {@link Device}s
     * 
* @param sdkLocation Location of the Android SDK
* @return A list of vendor provided {@link Device}s
*/
public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (lock) {
if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
mVendorDevicesLocation = sdkLocation;
List<Device> devices = new ArrayList<Device>();
//Synthetic comment -- @@ -225,11 +225,11 @@

/**
* Returns all user created {@link Device}s
     * 
* @return All user created {@link Device}s
*/
public List<Device> getUserDevices() {
        synchronized (lock) {
if (mUserDevices == null) {
// User devices should be saved out to
// $HOME/.android/devices.xml
//Synthetic comment -- @@ -244,6 +244,7 @@
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
} catch (SAXException e) {
// Probably an old config file which we don't want to overwrite.
String base = userDevicesFile.getAbsoluteFile()+".old";
File renamedConfig = new File(base);
int i = 0;
//Synthetic comment -- @@ -266,7 +267,7 @@
}

public void addUserDevice(Device d) {
        synchronized (lock) {
if (mUserDevices == null) {
getUserDevices();
}
//Synthetic comment -- @@ -276,7 +277,7 @@
}

public void removeUserDevice(Device d) {
        synchronized (lock) {
if (mUserDevices == null) {
getUserDevices();
}
//Synthetic comment -- @@ -295,7 +296,7 @@
}

public void replaceUserDevice(Device d) {
        synchronized (lock) {
if (mUserDevices == null) {
getUserDevices();
}
//Synthetic comment -- @@ -309,7 +310,7 @@
* {@link AndroidLocation#getFolder()}.
*/
public void saveUserDevices() {
        synchronized (lock) {
if (mUserDevices != null && mUserDevices.size() != 0) {
File userDevicesFile;
try {
//Synthetic comment -- @@ -333,8 +334,8 @@

/**
* Returns hardware properties (defined in hardware.ini) as a {@link Map}.
     * 
     * @param The {@link State} from which to derive the hardware properties.
* @return A {@link Map} of hardware properties.
*/
public static Map<String, String> getHardwareProperties(State s) {
//Synthetic comment -- @@ -360,9 +361,9 @@

/**
* Returns the hardware properties defined in
     * {@link AvdManager.HARDWARE_INI} as a {@link Map}.
     * 
     * @param The {@link Device} from which to derive the hardware properties.
* @return A {@link Map} of hardware properties.
*/
public static Map<String, String> getHardwareProperties(Device d) {
//Synthetic comment -- @@ -381,11 +382,11 @@
/**
* Takes a boolean and returns the appropriate value for
* {@link HardwareProperties}
     * 
* @param bool The boolean value to turn into the appropriate
*            {@link HardwareProperties} value.
     * @return {@value HardwareProperties#BOOLEAN_VALUES[0]} if true,
     *         {@value HardwareProperties#BOOLEAN_VALUES[1]} otherwise.
*/
private static String getBooleanVal(boolean bool) {
if (bool) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
//Synthetic comment -- index 4480745..feed6d4 100644

//Synthetic comment -- @@ -47,8 +47,8 @@

/**
* Writes the XML definition of the given {@link Collection} of {@link Device}s according to
     * {@value #NS_DEVICES_XSD} to the {@link OutputStream}. Note that it is up to the caller to
     * close the {@link OutputStream}.
* @param out The {@link OutputStream} to write the resulting XML to.
* @param devices The {@link Device}s from which to generate the XML.
* @throws ParserConfigurationException








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index cdd3f84..9e5c61f 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.SdkConstants;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;

import java.io.File;
import java.util.Collections;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
//Synthetic comment -- index e13ac5e..21dcc36 100644

//Synthetic comment -- @@ -97,7 +97,7 @@
*     overridden by the build.properties file.
* </ul>
*
     * @param type One the possible {@link PropertyType}s.
* @return this object, for chaining.
*/
public synchronized ProjectPropertiesWorkingCopy merge(PropertyType type) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/NullTaskMonitor.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/NullTaskMonitor.java
//Synthetic comment -- index 6464484..f5120c3 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.internal.repository;

import com.android.utils.ILogger;


/**
//Synthetic comment -- @@ -24,7 +25,7 @@
* <p/>
* This can be passed to methods that require a monitor when the caller doesn't
* have any UI to update or means to report tracked progress.
 * A custom {@link ILogger} is used. Clients could use {@link NullSdkLog} if
* they really don't care about the logging either.
*/
public class NullTaskMonitor implements ITaskMonitor {
//Synthetic comment -- @@ -38,7 +39,7 @@
* This can be passed to methods that require a monitor when the caller doesn't
* have any UI to update or means to report tracked progress.
*
     * @param log An {@link ILogger}. Must not be null. Consider using {@link NullSdkLog}.
*/
public NullTaskMonitor(ILogger log) {
mLog = log;







