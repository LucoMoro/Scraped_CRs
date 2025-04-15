/*Fixing a monkey crash in the BrowserSettings

Monkey crash, sometimes the function getCurrentTopWebView()
can return null causing a crash in clearFormData().

Change-Id:I27d38fafd448a3922a09bbdf2b31ab6f78fecf88*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index 9c5af34..c3b6a08 100644

//Synthetic comment -- @@ -515,7 +515,10 @@
/* package */ void clearFormData(Context context) {
WebViewDatabase.getInstance(context).clearFormData();
if (mTabControl != null) {
            mTabControl.getCurrentTopWebView().clearFormData();
}
}








