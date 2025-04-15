/*Avoid link being loaded when long pressing x-button

When long pressing a link in browser with the x-button or other
buttons mapped to KEYCODE_DPAD_CENTER the context menu is
displayed but the link is also loaded in the background. When
BrowserActivity loses focus due to the context menu the native
InputDispatcher injects a key event with action=up flag=canceled
which is received in WebViews onKeyUp method. This triggers the link
to be opened even though the context menu is shown.

Adding a check in WebViews onKeyUp method for flag=canceled avoids
the link being loaded.

Change-Id:I02d7ba1d428ac4e37a4d77b61dab2e2ef132067c*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 7f7a25e..cd7d527 100644

//Synthetic comment -- @@ -4348,7 +4348,9 @@
// remove the long press message first
mPrivateHandler.removeMessages(LONG_PRESS_CENTER);
mGotCenterDown = false;

if (mSelectingText) {
if (mExtendSelection) {
copySelection();







