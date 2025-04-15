/*[ACS-Monkey] Javacrash java.lang.NullPointerException in com.android.browser.

Provide a WebView interface in WebSettings, for BrowserSettings need to access it.

Change-Id:I375561867f3eb05681d579dfa173b5e298f7649eAuthor: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34962*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebSettingsClassic.java b/core/java/android/webkit/WebSettingsClassic.java
//Synthetic comment -- index 1288613..3105fe3 100644

//Synthetic comment -- @@ -1660,6 +1660,14 @@
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







