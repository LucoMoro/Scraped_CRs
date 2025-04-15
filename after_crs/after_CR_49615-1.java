/*TOMBSTONE in com.android.browser

The WebView obj could be destroyed when Tab is destroyed. Which
is weird because there are reference to it in HTML5VideoViewProxy
obj.

We release the HTML5VideoViewProxy reference in WebView destroy
function here.

And add another workaround to prevent HTML5VideoViewProxy calling
native once WebView destroy operation done.

NOTE: we can't set mWebView in HTML5VideoViewProxy to NULL directly
because it's reference everywhere in HTML5VideoViewProxy and that
could trigger NULL exception. We set another boolen member if Webview
is destroyed and use it in HTML5VideoViewProxy.VideoPlayer.setBaseLayer.

Change-Id:Ie223a9cf8ea9b68eb55d63f89c7d8b2ec47bf896Author: Bin Xu <binx.xu@intel.com>
Signed-off-by: Bin Xu <binx.xu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 64237*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index a3d62ae..66bbce6 100644

//Synthetic comment -- @@ -98,6 +98,11 @@

private static boolean isVideoSelfEnded = false;

        private static boolean mWebViewgone = true;
        private static void markWebViewgone(boolean gone) {
            mWebViewgone = gone;
        }

private static void setPlayerBuffering(boolean playerBuffering) {
mHTML5VideoView.setPlayerBuffering(playerBuffering);
}
//Synthetic comment -- @@ -107,7 +112,7 @@
// Otherwise, we may want to delete the Surface Texture to save memory.
public static void setBaseLayer(int layer) {
// Don't do this for full screen mode.
            if (mWebViewgone ==false && mHTML5VideoView != null
&& !mHTML5VideoView.isFullScreenMode()
&& !mHTML5VideoView.isReleased()) {
int currentVideoLayerId = mHTML5VideoView.getVideoLayerId();
//Synthetic comment -- @@ -608,6 +613,9 @@
mNativePointer = nativePtr;
// create the message handler for this thread
createWebCoreHandler();

        // mark WebView available
        VideoPlayer.markWebViewgone(false);
}

private void createWebCoreHandler() {
//Synthetic comment -- @@ -758,6 +766,11 @@
VideoPlayer.setBaseLayer(layer);
}

    // when WebView is destroyed, this function will be invoked.
    public void markWebViewgone(boolean gone) {
        VideoPlayer.markWebViewgone(gone);
    }

public void pauseAndDispatch() {
VideoPlayer.pauseAndDispatch();
}








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index ae56e6b..748d618b 100644

//Synthetic comment -- @@ -2136,6 +2136,21 @@
mAccessibilityInjector.destroy();
mAccessibilityInjector = null;
}
        // If there is Tab in BrowserActiviy, the Tab close will call
        // WebView.destory directly. So we need clear HTML5VideoViewProxy
        // here as well.
        if (mHTML5VideoViewProxy != null) {
            // we can't set mWebView to null in mHTML5VideoViewProxy because it may
            // trigger NULL exception. Instead, we have a new member to track the
            // WebView destroy.
            mHTML5VideoViewProxy.markWebViewgone(true);

            // We remove reference of HTML5VideoViewProxy here.
            // FIXME: add interface to HTML5VideoViewProxy to remove reference
            // of WebView because the WebView object is destroyed already.
            setHTML5VideoViewProxy(null);
        }

if (mWebViewCore != null) {
// Tell WebViewCore to destroy itself
synchronized (this) {
//Synthetic comment -- @@ -2906,6 +2921,9 @@
*/
@Override
public void requestFocusNodeHref(Message hrefMsg) {
        if (mNativeClass == 0) { // in case the WebView.destroy is called.
            return;
        }
if (hrefMsg == null) {
return;
}
//Synthetic comment -- @@ -3132,7 +3150,7 @@
calcOurContentVisibleRect(mVisibleRect);
// Rect.equals() checks for null input.
if (!mVisibleRect.equals(mLastVisibleRectSent)) {
            if (!mBlockWebkitViewMessages && (mNativeClass != 0)) {
mScrollOffset.set(mVisibleRect.left, mVisibleRect.top);
mWebViewCore.removeMessages(EventHub.SET_SCROLL_OFFSET);
mWebViewCore.sendMessage(EventHub.SET_SCROLL_OFFSET,
//Synthetic comment -- @@ -3477,6 +3495,9 @@
*/
@Override
public void onPause() {
        if (mNativeClass == 0)
            return;

if (!mIsPaused) {
mIsPaused = true;
mWebViewCore.sendMessage(EventHub.ON_PAUSE);
//Synthetic comment -- @@ -3868,6 +3889,7 @@
}

private void scrollLayerTo(int x, int y) {
        if (0 == mNativeClass) return; // client isn't initialized
int dx = mScrollingLayerRect.left - x;
int dy = mScrollingLayerRect.top - y;
if ((dx == 0 && dy == 0) || mNativeClass == 0) {
//Synthetic comment -- @@ -4448,6 +4470,7 @@
* Select the word at the indicated content coordinates.
*/
boolean selectText(int x, int y) {
        if (0 == mNativeClass) return false; // client isn't initialized
if (mWebViewCore == null) {
return false;
}
//Synthetic comment -- @@ -4640,6 +4663,7 @@
}

private void drawTextSelectionHandles(Canvas canvas) {
        if (0 == mNativeClass) return; // client isn't initialized
if (mBaseAlpha.getAlpha() == 0 && mExtentAlpha.getAlpha() == 0) {
return;
}
//Synthetic comment -- @@ -4886,6 +4910,7 @@
* debug only
*/
public void dumpDisplayTree() {
        if (0 == mNativeClass) return; // client isn't initialized
nativeDumpDisplayTree(getUrl());
}

//Synthetic comment -- @@ -5842,6 +5867,7 @@
* and the middle point for multi-touch.
*/
private void handleTouchEventCommon(MotionEvent event, int action, int x, int y) {
        if (0 == mNativeClass) return; // client isn't initialized
ScaleGestureDetector detector = mZoomManager.getScaleGestureDetector();

long eventTime = event.getEventTime();
//Synthetic comment -- @@ -6311,6 +6337,7 @@
}

private void startDrag() {
        if (0 == mNativeClass) return; // client isn't initialized
WebViewCore.reducePriority();
// to get better performance, pause updating the picture
WebViewCore.pauseUpdatePicture(mWebViewCore);
//Synthetic comment -- @@ -6387,6 +6414,7 @@
}

private void stopTouch() {
        if (0 == mNativeClass) return; // client isn't initialized
if (mScroller.isFinished() && !mSelectingText
&& (mTouchMode == TOUCH_DRAG_MODE
|| mTouchMode == TOUCH_DRAG_LAYER_MODE)) {
//Synthetic comment -- @@ -6419,6 +6447,7 @@
}

private void cancelTouch() {
        if (0 == mNativeClass) return; // client isn't initialized
// we also use mVelocityTracker == null to tell us that we are
// not "moving around", so we can take the slower/prettier
// mode in the drawing code
//Synthetic comment -- @@ -6666,6 +6695,7 @@
}

private void doTrackball(long time, int metaState) {
        if (0 == mNativeClass) return; // client isn't initialized
int elapsed = (int) (mTrackballLastTime - mTrackballFirstTime);
if (elapsed == 0) {
elapsed = TRACKBALL_TIMEOUT;








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index c35b768..6debbda 100644

//Synthetic comment -- @@ -462,7 +462,8 @@
new WebStorage.QuotaUpdater() {
@Override
public void updateQuota(long newQuota) {
                            if (mNativeClass != 0)
                                nativeSetNewStorageLimit(mNativeClass, newQuota);
}
});
}
//Synthetic comment -- @@ -480,7 +481,8 @@
new WebStorage.QuotaUpdater() {
@Override
public void updateQuota(long newQuota) {
                        if (mNativeClass != 0)
                            nativeSetNewStorageLimit(mNativeClass, newQuota);
}
});
}
//Synthetic comment -- @@ -2067,6 +2069,7 @@

// notify webkit that our virtual view size changed size (after inv-zoom)
private void viewSizeChanged(WebViewClassic.ViewSizeData data) {
        if (0 == mNativeClass) return;
int w = data.mWidth;
int h = data.mHeight;
int textwrapWidth = data.mTextWrapWidth;
//Synthetic comment -- @@ -2209,6 +2212,8 @@
}
}

        if (0 == mNativeClass) return;

mDrawIsScheduled = false;
DrawData draw = new DrawData();
if (DebugFlags.WEB_VIEW_CORE) Log.v(LOGTAG, "webkitDraw start");







