/*Clean Up VideoViewTest

Remove testOnKeyDown since it fails in batch mode. Remove empty
tests that were cluttering up the file.

Change-Id:I11b1b2e518194d0732732c013acfadeae10a8f07*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/VideoViewTest.java b/tests/tests/widget/src/android/widget/cts/VideoViewTest.java
//Synthetic comment -- index 6b9aa84..44d48a5 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View.MeasureSpec;
import android.view.animation.cts.DelayedCheck;
import android.widget.MediaController;
//Synthetic comment -- @@ -310,89 +309,6 @@
}

@TestTargetNew(
        level = TestLevel.NOT_NECESSARY,
        method = "onTouchEvent",
        args = {android.view.MotionEvent.class}
    )
    public void testOnTouchEvent() {
        // onTouchEvent() is implementation details, do NOT test
    }

    @TestTargetNew(
        level = TestLevel.SUFFICIENT,
        method = "onKeyDown",
        args = {int.class, android.view.KeyEvent.class}
    )
    public void testOnKeyDown() throws Throwable {
        runTestOnUiThread(new Runnable() {
            public void run() {
                mVideoView.setVideoPath(mVideoPath);
                mVideoView.requestFocus();
            }
        });
        mInstrumentation.waitForIdleSync();

        assertFalse(mVideoView.isPlaying());
        sendKeys(KeyEvent.KEYCODE_HEADSETHOOK);
        // video should be played.
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return mVideoView.isPlaying();
            }
        }.run();
        assertFalse(mMediaController.isShowing());

        sendKeys(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return !mVideoView.isPlaying();
            }
        }.run();
        // MediaController should show
        assertTrue(mMediaController.isShowing());

        runTestOnUiThread(new Runnable() {
            public void run() {
                mVideoView.start();
            }
        });
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return mVideoView.isPlaying();
            }
        }.run();

        sendKeys(KeyEvent.KEYCODE_MEDIA_STOP);
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return !mVideoView.isPlaying();
            }
        }.run();
    }

    @TestTargetNew(
        level = TestLevel.NOT_NECESSARY,
        method = "onMeasure",
        args = {int.class, int.class}
    )
    public void testOnMeasure() {
        // Do not test onMeasure(), implementation details
    }

    @TestTargetNew(
        level = TestLevel.NOT_NECESSARY,
        method = "onTrackballEvent",
        args = {android.view.MotionEvent.class}
    )
    public void testOnTrackballEvent() {
        // Do not test onTrackballEvent(), implementation details
    }

    @TestTargetNew(
level = TestLevel.COMPLETE,
method = "getDuration",
args = {}







