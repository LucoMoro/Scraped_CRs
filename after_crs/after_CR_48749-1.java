/*Browser hang when playing video in popup window

The function "onShowCustomView" in WebChromeClient of subwebview is
not implemented, and makes the fullscreen layout can't be set. Here
we call main webview's onShowCustomView.

Change-Id:I576e017be61bca0775b5cc897f35092a63c6dca7Author: weiweix.ji <weiweix.ji@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51736*/




//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index b5000c2..0b69db2 100644

//Synthetic comment -- @@ -1126,6 +1126,19 @@
mClient.onProgressChanged(view, newProgress);
}
@Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mClient.onShowCustomView(view, callback);
        }
        @Override
        public void onShowCustomView(View view, int requestedOrientation,
            CustomViewCallback callback) {
            mClient.onShowCustomView(view, requestedOrientation, callback);
        }
        @Override
        public void onHideCustomView() {
            mClient.onHideCustomView();
        }
        @Override
public boolean onCreateWindow(WebView view, boolean dialog,
boolean userGesture, android.os.Message resultMsg) {
return mClient.onCreateWindow(view, dialog, userGesture, resultMsg);
//Synthetic comment -- @@ -1137,6 +1150,10 @@
}
mWebViewController.dismissSubWindow(Tab.this);
}
        @Override
        public View getVideoLoadingProgressView() {
            return mClient.getVideoLoadingProgressView();
        }
}

// -------------------------------------------------------------------------







