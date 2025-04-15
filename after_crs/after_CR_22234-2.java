/*TextView: don't show context menu for double-tap

Use the mechanism long-press uses to prevent the context menu
from being shown.  hide()ing the selection was not working properly.

Also, prevent a triple-tap from being seen as two double-tap events.

Change-Id:Iae68839f01162729d565df5861f37f400bf569d8*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 97b05af..a66346f 100644

//Synthetic comment -- @@ -8191,20 +8191,23 @@
mMinTouchOffset = mMaxTouchOffset = getOffset(x, y);

// Double tap detection
                        if (mPreviousTapUpTime != 0) {
                            long duration = SystemClock.uptimeMillis() - mPreviousTapUpTime;
                            if (duration <= ViewConfiguration.getDoubleTapTimeout()) {
                                final int deltaX = x - mPreviousTapPositionX;
                                final int deltaY = y - mPreviousTapPositionY;
                                final int distanceSquared = deltaX * deltaX + deltaY * deltaY;
                                final int doubleTapSlop = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();
                                final int slopSquared = doubleTapSlop * doubleTapSlop;
                                if (distanceSquared < slopSquared) {
                                    startTextSelectionMode();
                                    // Hacky: onTapUpEvent will open a context menu with cut/copy
                                    // Prevent this by eating the event.
                                    mEatTouchRelease = true;
                                }
}
}

mPreviousTapPositionX = x;
mPreviousTapPositionY = y;

//Synthetic comment -- @@ -8221,7 +8224,13 @@
break;

case MotionEvent.ACTION_UP:
                        // If this was a double-tap event, don't consider
                        // the next touch event to be a double-tap.
                        if (mEatTouchRelease) {
                            mPreviousTapUpTime = 0;
                        } else {
                            mPreviousTapUpTime = SystemClock.uptimeMillis();
                        }
break;
}
}







