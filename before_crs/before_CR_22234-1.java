/*TextView: don't show context menu for double-tap

Use the mechanism long-press uses to prevent the context menu
from being shown.  hide()ing the selection was not working properly.

Change-Id:Iae68839f01162729d565df5861f37f400bf569d8*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 97b05af..c8fe809 100644

//Synthetic comment -- @@ -8201,8 +8201,8 @@
if (distanceSquared < slopSquared) {
startTextSelectionMode();
// Hacky: onTapUpEvent will open a context menu with cut/copy
                                // Prevent this by hiding handles which will be revived instead.
                                hide();
}
}
mPreviousTapPositionX = x;







