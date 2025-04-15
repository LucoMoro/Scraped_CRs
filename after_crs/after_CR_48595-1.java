/*Javacrash java.lang.NullPointerException in com.android.browser.

Provide a WebView interface in WebSettings, for BrowserSettings need to access it.

Change-Id:I8a4b9c9a746c9c0b2a1a6adec35943de9fb2f9b0Author: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34962*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebSettingsClassic.java b/core/java/android/webkit/WebSettingsClassic.java
//Synthetic comment -- index 1bbe7bb..b697099 100644

//Synthetic comment -- @@ -1668,6 +1668,14 @@
return mAutoFillProfile;
}

    /**
     * @hide
     * @return
     */
    public synchronized WebViewClassic getWebView(){
        return mWebView;
    }

int getDoubleTapToastCount() {
return mDoubleTapToastCount;
}







