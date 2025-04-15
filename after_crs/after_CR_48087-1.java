/*Memory leak due to bad destroy sequence

During the destroy sequence, it happens that the mWebViewClassic was
set to null in the transferMessages function and consequently, the
call to WebCoreThreadWatchdog.unregisterWebView(mWebViewClassic) is
not really removing the view from the watchdog's list; creating a
memory leak.

Change-Id:I2bae7c8d7c473e2af25e62a485699f88269d6658Author: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57869*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index 3fb3ec6..c35b768 100644

//Synthetic comment -- @@ -1278,6 +1278,7 @@
mBrowserFrame = null;
mSettings.onDestroyed();
mNativeClass = 0;
                                WebCoreThreadWatchdog.unregisterWebView(mWebViewClassic);
mWebViewClassic = null;
}
break;
//Synthetic comment -- @@ -1982,7 +1983,6 @@
mEventHub.sendMessageAtFrontOfQueue(
Message.obtain(null, EventHub.DESTROY));
mEventHub.blockMessages();
}
}








