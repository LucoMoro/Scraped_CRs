/*Fix Broken VideoViewTest

Bug 3188260

Fixed a couple conditions that were incorrect after inspecting the
VideoView code...but strangely the comments were correct...weird...

Change-Id:I2eace736b17d0b3dca6b054981eb0da1c6b8800a*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/VideoViewTest.java b/tests/tests/widget/src/android/widget/cts/VideoViewTest.java
//Synthetic comment -- index 00db05f..8e1ffec 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.Activity;
import android.app.Instrumentation;
//Synthetic comment -- @@ -462,9 +461,6 @@
method = "onKeyDown",
args = {int.class, android.view.KeyEvent.class}
)
public void testOnKeyDown() throws Throwable {
runTestOnUiThread(new Runnable() {
public void run() {
//Synthetic comment -- @@ -480,7 +476,7 @@
new DelayedCheck(TIME_OUT) {
@Override
protected boolean check() {
                return mVideoView.isPlaying();
}
}.run();
assertFalse(mMediaController.isShowing());
//Synthetic comment -- @@ -493,7 +489,7 @@
}
}.run();
// MediaController should show
        assertTrue(mMediaController.isShowing());

runTestOnUiThread(new Runnable() {
public void run() {







