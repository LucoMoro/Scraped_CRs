/*Tombstone in com.android.browser at /system/libhwui.so

remove the Subwindow container first.

Change-Id:Ie561a1640661ab59213facc5afc3ea98fa96846dAuthor: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 42566*/




//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index 6200286..81078dd 100644

//Synthetic comment -- @@ -1313,6 +1313,12 @@
*/
void destroy() {
if (mMainView != null) {
            if (mSubView != null)
            {
                //remove the Subwindow container first.
                Controller mController = (Controller)mWebViewController;
                mController.removeSubWindow(this);
            }
dismissSubWindow();
// save the WebView to call destroy() after detach it from the tab
WebView webView = mMainView;







