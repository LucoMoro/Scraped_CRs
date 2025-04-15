/*Fix hosttests to use the new ddmlib API.

Change-Id:I629876c5baabc1cd8700604c4ebed13cc9e2168d*/
//Synthetic comment -- diff --git a/core/tests/hosttests/src/android/content/pm/PackageManagerHostTestUtils.java b/core/tests/hosttests/src/android/content/pm/PackageManagerHostTestUtils.java
//Synthetic comment -- index 4cb50ce..b225c37 100644

//Synthetic comment -- @@ -16,24 +16,24 @@

package android.content.pm;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmlib.SyncService.SyncResult;
import com.android.ddmlib.testrunner.ITestRunListener;
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner;
import com.android.ddmlib.testrunner.TestIdentifier;
import com.android.hosttest.DeviceTestCase;
import com.android.hosttest.DeviceTestSuite;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.Runtime;
import java.lang.Process;
//Synthetic comment -- @@ -42,7 +42,6 @@
import java.util.regex.Pattern;

import junit.framework.Assert;
import com.android.hosttest.DeviceTestCase;

/**
* Set of tests that verify host side install cases
//Synthetic comment -- @@ -120,8 +119,14 @@
* Helper method to run tests and return the listener that collected the results.
* @param pkgName Android application package for tests
* @return the {@link CollectingTestRunListener}
*/
    private CollectingTestRunListener doRunTests(String pkgName) throws IOException {
RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner(
pkgName, mDevice);
CollectingTestRunListener listener = new CollectingTestRunListener();
//Synthetic comment -- @@ -134,8 +139,14 @@
*
* @param pkgName Android application package for tests
* @return true if every test passed, false otherwise.
*/
    public boolean runDeviceTestsDidAllTestsPass(String pkgName) throws IOException {
CollectingTestRunListener listener = doRunTests(pkgName);
return listener.didAllTestsPass();
}
//Synthetic comment -- @@ -143,22 +154,26 @@
/**
* Helper method to push a file to device
* @param apkAppPrivatePath
     * @throws IOException
*/
public void pushFile(final String localFilePath, final String destFilePath)
            throws IOException {
        SyncResult result = mDevice.getSyncService().pushFile(
                localFilePath, destFilePath, new NullSyncProgressMonitor());
        assertEquals(SyncService.RESULT_OK, result.getCode());
}

/**
* Helper method to install a file
* @param localFilePath the absolute file system path to file on local host to install
* @param reinstall set to <code>true</code> if re-install of app should be performed
     * @throws IOException
*/
    public void installFile(final String localFilePath, final boolean replace) throws IOException {
String result = mDevice.installPackage(localFilePath, replace);
assertEquals(null, result);
}
//Synthetic comment -- @@ -168,10 +183,11 @@
* @param localFilePath the absolute file system path to file on local host to install
* @param reinstall set to <code>true</code> if re-install of app should be performed
* @return the string output of the failed install attempt
     * @throws IOException
*/
public String installFileFail(final String localFilePath, final boolean replace)
            throws IOException {
String result = mDevice.installPackage(localFilePath, replace);
assertNotNull(result);
return result;
//Synthetic comment -- @@ -181,10 +197,17 @@
* Helper method to install a file to device as forward locked
* @param localFilePath the absolute file system path to file on local host to install
* @param reinstall set to <code>true</code> if re-install of app should be performed
     * @throws IOException
*/
public String installFileForwardLocked(final String localFilePath, final boolean replace)
            throws IOException {
String remoteFilePath = mDevice.syncPackageToDevice(localFilePath);
InstallReceiver receiver = new InstallReceiver();
String cmd = String.format(replace ? "pm install -r -l \"%1$s\"" :
//Synthetic comment -- @@ -199,9 +222,14 @@
*
* @param destPath the absolute path of file on device to check
* @return <code>true</code> if file exists, <code>false</code> otherwise.
     * @throws IOException if adb shell command failed
*/
    public boolean doesRemoteFileExist(String destPath) throws IOException {
String lsGrep = executeShellCommand(String.format("ls %s", destPath));
return !lsGrep.contains("No such file or directory");
}
//Synthetic comment -- @@ -212,10 +240,15 @@
* @param destPath the absolute path of the file
* @return <code>true</code> if file exists containing given string,
*         <code>false</code> otherwise.
     * @throws IOException if adb shell command failed
*/
public boolean doesRemoteFileExistContainingString(String destPath, String searchString)
            throws IOException {
String lsResult = executeShellCommand(String.format("ls %s", destPath));
return lsResult.contains(searchString);
}
//Synthetic comment -- @@ -225,9 +258,14 @@
*
* @param packageName the Android manifest package to check.
* @return <code>true</code> if package exists, <code>false</code> otherwise
     * @throws IOException if adb shell command failed
*/
    public boolean doesPackageExist(String packageName) throws IOException {
String pkgGrep = executeShellCommand(String.format("pm path %s", packageName));
return pkgGrep.contains("package:");
}
//Synthetic comment -- @@ -237,9 +275,14 @@
*
* @param packageName package name to check for
* @return <code>true</code> if file exists, <code>false</code> otherwise.
     * @throws IOException if adb shell command failed
*/
    public boolean doesAppExistOnDevice(String packageName) throws IOException {
return doesRemoteFileExistContainingString(DEVICE_APP_PATH, packageName);
}

//Synthetic comment -- @@ -248,9 +291,14 @@
*
* @param packageName package name to check for
* @return <code>true</code> if file exists, <code>false</code> otherwise.
     * @throws IOException if adb shell command failed
*/
    public boolean doesAppExistOnSDCard(String packageName) throws IOException {
return doesRemoteFileExistContainingString(SDCARD_APP_PATH, packageName);
}

//Synthetic comment -- @@ -259,9 +307,14 @@
*
* @param packageName package name to check for
* @return <code>true</code> if file exists, <code>false</code> otherwise.
     * @throws IOException if adb shell command failed
*/
    public boolean doesAppExistAsForwardLocked(String packageName) throws IOException {
return doesRemoteFileExistContainingString(APP_PRIVATE_PATH, packageName);
}

//Synthetic comment -- @@ -269,9 +322,14 @@
* Waits for device's package manager to respond.
*
* @throws InterruptedException
     * @throws IOException
*/
    public void waitForPackageManager() throws InterruptedException, IOException {
Log.i(LOG_TAG, "waiting for device");
int currentWaitTime = 0;
// poll the package manager until it returns something for android
//Synthetic comment -- @@ -337,9 +395,14 @@
*
* @param packageName The name of the package to wait to load
* @throws InterruptedException
     * @throws IOException
*/
    public void waitForApp(String packageName) throws InterruptedException, IOException {
Log.i(LOG_TAG, "waiting for app to launch");
int currentWaitTime = 0;
// poll the package manager until it returns something for the package we're looking for
//Synthetic comment -- @@ -356,9 +419,14 @@
/**
* Helper method which executes a adb shell command and returns output as a {@link String}
* @return the output of the command
     * @throws IOException
*/
    public String executeShellCommand(String command) throws IOException {
Log.i(LOG_TAG, String.format("adb shell %s", command));
CollectingOutputReceiver receiver = new CollectingOutputReceiver();
mDevice.executeShellCommand(command, receiver);
//Synthetic comment -- @@ -370,9 +438,14 @@
/**
* Helper method ensures we are in root mode on the host side. It returns only after
* PackageManager is actually up and running.
     * @throws IOException
*/
    public void runAdbRoot() throws IOException, InterruptedException {
Log.i(LOG_TAG, "adb root");
Runtime runtime = Runtime.getRuntime();
Process process = runtime.exec("adb root"); // adb should be in the path
//Synthetic comment -- @@ -390,10 +463,15 @@
/**
* Helper method which reboots the device and returns once the device is online again
* and package manager is up and running (note this function is synchronous to callers).
     * @throws IOException
* @throws InterruptedException
*/
    public void rebootDevice() throws IOException, InterruptedException {
String command = "reboot"; // no need for -s since mDevice is already tied to a device
Log.i(LOG_TAG, command);
CollectingOutputReceiver receiver = new CollectingOutputReceiver();
//Synthetic comment -- @@ -546,17 +624,23 @@
/**
* Helper method for installing an app to wherever is specified in its manifest, and
* then verifying the app was installed onto SD Card.
*
* @param the path of the apk to install
* @param the name of the package
* @param <code>true</code> if the app should be overwritten, <code>false</code> otherwise
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
     * <p/>
     * Assumes adb is running as root in device under test.
*/
public void installAppAndVerifyExistsOnSDCard(String apkPath, String pkgName, boolean overwrite)
            throws IOException, InterruptedException {
// Start with a clean slate if we're not overwriting
if (!overwrite) {
// cleanup test app just in case it already exists
//Synthetic comment -- @@ -577,17 +661,23 @@
/**
* Helper method for installing an app to wherever is specified in its manifest, and
* then verifying the app was installed onto device.
*
* @param the path of the apk to install
* @param the name of the package
* @param <code>true</code> if the app should be overwritten, <code>false</code> otherwise
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
     * <p/>
     * Assumes adb is running as root in device under test.
*/
public void installAppAndVerifyExistsOnDevice(String apkPath, String pkgName, boolean overwrite)
            throws IOException, InterruptedException {
// Start with a clean slate if we're not overwriting
if (!overwrite) {
// cleanup test app just in case it already exists
//Synthetic comment -- @@ -608,17 +698,24 @@
/**
* Helper method for installing an app as forward-locked, and
* then verifying the app was installed in the proper forward-locked location.
*
* @param the path of the apk to install
* @param the name of the package
* @param <code>true</code> if the app should be overwritten, <code>false</code> otherwise
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
     * <p/>
     * Assumes adb is running as root in device under test.
*/
public void installFwdLockedAppAndVerifyExists(String apkPath,
            String pkgName, boolean overwrite) throws IOException, InterruptedException {
// Start with a clean slate if we're not overwriting
if (!overwrite) {
// cleanup test app just in case it already exists
//Synthetic comment -- @@ -639,14 +736,21 @@

/**
* Helper method for uninstalling an app.
     *
     * @param pkgName package name to uninstall
     * @throws IOException if adb shell command failed
     * @throws InterruptedException if the thread was interrupted
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void uninstallApp(String pkgName) throws IOException, InterruptedException {
mDevice.uninstallPackage(pkgName);
// make sure its not installed anymore
assertFalse(doesPackageExist(pkgName));
//Synthetic comment -- @@ -656,12 +760,18 @@
* Helper method for clearing any installed non-system apps.
* Useful ensuring no non-system apps are installed, and for cleaning up stale files that
* may be lingering on the system for whatever reason.
     *
     * @throws IOException if adb shell command failed
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void wipeNonSystemApps() throws IOException {
String allInstalledPackages = executeShellCommand("pm list packages -f");
BufferedReader outputReader = new BufferedReader(new StringReader(allInstalledPackages));

//Synthetic comment -- @@ -686,8 +796,14 @@
*
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void setDevicePreferredInstallLocation(InstallLocPreference pref) throws IOException {
String command = "pm setInstallLocation %d";
int locValue = 0;
switch (pref) {
//Synthetic comment -- @@ -709,8 +825,14 @@
*
* <p/>
* Assumes adb is running as root in device under test.
*/
    public InstallLocPreference getDevicePreferredInstallLocation() throws IOException {
String result = executeShellCommand("pm getInstallLocation");
if (result.indexOf('0') != -1) {
return InstallLocPreference.AUTO;








//Synthetic comment -- diff --git a/core/tests/hosttests/src/android/content/pm/PackageManagerHostTests.java b/core/tests/hosttests/src/android/content/pm/PackageManagerHostTests.java
//Synthetic comment -- index 1b797d5..22a2be6 100644

//Synthetic comment -- @@ -16,13 +16,12 @@

package android.content.pm;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmlib.SyncService.SyncResult;
import com.android.hosttest.DeviceTestCase;
import com.android.hosttest.DeviceTestSuite;

//Synthetic comment -- @@ -156,10 +155,18 @@
* the app, and otherwise cause the system to blow up.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testPushAppPrivate() throws IOException, InterruptedException {
Log.i(LOG_TAG, "testing pushing an apk to /data/app-private");
final String apkAppPrivatePath =  appPrivatePath + SIMPLE_APK;

//Synthetic comment -- @@ -187,12 +194,18 @@
* @param apkName the file name of the test app apk
* @param pkgName the package name of the test app apk
* @param expectedLocation the file name of the test app apk
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
private void doStandardInstall(String apkName, String pkgName,
PackageManagerHostTestUtils.InstallLocation expectedLocation)
            throws IOException, InterruptedException {

if (expectedLocation == PackageManagerHostTestUtils.InstallLocation.DEVICE) {
mPMHostUtils.installAppAndVerifyExistsOnDevice(
//Synthetic comment -- @@ -211,12 +224,18 @@
* Assumes adb is running as root in device under test.
* @param preference the device's preferred location of where to install apps
* @param expectedLocation the expected location of where the apk was installed
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void installAppAutoLoc(PackageManagerHostTestUtils.InstallLocPreference preference,
PackageManagerHostTestUtils.InstallLocation expectedLocation)
            throws IOException, InterruptedException {

PackageManagerHostTestUtils.InstallLocPreference savedPref =
PackageManagerHostTestUtils.InstallLocPreference.AUTO;
//Synthetic comment -- @@ -239,10 +258,16 @@
* will install the app to the device when device's preference is auto.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppAutoLocPrefIsAuto() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=auto, prefer=auto gets installed on device");
installAppAutoLoc(PackageManagerHostTestUtils.InstallLocPreference.AUTO,
PackageManagerHostTestUtils.InstallLocation.DEVICE);
//Synthetic comment -- @@ -253,10 +278,17 @@
* will install the app to the device when device's preference is internal.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppAutoLocPrefIsInternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=auto, prefer=internal gets installed on device");
installAppAutoLoc(PackageManagerHostTestUtils.InstallLocPreference.INTERNAL,
PackageManagerHostTestUtils.InstallLocation.DEVICE);
//Synthetic comment -- @@ -267,10 +299,17 @@
* will install the app to the SD card when device's preference is external.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppAutoLocPrefIsExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=auto, prefer=external gets installed on device");
installAppAutoLoc(PackageManagerHostTestUtils.InstallLocPreference.EXTERNAL,
PackageManagerHostTestUtils.InstallLocation.DEVICE);
//Synthetic comment -- @@ -283,12 +322,18 @@
* Assumes adb is running as root in device under test.
* @param preference the device's preferred location of where to install apps
* @param expectedLocation the expected location of where the apk was installed
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void installAppInternalLoc(PackageManagerHostTestUtils.InstallLocPreference preference,
PackageManagerHostTestUtils.InstallLocation expectedLocation)
            throws IOException, InterruptedException {

PackageManagerHostTestUtils.InstallLocPreference savedPref =
PackageManagerHostTestUtils.InstallLocPreference.AUTO;
//Synthetic comment -- @@ -311,10 +356,17 @@
* will install the app to the device when device's preference is auto.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppInternalLocPrefIsAuto() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=internal, prefer=auto gets installed on device");
installAppInternalLoc(PackageManagerHostTestUtils.InstallLocPreference.AUTO,
PackageManagerHostTestUtils.InstallLocation.DEVICE);
//Synthetic comment -- @@ -325,10 +377,17 @@
* will install the app to the device when device's preference is internal.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppInternalLocPrefIsInternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=internal, prefer=internal is installed on device");
installAppInternalLoc(PackageManagerHostTestUtils.InstallLocPreference.INTERNAL,
PackageManagerHostTestUtils.InstallLocation.DEVICE);
//Synthetic comment -- @@ -339,10 +398,17 @@
* will install the app to the device when device's preference is external.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppInternalLocPrefIsExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=internal, prefer=external is installed on device");
installAppInternalLoc(PackageManagerHostTestUtils.InstallLocPreference.EXTERNAL,
PackageManagerHostTestUtils.InstallLocation.DEVICE);
//Synthetic comment -- @@ -355,12 +421,18 @@
* Assumes adb is running as root in device under test.
* @param preference the device's preferred location of where to install apps
* @param expectedLocation the expected location of where the apk was installed
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void installAppExternalLoc(PackageManagerHostTestUtils.InstallLocPreference preference,
PackageManagerHostTestUtils.InstallLocation expectedLocation)
            throws IOException, InterruptedException {

PackageManagerHostTestUtils.InstallLocPreference savedPref =
PackageManagerHostTestUtils.InstallLocPreference.AUTO;
//Synthetic comment -- @@ -384,10 +456,17 @@
* will install the app to the device when device's preference is auto.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppExternalLocPrefIsAuto() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=external, pref=auto gets installed on SD Card");
installAppExternalLoc(PackageManagerHostTestUtils.InstallLocPreference.AUTO,
PackageManagerHostTestUtils.InstallLocation.SDCARD);
//Synthetic comment -- @@ -398,10 +477,17 @@
* will install the app to the device when device's preference is internal.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppExternalLocPrefIsInternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=external, pref=internal gets installed on SD Card");
installAppExternalLoc(PackageManagerHostTestUtils.InstallLocPreference.INTERNAL,
PackageManagerHostTestUtils.InstallLocation.SDCARD);
//Synthetic comment -- @@ -412,10 +498,17 @@
* will install the app to the device when device's preference is external.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppExternalLocPrefIsExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installLocation=external, pref=external gets installed on SD Card");
installAppExternalLoc(PackageManagerHostTestUtils.InstallLocPreference.EXTERNAL,
PackageManagerHostTestUtils.InstallLocation.SDCARD);
//Synthetic comment -- @@ -427,10 +520,17 @@
* system decide.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppNoLocPrefIsAuto() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with no installLocation gets installed on device");

PackageManagerHostTestUtils.InstallLocPreference savedPref =
//Synthetic comment -- @@ -456,10 +556,17 @@
* external.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppNoLocPrefIsExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with no installLocation gets installed on SD card");

PackageManagerHostTestUtils.InstallLocPreference savedPref =
//Synthetic comment -- @@ -485,10 +592,17 @@
* internal.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallAppNoLocPrefIsInternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with no installLocation gets installed on device");

PackageManagerHostTestUtils.InstallLocPreference savedPref =
//Synthetic comment -- @@ -513,10 +627,18 @@
* forward-locked will get installed to the correct location.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallFwdLockedAppInternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with installLoc set to Internal gets installed to app-private");

try {
//Synthetic comment -- @@ -534,10 +656,18 @@
* forward-locked will get installed to the correct location.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallFwdLockedAppExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with installLoc set to Internal gets installed to app-private");

try {
//Synthetic comment -- @@ -555,10 +685,18 @@
* forward-locked will get installed to the correct location.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallFwdLockedAppAuto() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with installLoc set to Auto gets installed to app-private");

try {
//Synthetic comment -- @@ -576,10 +714,18 @@
* forward-locked installed will get installed to the correct location.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testInstallFwdLockedAppNone() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test an app with no installLoc set gets installed to app-private");

try {
//Synthetic comment -- @@ -597,14 +743,21 @@
* uninstall it, and reinstall it onto the SD card.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
// TODO: This currently relies on the app's manifest to switch from device to
// SD card install locations. We might want to make Device's installPackage()
// accept a installLocation flag so we can install a package to the
// destination of our choosing.
    public void testReinstallInternalToExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installing an app first to the device, then to the SD Card");

try {
//Synthetic comment -- @@ -625,14 +778,21 @@
* uninstall it, and reinstall it onto the device.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
// TODO: This currently relies on the app's manifest to switch from device to
// SD card install locations. We might want to make Device's installPackage()
// accept a installLocation flag so we can install a package to the
// destination of our choosing.
    public void testReinstallExternalToInternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installing an app first to the SD Care, then to the device");

try {
//Synthetic comment -- @@ -655,10 +815,16 @@
* the update onto the SD card as well when location is set to external for both versions
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testUpdateBothExternal() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating an app on the SD card stays on the SD card");

try {
//Synthetic comment -- @@ -681,10 +847,16 @@
* updated apps' manifest file.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testUpdateToSDCard() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating an app on the SD card stays on the SD card");

try {
//Synthetic comment -- @@ -706,10 +878,17 @@
* the update onto the device if the manifest has changed to installLocation=internalOnly
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
    public void testUpdateSDCardToDevice() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating an app on the SD card to the Device through manifest change");

try {
//Synthetic comment -- @@ -731,11 +910,18 @@
* the update onto the device's forward-locked location
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndUpdateExternalLocForwardLockedApp()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating a forward-locked app marked preferExternal");

try {
//Synthetic comment -- @@ -757,11 +943,18 @@
* the update onto the device's forward-locked location
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndUpdateNoLocForwardLockedApp()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating a forward-locked app with no installLocation pref set");

try {
//Synthetic comment -- @@ -783,11 +976,18 @@
* and then launched without crashing.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchAllPermsAppOnSD()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app with all perms set, installed on SD card");

try {
//Synthetic comment -- @@ -808,11 +1008,17 @@
* run without permissions errors.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchFLPermsAppOnSD()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app with location perms set, installed on SD card");

try {
//Synthetic comment -- @@ -833,11 +1039,17 @@
* run without permissions errors.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchBTPermsAppOnSD()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app with bluetooth perms set, installed on SD card");

try {
//Synthetic comment -- @@ -858,11 +1070,17 @@
* SecurityException when launched if its other shared apps are not installed.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchSharedPermsAppOnSD_NoPerms()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app with no explicit perms set, installed on SD card");

try {
//Synthetic comment -- @@ -888,11 +1106,17 @@
* shared apps are installed.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchSharedPermsAppOnSD_GrantedPerms()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app with no explicit perms set, installed on SD card");

try {
//Synthetic comment -- @@ -921,11 +1145,17 @@
* run without permissions errors even after a reboot
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchFLPermsAppOnSD_Reboot()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app with location perms set, installed on SD card");

try {
//Synthetic comment -- @@ -951,11 +1181,17 @@
* shared apps are installed, even after a reboot.
* <p/>
* Assumes adb is running as root in device under test.
     * @throws IOException if adb shell command failed
* @throws InterruptedException if the thread was interrupted
*/
public void testInstallAndLaunchSharedPermsAppOnSD_Reboot()
            throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test launching an app on SD, with no explicit perms set after reboot");

try {








//Synthetic comment -- diff --git a/core/tests/hosttests/src/android/content/pm/PackageManagerStressHostTests.java b/core/tests/hosttests/src/android/content/pm/PackageManagerStressHostTests.java
//Synthetic comment -- index 715c55b..a2a5dd3 100644

//Synthetic comment -- @@ -16,8 +16,11 @@

package android.content.pm;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.hosttest.DeviceTestCase;
import com.android.hosttest.DeviceTestSuite;

//Synthetic comment -- @@ -138,7 +141,9 @@
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void testUpdateAppManyTimesOnSD() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating an app on SD numerous times");

// cleanup test app just in case it already exists
//Synthetic comment -- @@ -173,7 +178,9 @@
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void testUninstallReinstallAppOnSDManyTimes() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test updating an app on the SD card stays on the SD card");

// cleanup test app just in case it was already exists
//Synthetic comment -- @@ -207,7 +214,9 @@
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void testInstallManyLargeAppsOnSD() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installing 20 large apps onto the sd card");

try {
//Synthetic comment -- @@ -251,7 +260,9 @@
* <p/>
* Assumes adb is running as root in device under test.
*/
    public void testInstallManyAppsOnSD() throws IOException, InterruptedException {
Log.i(LOG_TAG, "Test installing 500 small apps onto SD");

try {







