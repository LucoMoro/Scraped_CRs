/*crashed JAVACRASH on 'com.google.android.apps.docs'

other app call webviewcore, but it is not initialized, so
check the null pointer when run in webview.

Change-Id:I3ee498baa3a2bca62d6eca8e989153c7f9baccf9Author: Bin Xu <binx.xu@intel.com>
Signed-off-by: Bin Xu <binx.xu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 68896*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index ae56e6b..068593e 100644

//Synthetic comment -- @@ -2518,6 +2518,9 @@
}

private void loadUrlImpl(String url, Map<String, String> extraHeaders) {
        if (mWebViewCore == null) {
            return;
        }
switchOutDrawHistory();
WebViewCore.GetUrlData arg = new WebViewCore.GetUrlData();
arg.mUrl = url;







