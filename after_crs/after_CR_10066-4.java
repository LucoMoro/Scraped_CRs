/*Add PageUp and PageDown hot keys

Change-Id:I588f70d74bc5c02e1fb60c7a2821d8801f2333e0*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index de450fc..f5886f3 100644

//Synthetic comment -- @@ -3315,6 +3315,16 @@
nativeHideCursor();
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







