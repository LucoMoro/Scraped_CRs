/*Set progress to 0 before loading URL's

The progress value is updated asynchronously so it was possible to
call getProgress() and get a value of 100 before the load had even
been started.*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 24eebd7..70738b5 100644

//Synthetic comment -- @@ -2075,6 +2075,7 @@
WebViewCore.GetUrlData arg = new WebViewCore.GetUrlData();
arg.mUrl = url;
arg.mExtraHeaders = extraHeaders;
        mCallbackProxy.onProgressChanged(0);
mWebViewCore.sendMessage(EventHub.LOAD_URL, arg);
clearHelpers();
}
//Synthetic comment -- @@ -2110,6 +2111,7 @@
WebViewCore.PostUrlData arg = new WebViewCore.PostUrlData();
arg.mUrl = url;
arg.mPostData = postData;
            mCallbackProxy.onProgressChanged(0);
mWebViewCore.sendMessage(EventHub.POST_URL, arg);
clearHelpers();
} else {
//Synthetic comment -- @@ -2196,6 +2198,7 @@
arg.mMimeType = mimeType;
arg.mEncoding = encoding;
arg.mHistoryUrl = historyUrl;
        mCallbackProxy.onProgressChanged(0);
mWebViewCore.sendMessage(EventHub.LOAD_DATA, arg);
clearHelpers();
}
//Synthetic comment -- @@ -2264,6 +2267,7 @@
checkThread();
clearHelpers();
switchOutDrawHistory();
        mCallbackProxy.onProgressChanged(0);
mWebViewCore.sendMessage(EventHub.RELOAD);
}








