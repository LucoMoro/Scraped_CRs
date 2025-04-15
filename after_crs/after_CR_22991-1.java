/*Fix for the android issue 15162.

Overlapping of widgets in on the home screen has been resolved with this fix.

Change-Id:I719eb38226de3545651d9223ba15f232be899d51*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index e611303..bb88a60 100644

//Synthetic comment -- @@ -1163,6 +1163,8 @@
public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId()) {
case MENU_ADD:
		 		mMenuAddInfo = mWorkspace.findAllVacantCells(null);
            	if(mMenuAddInfo != null && mMenuAddInfo.valid)
addItems();
return true;
case MENU_MANAGE_APPS:
//Synthetic comment -- @@ -1975,6 +1977,10 @@
void closeAllApps(boolean animated) {
if (mAllAppsGrid.isVisible()) {
mWorkspace.setVisibility(View.VISIBLE);
            if(!animated)
            {
             closeFolder();
            }
mAllAppsGrid.zoom(0.0f, animated);
((View)mAllAppsGrid).setFocusable(false);
mWorkspace.getChildAt(mWorkspace.getCurrentScreen()).requestFocus();







