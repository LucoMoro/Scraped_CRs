/*Remove Broken MediaControllerTests

Bug 3188260

These don't seem like they will be reliable even if they are fixed.

Change-Id:Ib2c077a0a98afd7247df7641d33781c2c32d0be6*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/MediaControllerTest.java b/tests/tests/widget/src/android/widget/cts/MediaControllerTest.java
//Synthetic comment -- index 40465ea..9f27138 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -31,11 +30,9 @@
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
//Synthetic comment -- @@ -266,50 +263,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
        notes = "Test {@link MediaController#onTouchEvent(MotionEvent)}, " +
                "this function always returns true",
        method = "onTouchEvent",
        args = {android.view.MotionEvent.class}
    )
    @ToBeFixed(bug = "1559790", explanation = "MediaController does not appear " +
            "when the user touches the anchor view.")
    @BrokenTest("NullPointerException thrown; no stacktrace in result")
    public void testOnTouchEvent() {
        final XmlPullParser parser =
                mActivity.getResources().getXml(R.layout.mediacontroller_layout);
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        mMediaController = new MediaController(mActivity, attrs);
        final MockMediaPlayerControl mediaPlayerControl = new MockMediaPlayerControl();
        mMediaController.setMediaPlayer(mediaPlayerControl);

        final VideoView videoView =
                (VideoView) mActivity.findViewById(R.id.mediacontroller_videoview);
        videoView.setMediaController(mMediaController);
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                videoView.setVideoPath(prepareSampleVideo());
            }
        });
        mInstrumentation.waitForIdleSync();

        assertFalse(mMediaController.isShowing());
        TouchUtils.tapView(this, videoView);
        mInstrumentation.waitForIdleSync();

        // isShowing() should return true, but MediaController still not shows, this may be a bug.
        assertFalse(mMediaController.isShowing());

        // timeout is larger than duration, in case the system is sluggish
        new DelayedCheck(DEFAULT_TIMEOUT + 500) {
            @Override
            protected boolean check() {
                return !mMediaController.isShowing();
            }
        }.run();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
notes = "Test {@link MediaController#onTrackballEvent(MotionEvent)}, " +
"this function always returns false",
method = "onTrackballEvent",
//Synthetic comment -- @@ -351,46 +304,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
        notes = "Test {@link MediaController#dispatchKeyEvent(KeyEvent)}",
        method = "dispatchKeyEvent",
        args = {android.view.KeyEvent.class}
    )
    @ToBeFixed(bug = "1559790", explanation = "MediaController does not appear " +
            "when the user presses a key.")
    @BrokenTest("Fragile test. Passes only occasionally.")
    public void testDispatchKeyEvent() {
        mMediaController = new MediaController(mActivity);
        final MockMediaPlayerControl mediaPlayerControl = new MockMediaPlayerControl();
        mMediaController.setMediaPlayer(mediaPlayerControl);

        final VideoView videoView =
                (VideoView) mActivity.findViewById(R.id.mediacontroller_videoview);
        videoView.setMediaController(mMediaController);
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                videoView.setVideoPath(prepareSampleVideo());
                videoView.requestFocus();
            }
        });
        mInstrumentation.waitForIdleSync();

        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_SPACE);
        mInstrumentation.waitForIdleSync();

        // isShowing() should return true, but MediaController still not shows, this may be a bug.
        assertFalse(mMediaController.isShowing());

        // timeout is larger than duration, in case the system is sluggish
        new DelayedCheck(DEFAULT_TIMEOUT + 500) {
            @Override
            protected boolean check() {
                return !mMediaController.isShowing();
            }
        }.run();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
notes = "Test {@link MediaController#setEnabled(boolean)}",
method = "setEnabled",
args = {boolean.class}







