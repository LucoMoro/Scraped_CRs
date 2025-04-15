/*Merge remote branch 'korg/froyo' into froyomerge

Conflicts:
	tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java
	tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
	tests/tests/view/src/android/view/cts/ViewTest.java
	tools/host/src/com/android/cts/Version.java
	tools/utils/CollectAllTests.java

Change-Id:I32c04ce6a2eba4c1f30fe80afb9bf2adcf269e73*/




//Synthetic comment -- diff --git a/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java b/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java
//Synthetic comment -- index c0503bc..f162edc 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.testrunner.ITestRunListener;
//Synthetic comment -- @@ -53,6 +54,12 @@
// testAppFailAccessPrivateData constants
private static final String APP_WITH_DATA_APK = "CtsAppWithData.apk";
private static final String APP_WITH_DATA_PKG = "com.android.cts.appwithdata";
    private static final String APP_WITH_DATA_CLASS =
            "com.android.cts.appwithdata.CreatePrivateDataTest";
    private static final String APP_WITH_DATA_CREATE_METHOD =
            "testCreatePrivateData";
    private static final String APP_WITH_DATA_CHECK_NOEXIST_METHOD =
            "testEnsurePrivateDataNotExist";
private static final String APP_ACCESS_DATA_APK = "CtsAppAccessData.apk";
private static final String APP_ACCESS_DATA_PKG = "com.android.cts.appaccessdata";

//Synthetic comment -- @@ -144,7 +151,8 @@
false);
assertNull("failed to install app with data", installResult);
// run appwithdata's tests to create private data
            assertTrue("failed to create app's private data", runDeviceTests(APP_WITH_DATA_PKG,
                    APP_WITH_DATA_CLASS, APP_WITH_DATA_CREATE_METHOD));

installResult = getDevice().installPackage(getTestAppFilePath(APP_ACCESS_DATA_APK),
false);
//Synthetic comment -- @@ -159,6 +167,37 @@
}

/**
     * Test that uninstall of an app removes its private data.
     */
    public void testUninstallRemovesData() throws Exception {
        Log.i(LOG_TAG, "Uninstalling app, verifying data is removed.");
        try {
            // cleanup test app that might be installed from previous partial test run
            getDevice().uninstallPackage(APP_WITH_DATA_PKG);

            String installResult = getDevice().installPackage(getTestAppFilePath(APP_WITH_DATA_APK),
                    false);
            assertNull("failed to install app with data", installResult);
            // run appwithdata's tests to create private data
            assertTrue("failed to create app's private data", runDeviceTests(APP_WITH_DATA_PKG,
                    APP_WITH_DATA_CLASS, APP_WITH_DATA_CREATE_METHOD));

            getDevice().uninstallPackage(APP_WITH_DATA_PKG);

            installResult = getDevice().installPackage(getTestAppFilePath(APP_WITH_DATA_APK),
                    false);
            assertNull("failed to install app with data second time", installResult);
            // run appwithdata's 'check if file exists' test
            assertTrue("app's private data still exists after install", runDeviceTests(
                    APP_WITH_DATA_PKG, APP_WITH_DATA_CLASS, APP_WITH_DATA_CHECK_NOEXIST_METHOD));

        }
        finally {
            getDevice().uninstallPackage(APP_WITH_DATA_PKG);
        }
    }

    /**
* Test that an app cannot instrument another app that is signed with different certificate.
*/
public void testInstrumentationDiffCert() throws InstallException, TimeoutException,
//Synthetic comment -- @@ -239,10 +278,21 @@
* a period longer than the max time to output.
* @throws IOException if connection to device was lost.
*/
    private boolean runDeviceTests(String pkgName) throws AdbCommandRejectedException,
            ShellCommandUnresponsiveException, IOException, TimeoutException {
    	return runDeviceTests(pkgName, null, null);
    }

    /**
     * Helper method that will the specified packages tests on device.
     *
     * @param pkgName Android application package for tests
     * @return <code>true</code> if all tests passed.
     */
    private boolean runDeviceTests(String pkgName, String testClassName, String testMethodName)
            throws AdbCommandRejectedException, IOException, ShellCommandUnresponsiveException,
                   TimeoutException {
        CollectingTestRunListener listener = doRunTests(pkgName, testClassName, testMethodName);
return listener.didAllTestsPass();
}

//Synthetic comment -- @@ -256,28 +306,18 @@
* a period longer than the max time to output.
* @throws IOException if connection to device was lost.
*/
    private CollectingTestRunListener doRunTests(String pkgName, String testClassName,
            String testMethodName) throws AdbCommandRejectedException, IOException,
                    ShellCommandUnresponsiveException, TimeoutException {
RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner(pkgName, getDevice());
        if (testClassName != null && testMethodName != null) {
            testRunner.setMethodName(testClassName, testMethodName);
        }
CollectingTestRunListener listener = new CollectingTestRunListener();
testRunner.run(listener);
return listener;
}

private static class CollectingTestRunListener implements ITestRunListener {

private boolean mAllTestsPassed = true;
//Synthetic comment -- @@ -289,7 +329,8 @@

public void testFailed(TestFailure status, TestIdentifier test,
String trace) {
            Log.logAndDisplay(LogLevel.WARN, LOG_TAG, String.format("%s#%s failed: %s",
                    test.getClassName(),
test.getTestName(), trace));
mAllTestsPassed = false;
}
//Synthetic comment -- @@ -299,7 +340,8 @@
}

public void testRunFailed(String errorMessage) {
            Log.logAndDisplay(LogLevel.WARN, LOG_TAG, String.format("test run failed: %s",
                    errorMessage));
mAllTestsPassed = false;
mTestRunErrorMessage = errorMessage;
}








//Synthetic comment -- diff --git a/tests/appsecurity-tests/test-apps/AppWithData/src/com/android/cts/appwithdata/CreatePrivateDataTest.java b/tests/appsecurity-tests/test-apps/AppWithData/src/com/android/cts/appwithdata/CreatePrivateDataTest.java
//Synthetic comment -- index de8cb78..d77a872 100644

//Synthetic comment -- @@ -44,5 +44,14 @@
Context.MODE_PRIVATE);
outputStream.write("file contents".getBytes());
outputStream.close();
        assertTrue(getContext().getFileStreamPath(PRIVATE_FILE_NAME).exists());
    }

    /**
     * Check to ensure the private file created in testCreatePrivateData does not exist.
     * Used to check that uninstall of an app deletes the app's data.
     */
    public void testEnsurePrivateDataNotExist() throws IOException {
        assertFalse(getContext().getFileStreamPath(PRIVATE_FILE_NAME).exists());
}
}








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DialogTest.java b/tests/tests/app/src/android/app/cts/DialogTest.java
//Synthetic comment -- index 6a48358..77c55fb 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import android.os.Looper;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
//Synthetic comment -- @@ -60,6 +61,7 @@
private static final float MOTION_X = -20.0f;
private static final float MOTION_Y = -20.0f;
private static final String STUB_ACTIVITY_PACKAGE = "com.android.cts.stub";
    private static final long TEST_TIMEOUT = 1000L;

/**
*  please refer to Dialog
//Synthetic comment -- @@ -696,7 +698,12 @@
});
mInstrumentation.waitForIdleSync();

        // Wait until TestDialog#OnWindowFocusChanged() is called
        new DelayedCheck(TEST_TIMEOUT) {
            protected boolean check() {
                return d.isOnWindowFocusChangedCalled;
            }
        }.run();
}

@TestTargets({








//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioTrackTest.java b/tests/tests/media/src/android/media/cts/AudioTrackTest.java
//Synthetic comment -- index 4adc582..0316ef9 100644

//Synthetic comment -- @@ -394,8 +394,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -453,8 +453,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -519,8 +519,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -586,8 +586,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -661,8 +661,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -728,8 +728,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -790,8 +790,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------

track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -848,8 +848,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -964,8 +964,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
int outputSR = AudioTrack.getNativeOutputSampleRate(TEST_STREAM_TYPE);
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -1028,8 +1028,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 1693cd3..57ce365 100755

//Synthetic comment -- @@ -65,6 +65,18 @@
"45008"     // KT Mobility
);

    // List of network operators that doesn't support Data(binary) SMS message
    private static final List<String> UNSUPPORT_DATA_SMS_MESSAGES =
            Arrays.asList(
                    "44010"    // NTT DOCOMO
            );

    // List of network operators that doesn't support Maltipart SMS message
    private static final List<String> UNSUPPORT_MULTIPART_SMS_MESSAGES =
            Arrays.asList(
                    "44010"    // NTT DOCOMO
            );

private TelephonyManager mTelephonyManager;
private PackageManager mPackageManager;
private String mDestAddr;
//Synthetic comment -- @@ -137,6 +149,8 @@
return;
}

        String mccmnc = mTelephonyManager.getSimOperator();

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);

//Synthetic comment -- @@ -163,30 +177,40 @@
}

// send data sms
        if (!UNSUPPORT_DATA_SMS_MESSAGES.contains(mccmnc)) {
            byte[] data = mText.getBytes();
            short port = 19989;

            init();
            sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
            assertTrue(mSendReceiver.waitForCalls(1, TIME_OUT));
            if (mDeliveryReportSupported) {
                assertTrue(mDeliveryReceiver.waitForCalls(1, TIME_OUT));
            }
        } else {
            // This GSM network doesn't support Data(binary) SMS message.
            // Skip the test.
}

// send multi parts text sms
        if (!UNSUPPORT_MULTIPART_SMS_MESSAGES.contains(mccmnc)) {
            init();
            ArrayList<String> parts = divideMessage(LONG_TEXT);
            int numParts = parts.size();
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
            for (int i = 0; i < numParts; i++) {
                sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, mSendIntent, 0));
                deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
            }
            sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
            assertTrue(mSendReceiver.waitForCalls(numParts, TIME_OUT));
            if (mDeliveryReportSupported) {
              assertTrue(mDeliveryReceiver.waitForCalls(numParts, TIME_OUT));
            }
        } else {
            // This GSM network doesn't support Multipart SMS message.
            // Skip the test.
}
}









//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/VelocityTrackerTest.java b/tests/tests/view/src/android/view/cts/VelocityTrackerTest.java
//Synthetic comment -- index 3ba3a9b..35254b6 100644

//Synthetic comment -- @@ -96,10 +96,10 @@
VelocityTracker vt = VelocityTracker.obtain();
assertNotNull(vt);

        MotionEvent me = MotionEvent.obtain(0L, 10, 1, .0f, .0f, 0);

vt.clear();
        me.addBatch(20L, 20, 20, .0f, .0f, 0);
vt.addMovement(me);
vt.computeCurrentVelocity(1);
XVelocity = 2.0f;
//Synthetic comment -- @@ -112,7 +112,7 @@
assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);

        for (int i = 30; i < 100; i += 10) {
me.addBatch((long)i, (float)i, (float)i, .0f, .0f, 0);
}
vt.clear();
//Synthetic comment -- @@ -124,7 +124,7 @@
assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);

vt.clear();
        me.addBatch(100L, 100, 100, .0f, .0f, 0);
vt.addMovement(me);
vt.computeCurrentVelocity(1);
XVelocity = 1.1562872f;








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 91242cf..87b3eae 100644

//Synthetic comment -- @@ -2257,8 +2257,14 @@
assertFalse(view.hasWindowFocus());

// mAttachInfo is not null
        final View view2 = mActivity.findViewById(R.id.fit_windows);
        // Wait until the window has been focused.
        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return view2.hasWindowFocus();
            }
        }.run();
}

@TestTargetNew(
//Synthetic comment -- @@ -4422,6 +4428,14 @@

viewGroup.addView(editText);
editText.requestFocus();

        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return editText.isFocused();
            }
        }.run();

imm.showSoftInput(editText, 0);
assertTrue(editText.hasCalledOnCreateInputConnection());
assertTrue(editText.hasCalledOnCheckIsTextEditor());







