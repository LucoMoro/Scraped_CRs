/*Fix NullPointerException in WebViewCore

DNS resolver is getting invoked without checking whether it is NULL.
Putting NULL checks in place so that any attempt to destroy DNS resolver
without proper creation does not cause Browser crash.*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index 4118119..ff5aff6 100644

//Synthetic comment -- @@ -944,6 +944,10 @@
break;

case LOAD_URL: {
GetUrlData param = (GetUrlData) msg.obj;
loadUrl(param.mUrl, param.mExtraHeaders);
break;
//Synthetic comment -- @@ -1000,6 +1004,10 @@
break;

case RELOAD:
mBrowserFrame.reload(false);
break;

//Synthetic comment -- @@ -1043,6 +1051,10 @@
if (!mBrowserFrame.committed() && msg.arg1 == -1 &&
(mBrowserFrame.loadType() ==
BrowserFrame.FRAME_LOADTYPE_STANDARD)) {
mBrowserFrame.reload(true);
} else {
mBrowserFrame.goBackOrForward(msg.arg1);
//Synthetic comment -- @@ -1544,6 +1556,10 @@
}
mEventHub.blockMessages();
}
}

//-------------------------------------------------------------------------







