/*Add navigation hot keys for browser (like firefox & IE)

PageUp / PageDown
Alt-Left/Right  - go back/forward a page*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 4fc4e5b..1d7374b 100644

//Synthetic comment -- @@ -3153,6 +3153,24 @@
nativeClearFocus(contentX, contentY);
}

        if (event.isAltPressed()) {
           if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
              goBack();
              return true;
           } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
              goForward();
              return true;
           }
        }
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            pageUp(false);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            pageDown(false);
            return true;
        }

if (keyCode >= KeyEvent.KEYCODE_DPAD_UP
&& keyCode <= KeyEvent.KEYCODE_DPAD_RIGHT) {
// always handle the navigation keys in the UI thread







