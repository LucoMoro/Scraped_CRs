/*Pause WebView timer when Browser is paused

If the Browser is paused while loading a page it
shall finish loading the page before the WebView
timer is paused.

The WebView timer shall be paused in onProgressChanged
when the progress has reached 100 not from onPageFinished
since onPageFinished is called when the main frame is loaded
and at that time subframe and other resources might haven't
been finished loaded i.e. the page is still in load, which the
result in WebView timers are not paused.

With this patch the Browser won't consume system resources
if it's minimized while doing a page load.

Change-Id:I3957aec505ac32c0b16cdbe74ace0ff597b23195*/




//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index 8238d77..b3b3f85 100644

//Synthetic comment -- @@ -844,11 +844,6 @@
}
}
}

// Performance probe
if (false) {
//Synthetic comment -- @@ -874,6 +869,11 @@
// onPageFinished has executed)
if (tab.inPageLoad()) {
updateInLoadMenuItems(mCachedMenu, tab);
            } else if (mActivityPaused && pauseWebViewTimers(tab)) {
                // pause the WebView timer and release the wake lock if it is
                // finished
                // while BrowserActivity is in pause state.
                releaseWakeLock();
}
} else {
if (!tab.inPageLoad()) {







