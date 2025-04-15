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

Change-Id:Id6e3f1f50be8f811942d120d955dfde9ad43662b*/
//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 84deeb0..4fb1aad 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
// queue has been cleared,they are ignored.
private boolean mBlockMessages = false;
private int mOrientation = -1;

// Is this frame the main frame?
private boolean mIsMainFrame;
//Synthetic comment -- @@ -476,7 +477,7 @@
case ORIENTATION_CHANGED: {
if (mOrientation != msg.arg1) {
mOrientation = msg.arg1;
                    nativeOrientationChanged(msg.arg1);
}
break;
}
//Synthetic comment -- @@ -486,6 +487,13 @@
}
}

/**
* Punch-through for WebCore to set the document
* title. Inform the Activity of the new title.








//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 88516ce..359e526 100644

//Synthetic comment -- @@ -4670,6 +4670,30 @@
return changed;
}

private static class PostScale implements Runnable {
final WebView mWebView;
final boolean mUpdateTextWrap;








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index f54b207..ceeba65 100644

//Synthetic comment -- @@ -873,6 +873,8 @@
static final int ADD_PACKAGE_NAME = 185;
static final int REMOVE_PACKAGE_NAME = 186;

// private message ids
private static final int DESTROY =     200;

//Synthetic comment -- @@ -1365,6 +1367,12 @@
BrowserFrame.sJavaBridge.removePackageName(
(String) msg.obj);
break;
}
}
};







