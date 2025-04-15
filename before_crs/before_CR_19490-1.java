/*Handle BrowserFrame is null in WebViewCore's contentDraw

WebViewCore's method contentDraw doesn't handle the case
when BrowserFrame (mBrowserFrame) is null. If BrowserFrame
is null and method contentDraw is executed a NullPointerException
will be thrown causing the appplicationto crash.

This patch checks BrowserFrame against null before it's used
in order to avoid a crash.

The problem has not been able to reproduce but it has happened
since found by a live user. The call stack indicates that a timer
has fired in JWebCoreJavaBridge, which has triggered a contentDraw.
In the meantime WebViewCore has been destroyed, which sets
WebViewCore's BrowserFrame to null.

Change-Id:I9aa6262f19993be0f0d79eb0dd0fbfc96d30f3ce*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index 4118119..7376980 100644

//Synthetic comment -- @@ -1860,7 +1860,7 @@
/* package */ void contentDraw() {
// don't update the Picture until we have an initial width and finish
// the first layout
        if (mCurrentViewWidth == 0 || !mBrowserFrame.firstLayoutDone()) {
return;
}
// only fire an event if this is our first request







