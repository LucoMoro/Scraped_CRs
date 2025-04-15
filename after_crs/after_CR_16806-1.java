/*Fix a monkey crash when the new WebView is destroyed.

Grab the WebViewCore immediately so that if the Tab is destroyed, we have the
old WebViewCore object and can return the BrowserFrame.

Bug: 2733004
Change-Id:Ic3e4c5417f2165f412f60f05aea3ed403d8cecfd*/




//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 219a469..a2c80f2 100644

//Synthetic comment -- @@ -785,11 +785,7 @@
* @return The BrowserFrame object stored in the new WebView.
*/
private BrowserFrame createWindow(boolean dialog, boolean userGesture) {
        return mCallbackProxy.createWindow(dialog, userGesture);
}

/**








//Synthetic comment -- diff --git a/core/java/android/webkit/CallbackProxy.java b/core/java/android/webkit/CallbackProxy.java
//Synthetic comment -- index 0e0e032..d65c106 100644

//Synthetic comment -- @@ -1098,7 +1098,7 @@
}
}

    public BrowserFrame createWindow(boolean dialog, boolean userGesture) {
// Do an unsynchronized quick check to avoid posting if no callback has
// been set.
if (mWebChromeClient == null) {
//Synthetic comment -- @@ -1122,9 +1122,15 @@

WebView w = transport.getWebView();
if (w != null) {
            WebViewCore core = w.getWebViewCore();
            // If WebView.destroy() has been called, core may be null.  Skip
            // initialization in that case and return null.
            if (core != null) {
                core.initializeSubwindow();
                return core.getBrowserFrame();
            }
}
        return null;
}

public void onRequestFocus() {







