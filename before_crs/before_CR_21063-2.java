/*Dismiss context menu before removing ExtractEditText view.

When an ExtractEditText context menu is opened from fullscreen input
mode, its view hierarchy is attached to InputMethodService's window.
If the orientation is changed, the menu will loose focus and become
unresponsive. In addition, the "Select Word" option will cause an
exception after orientation change. By dismissing the dialog early,
both problems are avoided.

Change-Id:I3cad2a9fbc9e95ac4d2b4c91ba5810ec987b5084*/
//Synthetic comment -- diff --git a/core/java/android/inputmethodservice/InputMethodService.java b/core/java/android/inputmethodservice/InputMethodService.java
//Synthetic comment -- index 53cdf21..38359428 100644

//Synthetic comment -- @@ -716,6 +716,8 @@
@Override public void onConfigurationChanged(Configuration newConfig) {
super.onConfigurationChanged(newConfig);

boolean visible = mWindowVisible;
int showFlags = mShowInputFlags;
boolean showingInput = mShowInputRequested;







