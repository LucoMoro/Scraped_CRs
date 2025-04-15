/*Fix memory leak after TextView.setText

When TextView.setText was called on a TextView that the user
previously touched the mInsertionPointCursorController and
mSelectionModifierCursorController was set to null without first
calling ViewTreeObserver.removeOnTouchModeChangeListener. This caused
the ViewTreeObserver.mOnTouchModeChangeListeners to grow when the
user pressed the updated TextView. In some cases this also lead to a
larger memory leak where the TextView held a reference to a
LinearLayout through mParent.

Change-Id:I8c6042706f259c86e468f828652dc769c83d967c*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 97b05af..09fe2da 100644

//Synthetic comment -- @@ -6935,13 +6935,25 @@
mLayout != null;

if (!mInsertionControllerEnabled) {
            mInsertionPointCursorController = null;
}

if (!mSelectionControllerEnabled) {
// Stop selection mode if the controller becomes unavailable.
stopTextSelectionMode();
            mSelectionModifierCursorController = null;
}
}








