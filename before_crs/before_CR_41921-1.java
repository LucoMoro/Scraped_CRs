/*No search pane while tapping search button after backing to "windows" view from "saved page" view.

When NavScreen is showing, the active tab should be detached normally,
but tab is reattached after backing to NavScreen from other activity.
Do the detach again.

Change-Id:I1b30791285a0ff08c69924d74a0f1660212995f2Author: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28528*/
//Synthetic comment -- diff --git a/src/com/android/browser/PhoneUi.java b/src/com/android/browser/PhoneUi.java
//Synthetic comment -- index e3c22bd..89eae70 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
private int mActionBarHeight;

boolean mAnimating;

/**
* @param browser
//Synthetic comment -- @@ -80,6 +81,8 @@
if (mUseQuickControls) {
mTitleBar.setShowProgressOnly(false);
}
super.editUrl(clearInput, forceIME);
}

//Synthetic comment -- @@ -131,6 +134,12 @@
mTitleBar.cancelTitleBarAnimation(true);
mTitleBar.setSkipTitleBarAnimations(true);
super.setActiveTab(tab);
BrowserWebView view = (BrowserWebView) tab.getWebView();
// TabControl.setCurrentTab has been called before this,
// so the tab is guaranteed to have a webview
//Synthetic comment -- @@ -254,6 +263,7 @@
}

void showNavScreen() {
mUiController.setBlockEvents(true);
if (mNavScreen == null) {
mNavScreen = new NavScreen(mActivity, mUiController, this);
//Synthetic comment -- @@ -334,6 +344,7 @@
}

void hideNavScreen(int position, boolean animate) {
if (!showingNavScreen()) return;
final Tab tab = mUiController.getTabControl().getTab(position);
if ((tab == null) || !animate) {







