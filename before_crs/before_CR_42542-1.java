/*Added a timout to avoid accelerating a fling unintentionally

Checking the time since getDownTime to avoid accelerating the
fling if the user stopped scrolling and starts again without
lifting the finger.

Change-Id:I70fa0ebc2b555148ba5be29dfa721bd2e7b7ccec*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index 84a6129..dd74295 100644

//Synthetic comment -- @@ -885,6 +885,8 @@
private static final int MIN_FLING_TIME = 250;
// draw unfiltered after drag is held without movement
private static final int MOTIONLESS_TIME = 100;
// The amount of content to overlap between two screens when going through
// pages with the space bar, in pixels.
private static final int PAGE_SCROLL_OVERLAP = 24;
//Synthetic comment -- @@ -6050,6 +6052,11 @@
case TOUCH_DRAG_LAYER_MODE:
case TOUCH_DRAG_TEXT_MODE:
mPrivateHandler.removeMessages(DRAG_HELD_MOTIONLESS);
// if the user waits a while w/o moving before the
// up, we don't want to do a fling
if (eventTime - mLastTouchTime <= MIN_FLING_TIME) {







