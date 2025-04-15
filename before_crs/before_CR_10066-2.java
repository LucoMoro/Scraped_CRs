/*Add navigation hot keys for browser (like firefox & IE)

PageUp / PageDown
Alt-Left/Right  - go back/forward a page*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 2892051..2bb69bcd 100644

//Synthetic comment -- @@ -3208,6 +3208,24 @@
nativeClearFocus(contentX, contentY);
}

if (keyCode >= KeyEvent.KEYCODE_DPAD_UP
&& keyCode <= KeyEvent.KEYCODE_DPAD_RIGHT) {
// always handle the navigation keys in the UI thread







