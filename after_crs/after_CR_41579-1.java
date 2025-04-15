/*Settings: Fix crash seen when setting the radio band

When the user presses back before the set radio band
result dialog is shown, crash is seen as the activity
associated with this is already finishing.

Check the activity status before displaying the
set radio band result alert dialog.

Change-Id:I34104773f5cce0be6d0021823b2889aad0fdb945Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39317*/




//Synthetic comment -- diff --git a/src/com/android/settings/BandMode.java b/src/com/android/settings/BandMode.java
//Synthetic comment -- index 4c7663e..0a0f77f 100644

//Synthetic comment -- @@ -207,7 +207,9 @@
Window.FEATURE_INDETERMINATE_PROGRESS,
Window.PROGRESS_VISIBILITY_OFF);

                    if (!isFinishing()) {
                        displayBandSelectionResult(ar.exception);
                    }
break;
}
}







