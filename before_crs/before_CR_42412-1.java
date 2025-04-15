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

Change-Id:Id971dbcedfbedb5b0e56cba4076cd724d590ac85*/
//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index ebfd56f..ec7ff48 100644

//Synthetic comment -- @@ -860,11 +860,6 @@
public void onPageFinished(Tab tab) {
mCrashRecoveryHandler.backupState();
mUi.onTabDataChanged(tab);
        // pause the WebView timer and release the wake lock if it is finished
        // while BrowserActivity is in pause state.
        if (mActivityPaused && pauseWebViewTimers(tab)) {
            releaseWakeLock();
        }

// Performance probe
if (false) {
//Synthetic comment -- @@ -889,6 +884,10 @@
// onPageFinished has executed)
if (tab.inPageLoad()) {
updateInLoadMenuItems(mCachedMenu, tab);
}
if (!tab.isPrivateBrowsingEnabled()
&& !TextUtils.isEmpty(tab.getUrl())







