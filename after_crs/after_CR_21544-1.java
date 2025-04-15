/*Not possible to follow links after pinch-zoom in browser

A pinch-zoom may set the browser in drawIsPaused
state which prevent the screen from being updated.

A pinch-zoom starts with a call to startDrag
which calls pauseUpdatePicture which in turn sets
WebView in the drawIsPaused state. The pinch-zoom
ends by invoking onScaleEnd which sets mTouchMode to
TOUCH_PINCH_DRAG. When the gesture finishes
onTouchEvent is called with action MotionEvent.ACTION_UP.
But mTouchMode equals TOUCH_PINCH_DRAG and that case is
not handled so resumeUpdatePicture is never called.
The function resumeUpdatePicture has to be called in
order to reset the drawIsPaused state.

The solution is to call resumeUpdatePicture
in onTouchEvent if mTouchMode is equal to
TOUCH_PINCH_DRAG in the MotionEvent.ACTION_UP case.

Reproduce:
1. Open www.google.com in classic view
2. Use pinch-zoom to zoom-in and zoom-out
3. Release fingers straight up.
4. Click a link to go to a new page. The new page does not appear.
5. Fling or zoom using zoom-buttons to make
the new page be displayed on screen.

Note: In step 3, it is important to release straight up in order
to not trigger a fling.

Change-Id:I4e095de9d4a05e043fbe39e9c665e9f584a723bf*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 88516ce..3a5093a 100644

//Synthetic comment -- @@ -5547,6 +5547,7 @@
mHeldMotionless = MOTIONLESS_TRUE;
invalidate();
// fall through
                    case TOUCH_PINCH_DRAG:
case TOUCH_DRAG_START_MODE:
// TOUCH_DRAG_START_MODE should not happen for the real
// device as we almost certain will get a MOVE. But this







