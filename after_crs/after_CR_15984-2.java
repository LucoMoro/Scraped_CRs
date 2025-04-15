/*Fix CTS to use the new ddmlib API.

Change-Id:I84b0e21029070ca7233df2ebd39b3a3718e173db*/




//Synthetic comment -- diff --git a/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java b/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java
//Synthetic comment -- index 1928a09..356cef9 100644

//Synthetic comment -- @@ -22,7 +22,11 @@

import junit.framework.Test;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.Log;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.testrunner.ITestRunListener;
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner;
import com.android.ddmlib.testrunner.TestIdentifier;
//Synthetic comment -- @@ -79,7 +83,7 @@
* Test that an app that declares the same shared uid as an existing app, cannot be installed
* if it is signed with a different certificate.
*/
    public void testSharedUidDifferentCerts() throws InstallException {
Log.i(LOG_TAG, "installing apks with shared uid, but different certs");
try {
// cleanup test apps that might be installed from previous partial test run
//Synthetic comment -- @@ -105,7 +109,7 @@
* Test that an app update cannot be installed over an existing app if it has a different
* certificate.
*/
    public void testAppUpgradeDifferentCerts() throws InstallException {
Log.i(LOG_TAG, "installing app upgrade with different certs");
try {
// cleanup test app that might be installed from previous partial test run
//Synthetic comment -- @@ -128,7 +132,8 @@
/**
* Test that an app cannot access another app's private data.
*/
    public void testAppFailAccessPrivateData() throws InstallException, TimeoutException,
            AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
Log.i(LOG_TAG, "installing app that attempts to access another app's private data");
try {
// cleanup test app that might be installed from previous partial test run
//Synthetic comment -- @@ -156,7 +161,8 @@
/**
* Test that an app cannot instrument another app that is signed with different certificate.
*/
    public void testInstrumentationDiffCert() throws InstallException, TimeoutException,
            AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
Log.i(LOG_TAG, "installing app that attempts to instrument another app");
try {
// cleanup test app that might be installed from previous partial test run
//Synthetic comment -- @@ -190,7 +196,8 @@
* Test that an app cannot use a signature-enforced permission if it is signed with a different
* certificate than the app that declared the permission.
*/
    public void testPermissionDiffCert() throws InstallException, TimeoutException,
            AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
Log.i(LOG_TAG, "installing app that attempts to use permission of another app");
try {
// cleanup test app that might be installed from previous partial test run
//Synthetic comment -- @@ -229,9 +236,15 @@
*
* @param pkgName Android application package for tests
* @return <code>true</code> if all tests passed.
     * @throws TimeoutException in case of a timeout on the connection.
     * @throws AdbCommandRejectedException if adb rejects the command
     * @throws ShellCommandUnresponsiveException if the device did not output any test result for
     * a period longer than the max time to output.
     * @throws IOException if connection to device was lost.
*/
    private boolean runDeviceTests(String pkgName)
            throws TimeoutException, AdbCommandRejectedException,
            ShellCommandUnresponsiveException, IOException {
CollectingTestRunListener listener = doRunTests(pkgName);
return listener.didAllTestsPass();
}
//Synthetic comment -- @@ -240,9 +253,15 @@
* Helper method to run tests and return the listener that collected the results.
* @param pkgName Android application package for tests
* @return the {@link CollectingTestRunListener}
     * @throws TimeoutException in case of a timeout on the connection.
     * @throws AdbCommandRejectedException if adb rejects the command
     * @throws ShellCommandUnresponsiveException if the device did not output any test result for
     * a period longer than the max time to output.
     * @throws IOException if connection to device was lost.
*/
    private CollectingTestRunListener doRunTests(String pkgName)
            throws TimeoutException, AdbCommandRejectedException,
            ShellCommandUnresponsiveException, IOException {
RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner(pkgName, getDevice());
CollectingTestRunListener listener = new CollectingTestRunListener();
testRunner.run(listener);
//Synthetic comment -- @@ -256,7 +275,8 @@
* @return the test run error message or <code>null</code> if test run completed.
* @throws IOException if connection to device was lost
*/
    private String runDeviceTestsWithRunResult(String pkgName) throws TimeoutException,
            AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
CollectingTestRunListener listener = doRunTests(pkgName);
return listener.getTestRunErrorMessage();
}








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/DeviceManager.java b/tools/host/src/com/android/cts/DeviceManager.java
//Synthetic comment -- index 528036e..80e05d5 100644

//Synthetic comment -- @@ -16,8 +16,10 @@

package com.android.cts;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;

import java.io.IOException;
//Synthetic comment -- @@ -230,6 +232,10 @@
mSemaphore.release();
} catch (IOException e) {
// FIXME: handle failed connection to device.
            } catch (TimeoutException e) {
                // FIXME: handle failed connection to device.
            } catch (AdbCommandRejectedException e) {
                // FIXME: handle failed connection to device.
}
}
}








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/ReferenceAppTestPackage.java b/tools/host/src/com/android/cts/ReferenceAppTestPackage.java
//Synthetic comment -- index 2b70b86..6b91ab4 100644

//Synthetic comment -- @@ -16,8 +16,10 @@

package com.android.cts;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.log.LogReceiver.ILogListener;
import com.android.ddmlib.log.LogReceiver.LogEntry;

//Synthetic comment -- @@ -199,6 +201,10 @@
}
} catch (IOException e) {
Log.e("Error taking snapshot! " + cmdArgs, e);
                } catch (TimeoutException e) {
                    Log.e("Error taking snapshot! " + cmdArgs, e);
                } catch (AdbCommandRejectedException e) {
                    Log.e("Error taking snapshot! " + cmdArgs, e);
}
}
});








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java b/tools/host/src/com/android/cts/TestDevice.java
//Synthetic comment -- index e384824..724ab12 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.cts;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IDevice;
//Synthetic comment -- @@ -23,13 +24,16 @@
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.log.LogReceiver;
import com.android.ddmlib.log.LogReceiver.ILogListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//Synthetic comment -- @@ -101,6 +105,8 @@
try {
mDevice.runLogService("main", logReceiver);
} catch (IOException e) {
            } catch (TimeoutException e) {
            } catch (AdbCommandRejectedException e) {
}
}

//Synthetic comment -- @@ -136,6 +142,10 @@
mSyncService = mDevice.getSyncService();
} catch (IOException e) {
// FIXME: handle failed connection.
        } catch (TimeoutException e) {
            // FIXME: handle failed connection.
        } catch (AdbCommandRejectedException e) {
            // FIXME: handle failed connection.
}
mBatchModeResultParser = null;
mUninstallObserver = new PackageActionObserver(ACTION_UNINSTALL);
//Synthetic comment -- @@ -892,10 +902,16 @@
* @param remotePath The remote path.
*/
public void pushFile(String localPath, String remotePath) {
        try {
            mSyncService.pushFile(localPath, remotePath, new PushMonitor());
        } catch (TimeoutException e) {
            Log.e("Uploading file failed: timeout", null);
        } catch (SyncException e) {
            Log.e("Uploading file failed: " + e.getMessage(), null);
        } catch (FileNotFoundException e) {
            Log.e("Uploading file failed: " + e.getMessage(), null);
        } catch (IOException e) {
            Log.e("Uploading file failed: " + e.getMessage(), null);
}
}

//Synthetic comment -- @@ -1640,6 +1656,12 @@
mDevice.executeShellCommand(cmd, receiver);
} catch (IOException e) {
Log.e("", e);
                } catch (TimeoutException e) {
                    Log.e("", e);
                } catch (AdbCommandRejectedException e) {
                    Log.e("", e);
                } catch (ShellCommandUnresponsiveException e) {
                    Log.e("", e);
}
}
}.start();
//Synthetic comment -- @@ -1857,8 +1879,11 @@
*
* @return the screenshot
* @throws IOException
     * @throws AdbCommandRejectedException
     * @throws TimeoutException
*/
    public RawImage getScreenshot() throws IOException, TimeoutException,
            AdbCommandRejectedException {
return mDevice.getScreenshot();
}
}







