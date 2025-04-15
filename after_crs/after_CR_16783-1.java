/*Avoid NullPointerException in BrowserFrame

Avoid NullPointerException in BrowserFrame in
method createWindow. getWebViewCore can return
null if WebView has been destroyed. Crash found
in live environment.

Change-Id:I49c3549a037d8f424a0abaee1f8933974b1ec9d7*/




//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 219a469..6544d05 100644

//Synthetic comment -- @@ -787,7 +787,10 @@
private BrowserFrame createWindow(boolean dialog, boolean userGesture) {
WebView w = mCallbackProxy.createWindow(dialog, userGesture);
if (w != null) {
            WebViewCore wvc = w.getWebViewCore();
            if (wvc != null) {
                return wvc.getBrowserFrame();
            }
}
return null;
}







