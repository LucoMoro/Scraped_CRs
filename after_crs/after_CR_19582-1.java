/*orientationchange event sent too early

The orientationchange event is sent to native webkit
before the screen dimensions have been reported to native webkit.
Thus, scripts using the orientationchange event will
use wrong dimensions.

This patch sends the orientationchange event to native
webkit after the new dimensions have been reported.

Reproduce this problem by create a website which
uses the orientationchange event. Read the window.innerWidth
and window.innerHeight and print to console in the
orientationchange eventhandler. Load the website into the
browser rotate the screen. Without the patch the
width and height values are wrong, with the patch
widht and height are the correct.

Change-Id:Id6e3f1f50be8f811942d120d955dfde9ad43662bConflicts:

	core/java/android/webkit/BrowserFrame.java*/




//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 84deeb0..cab6c94 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
// queue has been cleared,they are ignored.
private boolean mBlockMessages = false;
private int mOrientation = -1;
    private boolean mPendingOrientationChanged = false;

// Is this frame the main frame?
private boolean mIsMainFrame;
//Synthetic comment -- @@ -476,7 +477,7 @@
case ORIENTATION_CHANGED: {
if (mOrientation != msg.arg1) {
mOrientation = msg.arg1;
                    mPendingOrientationChanged = true;
}
break;
}
//Synthetic comment -- @@ -487,6 +488,26 @@
}

/**
    * Return true if there is a pending orientation change that has
    * not been reported to native webkit yet
    * @return True if there is a pending orientation change else false
    */
    /* package */ boolean hasPendingOrientationChange() {
        return mPendingOrientationChanged;
    }

    /**
    * Report orientation change to native and update
    * the internal pending orientation change state.
    * Use together with {@link BrowserFrame#hasPendingOrientationChange()}
    * in order to prevent unnecessary calls.
    */
    /* package */ void sendOrientationChanged() {
        nativeOrientationChanged(mOrientation);
        mPendingOrientationChanged = false;
    }

    /**
* Punch-through for WebCore to set the document
* title. Inform the Activity of the new title.
* @param title The new title of the document.








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index f54b207..00e37b9 100644

//Synthetic comment -- @@ -1665,6 +1665,9 @@
}
mEventHub.sendMessage(Message.obtain(null,
EventHub.UPDATE_CACHE_AND_TEXT_ENTRY));
        if (mBrowserFrame != null && mBrowserFrame.hasPendingOrientationChange()) {
            mBrowserFrame.sendOrientationChanged();
        }
}

private void sendUpdateTextEntry() {







