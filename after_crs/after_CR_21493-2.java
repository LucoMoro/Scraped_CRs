/*Merge remote branch 'korg/froyo' into froyomerge

Conflicts:
	tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
	tests/tests/view/src/android/view/cts/WindowTest.java

Change-Id:Ibf7651011e4e594d0173c90f6341f75a9e71d45b*/




//Synthetic comment -- diff --git a/tests/src/android/view/cts/WindowStubActivity.java b/tests/src/android/view/cts/WindowStubActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 24a971f..502c947

//Synthetic comment -- @@ -39,8 +39,8 @@

@Override
public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Quit").setShortcut('1', 'q');
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Action").setShortcut('2', 'a');
mIsOnCreateOptionsMenuCalled = true;
return super.onCreateOptionsMenu(menu);
}








//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index a9e1c33..ead3d62 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
public class MediaRecorderTest extends ActivityInstrumentationTestCase2<MediaStubActivity> {

private final String OUTPUT_PATH;
    private final String OUTPUT_PATH2;
private static final int RECORD_TIME = 3000;
private static final int VIDEO_WIDTH = 176;
private static final int VIDEO_HEIGHT = 144;
//Synthetic comment -- @@ -46,6 +47,7 @@
private boolean mOnInfoCalled;
private boolean mOnErrorCalled;
private File mOutFile;
    private File mOutFile2;
private Camera mCamera;

/*
//Synthetic comment -- @@ -61,11 +63,14 @@
super("com.android.cts.stub", MediaStubActivity.class);
OUTPUT_PATH = new File(Environment.getExternalStorageDirectory(),
"record.out").getAbsolutePath();
        OUTPUT_PATH2 = new File(Environment.getExternalStorageDirectory(),
                "record2.out").getAbsolutePath();
}

@Override
protected void setUp() throws Exception {
mOutFile = new File(OUTPUT_PATH);
        mOutFile2 = new File(OUTPUT_PATH2);
mMediaRecorder.reset();
mMediaRecorder.setOutputFile(OUTPUT_PATH);
mMediaRecorder.setOnInfoListener(new OnInfoListener() {
//Synthetic comment -- @@ -87,6 +92,9 @@
if (mOutFile != null && mOutFile.exists()) {
mOutFile.delete();
}
        if (mOutFile2 != null && mOutFile2.exists()) {
            mOutFile2.delete();
        }
if (mCamera != null)  {
mCamera.release();
mCamera = null;
//Synthetic comment -- @@ -251,15 +259,16 @@
public void testRecorderVideo() throws Exception {
mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setOutputFile(OUTPUT_PATH2);
mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
mMediaRecorder.setVideoFrameRate(FRAME_RATE);
mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
        FileOutputStream fos = new FileOutputStream(OUTPUT_PATH2);
FileDescriptor fd = fos.getFD();
mMediaRecorder.setOutputFile(fd);
long maxFileSize = MAX_FILE_SIZE * 10;
        recordMedia(maxFileSize, mOutFile2);
}

@TestTargets({
//Synthetic comment -- @@ -318,8 +327,9 @@
mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
assertEquals(0, mMediaRecorder.getMaxAmplitude());
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(OUTPUT_PATH);
mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recordMedia(MAX_FILE_SIZE, mOutFile);
}

@TestTargets({
//Synthetic comment -- @@ -432,22 +442,21 @@
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recordMedia(MAX_FILE_SIZE, mOutFile);
// TODO: how can we trigger a recording error?
assertFalse(mOnErrorCalled);
}

    private void recordMedia(long maxFileSize, File outFile) throws Exception {
mMediaRecorder.setMaxFileSize(maxFileSize);
mMediaRecorder.prepare();
mMediaRecorder.start();
Thread.sleep(RECORD_TIME);
mMediaRecorder.stop();
        assertTrue(outFile.exists());
// The max file size is always guaranteed.
// We just make sure that the margin is not too big
        assertTrue(outFile.length() < 1.1 * maxFileSize);
        assertTrue(outFile.length() > 0);
}
}








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index be47953..12cc21e 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -233,7 +231,7 @@
fail("Broadcast receiver waiting for ConnectivityManager interrupted.");
} finally {
mCm.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE,
                    FEATURE_ENABLE_HIPRI);
if (!isWifiConnected) {
mWifiManager.setWifiEnabled(false);
}








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/MultiTapKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/MultiTapKeyListenerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 175047e..d9bf6d9

//Synthetic comment -- @@ -176,8 +176,7 @@
callOnKeyDown(keyListener, KeyEvent.KEYCODE_1, 1);
assertEquals("Hi.", mTextView.getText().toString());

        addSpace();

callOnKeyDown(keyListener, KeyEvent.KEYCODE_2, 2);
assertEquals("Hi. B", mTextView.getText().toString());
//Synthetic comment -- @@ -201,8 +200,7 @@
callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 3);
assertEquals("Hi", mTextView.getText().toString());

        addSpace();

callOnKeyDown(keyListener, KeyEvent.KEYCODE_2, 2);
assertEquals("Hi B", mTextView.getText().toString());
//Synthetic comment -- @@ -245,6 +243,15 @@
}
}

    private void addSpace() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.append(" ");
            }
        });
        mInstrumentation.waitForIdleSync();
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getInstance",








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/PasswordTransformationMethodTest.java b/tests/tests/text/src/android/text/method/cts/PasswordTransformationMethodTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6058cdd..ed7be70

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.animation.cts.DelayedCheck;
import android.widget.Button;
//Synthetic comment -- @@ -145,7 +146,16 @@
});

mMethod.reset();
        // 12-key support
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // "HELLO" in case of 12-key(NUMERIC) keyboard
            sendKeys("6*4 6*3 7*5 DPAD_RIGHT 7*5 7*6 DPAD_RIGHT");
        }
        else {
            sendKeys("H E 2*L O");
        }
assertTrue(mMethod.hasCalledBeforeTextChanged());
assertTrue(mMethod.hasCalledOnTextChanged());
assertTrue(mMethod.hasCalledAfterTextChanged());








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index e07e105..91242cf 100644

//Synthetic comment -- @@ -3926,6 +3926,7 @@
fitWindowsView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertTrue(mockView.isFocusableInTouchMode());
assertFalse(fitWindowsView.isFocusableInTouchMode());
assertTrue(mockView.isFocusable());
//Synthetic comment -- @@ -3943,12 +3944,14 @@
mockView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertTrue(mockView.isFocused());
runTestOnUiThread(new Runnable() {
public void run() {
fitWindowsView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertFalse(fitWindowsView.isFocused());
assertTrue(mockView.isInTouchMode());
assertTrue(fitWindowsView.isInTouchMode());
//Synthetic comment -- @@ -3962,6 +3965,7 @@
fitWindowsView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertFalse(mockView.isFocused());
assertTrue(fitWindowsView.isFocused());
assertFalse(mockView.isInTouchMode());








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/WindowTest.java b/tests/tests/view/src/android/view/cts/WindowTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0e31888..88edecf

//Synthetic comment -- @@ -40,6 +40,7 @@
import android.view.accessibility.AccessibilityEvent;
import android.view.Gravity;
import android.view.InputQueue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
//Synthetic comment -- @@ -580,10 +581,19 @@
}
});
mInstrumentation.waitForIdleSync();
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            assertTrue(mWindow.isShortcutKey(KeyEvent.KEYCODE_1, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_1)));
            assertFalse(mWindow.isShortcutKey(KeyEvent.KEYCODE_5, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_5)));
        } else {
            assertTrue(mWindow.isShortcutKey(KeyEvent.KEYCODE_Q, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_Q)));
            assertFalse(mWindow.isShortcutKey(KeyEvent.KEYCODE_F, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_F)));
        }
}

@TestTargets({








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/inputmethod/cts/BaseInputConnectionTest.java b/tests/tests/view/src/android/view/inputmethod/cts/BaseInputConnectionTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index f5f2286..1ec2003

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
//Synthetic comment -- @@ -325,7 +326,17 @@
args = {KeyEvent.class}
)
public void testSendKeyEvent() {
        // 12-key support
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // 'Q' in case of 12-key(NUMERIC) keyboard
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7));
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7));
        }
        else {
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Q));
        }
new DelayedCheck() {
@Override
protected boolean check() {








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CacheManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CacheManagerTest.java
//Synthetic comment -- index 3e9a073..5a62c9b 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

@TestTargetClass(android.webkit.CacheManager.class)
public class CacheManagerTest extends ActivityInstrumentationTestCase2<WebViewStubActivity> {
    private static final long CACHEMANAGER_INIT_TIMEOUT = 5000l;
private static final long NETWORK_OPERATION_DELAY = 10000l;

private WebView mWebView;
//Synthetic comment -- @@ -96,6 +97,14 @@
mWebServer = new CtsTestServer(getActivity());
final String url = mWebServer.getAssetUrl(TestHtmlConstants.EMBEDDED_IMG_URL);

        // Wait for CacheManager#init() finish.
        new DelayedCheck(CACHEMANAGER_INIT_TIMEOUT) {
            @Override
            protected boolean check() {
                return CacheManager.getCacheFileBaseDir() != null;
            }
        }.run();

mWebView.clearCache(true);
new DelayedCheck(NETWORK_OPERATION_DELAY) {
@Override








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AutoCompleteTextViewTest.java b/tests/tests/widget/src/android/widget/cts/AutoCompleteTextViewTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index a8c9ae0..40e1c74

//Synthetic comment -- @@ -29,6 +29,7 @@
import android.test.UiThreadTest;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -64,6 +65,7 @@
/** The m instrumentation. */
private Instrumentation mInstrumentation;
private AutoCompleteTextView mAutoCompleteTextView;
    private boolean mNumeric = false;
ArrayAdapter<String> mAdapter;
private final String[] WORDS = new String[] { "testOne", "testTwo", "testThree", "testFour" };
boolean isOnFilterComplete = false;
//Synthetic comment -- @@ -95,6 +97,11 @@
.findViewById(R.id.autocompletetv_edit);
mAdapter = new ArrayAdapter<String>(mActivity,
android.R.layout.simple_dropdown_item_1line, WORDS);
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            mNumeric = true;
        }
}

@TestTargets({
//Synthetic comment -- @@ -501,7 +508,13 @@

inflatePopup();
assertTrue(mAutoCompleteTextView.isPopupShowing());
        String testString = "";
        if (mNumeric) {
            // "tes" in case of 12-key(NUMERIC) keyboard
            testString = "8337777";
        } else {
            testString = "tes";
        }
// Test the filter if the input string is not long enough to threshold
runTestOnUiThread(new Runnable() {
public void run() {
//Synthetic comment -- @@ -517,7 +530,12 @@

inflatePopup();
assertTrue(mAutoCompleteTextView.isPopupShowing());
        if (mNumeric) {
            // "that" in case of 12-key(NUMERIC) keyboard
            testString = "84428";
        } else {
            testString = "that";
        }
mInstrumentation.sendStringSync(testString);
assertFalse(mAutoCompleteTextView.isPopupShowing());

//Synthetic comment -- @@ -529,7 +547,12 @@
mAutoCompleteTextView.setText("");
}
});
        if (mNumeric) {
            // "test" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("83377778");
        } else {
            mInstrumentation.sendStringSync("test");
        }
assertTrue(mAutoCompleteTextView.hasFocus());
assertTrue(mAutoCompleteTextView.hasWindowFocus());
// give some time for UI to settle
//Synthetic comment -- @@ -594,10 +617,18 @@

// performFiltering will be indirectly invoked by onKeyDown
assertNull(filter.getResult());
        // 12-key support
        if (mNumeric) {
            // "numeric" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("6688633777444222");
            Thread.sleep(100);
            assertEquals("numeric", filter.getResult());
        } else {
            mInstrumentation.sendStringSync(STRING_TEST);
            // give some time for UI to settle
            Thread.sleep(100);
            assertEquals(STRING_TEST, filter.getResult());
        }
}

@TestTargets({








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/DialerFilterTest.java b/tests/tests/widget/src/android/widget/cts/DialerFilterTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0b436b2..661d257

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.widget.DialerFilter;
import android.widget.EditText;
//Synthetic comment -- @@ -129,7 +130,16 @@
});
mInstrumentation.waitForIdleSync();

        // 12-key support
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // "adg" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("234");
        }
        else {
            mInstrumentation.sendStringSync("adg");
        }
assertEquals("ADG", mDialerFilter.getLetters().toString());
assertEquals("", mDialerFilter.getDigits().toString());

//Synthetic comment -- @@ -141,7 +151,14 @@
});
mInstrumentation.waitForIdleSync();

        // 12-key support
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // "adg" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("234");
        }
        else {
            mInstrumentation.sendStringSync("adg");
        }
assertEquals("ADG", mDialerFilter.getLetters().toString());
// A, D, K may map to numbers on some keyboards. Don't test.








