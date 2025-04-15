/*Selection cursors is not reversed and crash is prevented.

When a user extends a selection using the keys shift and left arrow
and the selects the word the crusors are no longer reversed. So
the user can no longer cause a crash by dragging the end cursor to
the right if the selection starts at the end of the word.

Change-Id:Ibf9d67fddf864c96b3c38567dc0b2c3956fe6951*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 97b05af..f658bf5 100644

//Synthetic comment -- @@ -8174,8 +8174,14 @@
return;
}

            if (selectionStart > selectionEnd) {
                // Preventing the cursors to be reversed due to extended selection
                mStartHandle.positionAtCursor(selectionEnd, true);
                mEndHandle.positionAtCursor(selectionStart, true);
            } else{
                mStartHandle.positionAtCursor(selectionStart, true);
                mEndHandle.positionAtCursor(selectionEnd, true);
            }
}

public boolean onTouchEvent(MotionEvent event) {







