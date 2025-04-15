/*Fix Biggest Memory leaks on Browser application

Working on Issue 31125 (http://code.google.com/p/android/issues/detail?id=31125):
* Enhance bitmap management (add recycle() functions when necessary)
* Enhance mTabViews management on NavScreen class (add remove() function
  when necessary).

Change-Id:I544620484f4ad52e02e4d3f4336a1f007bbdc8fbAuthor: Sebastien MICHEL <sebastien.michel@intel.com
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 56189*/
//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index caea83e..0dbd2f7 100644

//Synthetic comment -- @@ -754,6 +754,10 @@
mUploadHandler.onResult(Activity.RESULT_CANCELED, null);
mUploadHandler = null;
}
if (mTabControl == null) return;
mUi.onDestroy();
// Remove the current tab and sub window








//Synthetic comment -- diff --git a/src/com/android/browser/NavScreen.java b/src/com/android/browser/NavScreen.java
//Synthetic comment -- index 1d2114e..5df64de 100644

//Synthetic comment -- @@ -162,6 +162,7 @@
} else {
mUiController.closeTab(tab);
}
}
}

//Synthetic comment -- @@ -238,6 +239,7 @@
public void onClick(View v) {
if (tabview.isClose(v)) {
mScroller.animateOut(tabview);
} else if (tabview.isTitle(v)) {
switchToTab(tab);
mUi.getTitleBar().setSkipTitleBarAnimations(true);








//Synthetic comment -- diff --git a/src/com/android/browser/PhoneUi.java b/src/com/android/browser/PhoneUi.java
//Synthetic comment -- index 89eae70..86ffd94 100644

//Synthetic comment -- @@ -74,6 +74,7 @@
@Override
public void onDestroy() {
hideTitleBar();
}

@Override
//Synthetic comment -- @@ -514,6 +515,17 @@
mContent.setImageBitmap(mContentBitmap);
}

private Bitmap safeCreateBitmap(int width, int height) {
if (width <= 0 || height <= 0) {
Log.w(LOGTAG, "safeCreateBitmap failed! width: " + width








//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index b5000c2..426b18a 100644

//Synthetic comment -- @@ -1315,6 +1315,11 @@
* Destroy the tab's main WebView and subWindow if any
*/
void destroy() {
if (mMainView != null) {
dismissSubWindow();
// save the WebView to call destroy() after detach it from the tab







