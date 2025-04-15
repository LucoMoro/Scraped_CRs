/*Remove Broken Widget Tests

Bug 3188260

Change-Id:Icbc5700430d1575d601efa0013eabcc7969da267*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GalleryTest.java b/tests/tests/widget/src/android/widget/cts/GalleryTest.java
//Synthetic comment -- index e95deb7..aea178a 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.cts.stub.R;
import com.android.internal.view.menu.ContextMenuBuilder;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -34,7 +33,6 @@
import android.content.Context;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.AttributeSet;
//Synthetic comment -- @@ -133,70 +131,6 @@
}
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setCallbackDuringFling",
            args = {boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onTouchEvent",
            args = {android.view.MotionEvent.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onFling",
            args = {MotionEvent.class, MotionEvent.class, float.class, float.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "onDown",
            args = {android.view.MotionEvent.class}
        )
    })
    @BrokenTest("listener.isItemSelected() is false, need to investigate")
    public void testSetCallbackDuringFling() {
        MockOnItemSelectedListener listener = new MockOnItemSelectedListener();
        mGallery.setOnItemSelectedListener(listener);

        mGallery.setCallbackDuringFling(true);

        int[] xy = new int[2];
        getSelectedViewCenter(mGallery, xy);

        // This drags over only one item.
        TouchUtils.drag(this, xy[0], 0, xy[1], xy[1], 1);

        listener.reset();
        // This will drags over several items.
        TouchUtils.drag(this, xy[0], 0, xy[1], xy[1], 1);

        assertTrue(listener.isItemSelected());
        // onItemSelected called more than once
        assertTrue(listener.getItemSelectedCalledCount() > 1);

        listener.reset();
        mGallery.setCallbackDuringFling(false);

        TouchUtils.drag(this, xy[0], 240, xy[1], xy[1], 1);

        assertTrue(listener.isItemSelected());
        // onItemSelected called only once
        assertTrue(listener.getItemSelectedCalledCount() == 1);
    }

    private void getSelectedViewCenter(Gallery gallery, int[] xy) {
        View v = gallery.getSelectedView();
        v.getLocationOnScreen(xy);

        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();

        xy[1] += viewHeight / 2;
        xy[0] += viewWidth / 2;
    }

@TestTargetNew(
level = TestLevel.NOT_FEASIBLE,
method = "setAnimationDuration",








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TableLayoutTest.java b/tests/tests/widget/src/android/widget/cts/TableLayoutTest.java
//Synthetic comment -- index 258883d..8214102 100644

//Synthetic comment -- @@ -16,6 +16,14 @@

package android.widget.cts;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.test.ActivityInstrumentationTestCase2;
//Synthetic comment -- @@ -33,15 +41,6 @@
import android.widget.TableRow;
import android.widget.TextView;

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.ToBeFixed;

/**
* Test {@link TableLayout}.
*/
//Synthetic comment -- @@ -492,169 +491,6 @@
assertTrue(tableLayout.getChildAt(1).isLayoutRequested());
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "test whether columns are actually shrunk",
            method = "setColumnShrinkable",
            args = {java.lang.Integer.class, java.lang.Boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "test whether columns are actually shrunk",
            method = "setShrinkAllColumns",
            args = {java.lang.Boolean.class}
        )
    })
    @ToBeFixed( bug = "", explanation = "After set a column unable to be shrunk," +
            " the other shrinkable columns are not shrunk more.")
    @BrokenTest("fails consistently")
    public void testColumnShrinkableEffect() {
        final TableStubActivity activity = getActivity();
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                activity.setContentView(com.android.cts.stub.R.layout.table_layout_2);
            }
        });
        getInstrumentation().waitForIdleSync();
        final TableLayout tableLayout =
                (TableLayout) activity.findViewById(com.android.cts.stub.R.id.table2);

        final int columnVirtualIndex0 = 1;
        final int columnVirtualIndex1 = 2;
        final int columnVirtualIndex2 = 4;
        final TextView child0 = (TextView) ((TableRow) tableLayout.getChildAt(0)).getChildAt(0);
        final TextView child1 = (TextView) ((TableRow) tableLayout.getChildAt(0)).getChildAt(1);
        final TextView child2 = (TextView) ((TableRow) tableLayout.getChildAt(0)).getChildAt(2);

        // get the original width of each child.
        int oldWidth0 = child0.getWidth();
        int oldWidth1 = child1.getWidth();
        int oldWidth2 = child2.getWidth();
        child0.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY);
        int orignalWidth0 = child0.getMeasuredWidth();
        // child1 has 2 columns.
        child1.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY);
        TextView column12 = (TextView) ((TableRow) tableLayout.getChildAt(1)).getChildAt(2);
        column12.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY);
        int orignalWidth1 = child1.getMeasuredWidth() + column12.getMeasuredWidth();
        child2.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY);
        int orignalWidth2 = child2.getMeasuredWidth();
        int totalSpace = tableLayout.getWidth() - orignalWidth0
                - orignalWidth1 - orignalWidth2;

        // Test: set column 2 which is the start column for child 1 is able to be shrunk.
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                tableLayout.setColumnShrinkable(columnVirtualIndex1, true);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(oldWidth0 < child0.getWidth());
        assertTrue(oldWidth1 > child1.getWidth());
        assertEquals(oldWidth2, child2.getWidth());
        int extraSpace = totalSpace / 2;
        assertEquals(dropNegative(orignalWidth0 + extraSpace), child0.getWidth());
        assertEquals(dropNegative(orignalWidth1 + extraSpace), child1.getWidth());
        assertEquals(orignalWidth2, child2.getWidth());
        oldWidth0 = child0.getWidth();
        oldWidth1 = child1.getWidth();
        oldWidth2 = child2.getWidth();

        // Test: set column 4 which is the column for child 2 is able to be shrunk.
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                tableLayout.setColumnShrinkable(columnVirtualIndex2, true);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(oldWidth0 < child0.getWidth());
        assertTrue(oldWidth1 < child1.getWidth());
        assertTrue(oldWidth2 > child2.getWidth());
        extraSpace = totalSpace / 3;
        assertEquals(dropNegative(orignalWidth0 + extraSpace), child0.getWidth());
        assertEquals(dropNegative(orignalWidth1 + extraSpace), child1.getWidth());
        assertEquals(dropNegative(orignalWidth2 + extraSpace), child2.getWidth());
        oldWidth0 = child0.getWidth();
        oldWidth1 = child1.getWidth();
        oldWidth2 = child2.getWidth();

        // Test: set column 3 which is the end column for child 1 is able to be shrunk.
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                tableLayout.setColumnShrinkable(columnVirtualIndex1+1, true);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(oldWidth0 < child0.getWidth());
        assertTrue(oldWidth1 > child1.getWidth());
        assertTrue(oldWidth2 < child2.getWidth());
        extraSpace = totalSpace / 4;
        assertEquals(dropNegative(orignalWidth0 + extraSpace), child0.getWidth());
        assertEquals(dropNegative(orignalWidth1 + extraSpace * 2), child1.getWidth());
        assertEquals(dropNegative(orignalWidth2 + extraSpace), child2.getWidth());
        oldWidth0 = child0.getWidth();
        oldWidth1 = child1.getWidth();
        oldWidth2 = child2.getWidth();

        // Test: set column 1 which is the column for child 0 is unable to be shrunk.
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                tableLayout.setColumnShrinkable(columnVirtualIndex0, false);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(oldWidth0 < child0.getWidth());
        // assertTrue(oldWidth1 > column1.getWidth());
        // assertTrue(oldWidth2 > column2.getWidth());
        assertEquals(oldWidth1, child1.getWidth());
        assertEquals(oldWidth2, child2.getWidth());
        // extraSpace = totalSpace / 3;
        extraSpace = totalSpace / 4;
        assertEquals(orignalWidth0, child0.getWidth());
        assertEquals(orignalWidth1 + extraSpace * 2, child1.getWidth());
        assertEquals(orignalWidth2 + extraSpace, child2.getWidth());
        oldWidth0 = child0.getWidth();
        oldWidth1 = child1.getWidth();
        oldWidth2 = child2.getWidth();

        // Test: mark all columns are able to be shrunk.
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                tableLayout.setShrinkAllColumns(true);
                tableLayout.requestLayout();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(oldWidth0 > child0.getWidth());
        assertTrue(oldWidth1 < child1.getWidth());
        assertTrue(oldWidth2 < child2.getWidth());
        extraSpace = totalSpace / 5;
        assertEquals(orignalWidth0 + extraSpace, child0.getWidth());
        assertEquals(orignalWidth1 + extraSpace * 2, child1.getWidth());
        assertEquals(orignalWidth2 + extraSpace, child2.getWidth());
        oldWidth0 = child0.getWidth();
        oldWidth1 = child1.getWidth();
        oldWidth2 = child2.getWidth();

        // Test: Remove the mark for all columns are able to be shrunk.
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                tableLayout.setShrinkAllColumns(false);
                tableLayout.requestLayout();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(oldWidth0 < child0.getWidth());
        assertTrue(oldWidth1 > child1.getWidth());
        assertTrue(oldWidth2 > child2.getWidth());
        // extraSpace = totalSpace / 3;
        extraSpace = totalSpace / 4;
        assertEquals(orignalWidth0, child0.getWidth());
        assertEquals(orignalWidth1 + extraSpace * 2, child1.getWidth());
        assertEquals(orignalWidth2 + extraSpace, child2.getWidth());
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
notes = "Test addView(View child)",








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/VideoViewTest.java b/tests/tests/widget/src/android/widget/cts/VideoViewTest.java
//Synthetic comment -- index 8e1ffec..6b9aa84 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -31,7 +30,6 @@
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View.MeasureSpec;
//Synthetic comment -- @@ -239,142 +237,6 @@
}.run();
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setVideoURI",
            args = {android.net.Uri.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setOnPreparedListener",
            args = {android.media.MediaPlayer.OnPreparedListener.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "isPlaying",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "pause",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "start",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "seekTo",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopPlayback",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCurrentPosition",
            args = {}
        )
    })
    @BrokenTest("Fails in individual mode (current pos > 0 before start)")
    public void testPlayVideo2() throws Throwable {
        final int seekTo = mVideoView.getDuration() >> 1;
        final MockOnPreparedListener listener = new MockOnPreparedListener();
        mVideoView.setOnPreparedListener(listener);

        runTestOnUiThread(new Runnable() {
            public void run() {
                mVideoView.setVideoURI(Uri.parse(mVideoPath));
            }
        });
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return listener.isTriggered();
            }
        }.run();
        assertEquals(0, mVideoView.getCurrentPosition());

        // test start
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
        assertTrue(mVideoView.getCurrentPosition() > 0);

        // test pause
        runTestOnUiThread(new Runnable() {
            public void run() {
                mVideoView.pause();
            }
        });
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return !mVideoView.isPlaying();
            }
        }.run();
        int currentPosition = mVideoView.getCurrentPosition();

        // sleep a second and then check whether player is paused.
        Thread.sleep(OPERATION_INTERVAL);
        assertEquals(currentPosition, mVideoView.getCurrentPosition());

        // test seekTo
        runTestOnUiThread(new Runnable() {
            public void run() {
                mVideoView.seekTo(seekTo);
            }
        });
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return mVideoView.getCurrentPosition() >= seekTo;
            }
        }.run();
        assertFalse(mVideoView.isPlaying());

        // test start again
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
        assertTrue(mVideoView.getCurrentPosition() > seekTo);

        // test stop
        runTestOnUiThread(new Runnable() {
            public void run() {
                mVideoView.stopPlayback();
            }
        });
        new DelayedCheck(TIME_OUT) {
            @Override
            protected boolean check() {
                return !mVideoView.isPlaying();
            }
        }.run();
        assertEquals(0, mVideoView.getCurrentPosition());
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "setOnErrorListener",







