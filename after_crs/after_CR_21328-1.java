/*Prevent window leak in TextView

The input cursor controller leaks if it is disabled while visible and
the Activity is finished before the input cursor disapears.
This causes an IllegalArgumentException in WindowManagerImpl.findViewLocked
when the time out for the input cursor controller tries to hide itself.

To prevent this, hide the input cursor when disabling it, instead of
just letting it time out to disapear

Change-Id:I149cb99c8d742bf5f552a3fa51c744015f8bab34*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index bdc5014..9accf86 100644

//Synthetic comment -- @@ -6920,6 +6920,7 @@
mLayout != null;

if (!mInsertionControllerEnabled) {
            hideInsertionPointCursorController();
mInsertionPointCursorController = null;
}








