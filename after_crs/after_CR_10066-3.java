/*Add navigation hot keys for browser (like firefox & IE)

PageUp / PageDown
Alt-Left/Right  - go back/forward a page

Change-Id:Iec5e2868c3eee4cbb1531a5f06b391774e26e22f*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index c5c14d3..73a2e7f 100644

//Synthetic comment -- @@ -3297,6 +3297,24 @@
nativeHideCursor();
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







