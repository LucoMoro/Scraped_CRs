/*Limit the search for siblings to media windows.

A previous patch allowed skipping over multipli SurfaceView windows
when searching for a suitable position for the wallpaper window.
However the patch was too generic, and introduced some realy rare
sideeffects (sometimes the wallpaper was hidden behind another
application).

This fix improves the previous patch by making sure the previous patch
applies only to the SurfaceView use-cases.

Previous patch:https://review.source.android.com/#change,16987Change-Id:I3b1844e0fb30c4928cf7d1a455d41de3a15ae743*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 5b236a7..cee30b9 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import static android.view.WindowManager.LayoutParams.TYPE_INPUT_METHOD;
import static android.view.WindowManager.LayoutParams.TYPE_INPUT_METHOD_DIALOG;
import static android.view.WindowManager.LayoutParams.TYPE_WALLPAPER;

import com.android.internal.app.IBatteryStats;
import com.android.internal.policy.PolicyManager;
//Synthetic comment -- @@ -1481,7 +1482,8 @@
WindowState wb = localmWindows.get(foundI-1);
if (wb.mBaseLayer < maxLayer &&
wb.mAttachedWindow != foundW &&
                        wb.mAttachedWindow != foundW.mAttachedWindow &&
(wb.mAttrs.type != TYPE_APPLICATION_STARTING ||
wb.mToken != foundW.mToken)) {
// This window is not related to the previous one in any







