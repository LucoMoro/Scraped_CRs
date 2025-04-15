/*Fix ProgressBarTest

Bug 3188260

- Remove broken testAccessInterpolatorContext, because it was doing the
  same thing as the test above it.
- Change references to com.android.internal to android...

Change-Id:Id3698e7a17463f2c21c6f4afe7e87bbd00e4986a*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ProgressBarTest.java b/tests/tests/widget/src/android/widget/cts/ProgressBarTest.java
//Synthetic comment -- index 4c5c717..1b9b517 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -26,7 +25,6 @@
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
//Synthetic comment -- @@ -34,7 +32,6 @@
import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
//Synthetic comment -- @@ -72,7 +69,7 @@

new ProgressBar(mContext, null);

        new ProgressBar(mContext, null, com.android.internal.R.attr.progressBarStyle);
}

@TestTargets({
//Synthetic comment -- @@ -98,8 +95,7 @@
// because default is Indeterminate only progressBar, can't change the status
assertTrue(progressBar.isIndeterminate());

        progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);
assertFalse(progressBar.isIndeterminate());

progressBar.setIndeterminate(true);
//Synthetic comment -- @@ -152,7 +148,7 @@
})
public void testAccessProgressDrawable() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                        com.android.internal.R.attr.progressBarStyleHorizontal);

// set ProgressDrawable
// normal value
//Synthetic comment -- @@ -182,7 +178,7 @@
})
public void testAccessProgress() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);
assertEquals(0, progressBar.getProgress());

final int maxProgress = progressBar.getMax();
//Synthetic comment -- @@ -221,7 +217,7 @@
})
public void testAccessSecondaryProgress() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);
assertEquals(0, progressBar.getSecondaryProgress());

final int maxProgress = progressBar.getMax();
//Synthetic comment -- @@ -253,7 +249,7 @@
)
public void testIncrementProgressBy() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);

// normal value
int increment = 1;
//Synthetic comment -- @@ -283,7 +279,7 @@
)
public void testIncrementSecondaryProgressBy() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);

// normal value
int increment = 1;
//Synthetic comment -- @@ -319,8 +315,7 @@
)
})
public void testAccessInterpolator() {
        ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyle);

// default should be LinearInterpolator
assertTrue(progressBar.getInterpolator() instanceof LinearInterpolator);
//Synthetic comment -- @@ -329,54 +324,6 @@
Interpolator i = new AccelerateDecelerateInterpolator();
progressBar.setInterpolator(i);
assertEquals(i, progressBar.getInterpolator());

        // exceptional value
        progressBar.setInterpolator(null);
        assertNull(progressBar.getInterpolator());

        // TODO: test whether setInterpolator takes effect? How to get the animation?
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setInterpolator",
            args = {android.content.Context.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInterpolator",
            args = {}
        )
    })
    @ToBeFixed(bug = "1695243", explanation = "the javadoc for setInterpolator() is incomplete." +
            "1. not clear what is supposed to happen if context or resID is exceptional.")
    @BrokenTest("Initial setInterpolator() call occasionally fails with NPE. context null?")
    public void testAccessInterpolatorContext() {
        ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyle);

        // default is LinearInterpolator
        assertTrue(progressBar.getInterpolator() instanceof LinearInterpolator);

        // normal value
        progressBar.setInterpolator(mContext.getApplicationContext(), R.anim.move_cycle);
        assertTrue(progressBar.getInterpolator() instanceof CycleInterpolator);

        // exceptional value
        try {
            progressBar.setInterpolator(null, R.anim.move_ani);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // issue 1695243, not clear what is supposed to happen if context is null.
        }

        try {
            progressBar.setInterpolator(mContext.getApplicationContext(), -1);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
            // issue 1695243, not clear what is supposed to happen if resID is exceptional.
        }
}

@TestTargetNew(
//Synthetic comment -- @@ -388,7 +335,7 @@
"1. not clear what is supposed result if visibility isn't VISIBLE, INVISIBLE or GONE.")
public void testSetVisibility() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);

// set visibility
// normal value
//Synthetic comment -- @@ -472,7 +419,7 @@
})
public void testAccessMax() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);

// set Progress
int progress = 10;
//Synthetic comment -- @@ -612,7 +559,7 @@
})
public void testOnSaveAndRestoreInstanceState() {
ProgressBar progressBar = new ProgressBar(mContext, null,
                com.android.internal.R.attr.progressBarStyleHorizontal);
int oldProgress = 1;
int oldSecondaryProgress = progressBar.getMax() - 1;
progressBar.setProgress(oldProgress);







