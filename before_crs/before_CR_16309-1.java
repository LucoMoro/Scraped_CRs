/*packages/apps: Hide the wallpaper when the allapps view is displayed

The Wallaper consumes CPU even when the all apps view is in focus.
Hide wallpaper when the all apps view is displayed.
This improves performance.

Change-Id:I3d4cf99a0e1272cb15a0f32012473258a972cba2*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 6bd915a..3130f60 100644

//Synthetic comment -- @@ -1888,6 +1888,7 @@
}

void showAllApps(boolean animated) {
mAllAppsGrid.zoom(1.0f, animated);

((View) mAllAppsGrid).setFocusable(true);
//Synthetic comment -- @@ -1942,6 +1943,7 @@
mAllAppsGrid.zoom(0.0f, animated);
((View)mAllAppsGrid).setFocusable(false);
mWorkspace.getChildAt(mWorkspace.getCurrentScreen()).requestFocus();
}
}









//Synthetic comment -- diff --git a/src/com/android/launcher2/Workspace.java b/src/com/android/launcher2/Workspace.java
//Synthetic comment -- index c337c30..806acbd 100644

//Synthetic comment -- @@ -810,6 +810,15 @@
}
}

void enableChildrenCache(int fromScreen, int toScreen) {
if (fromScreen > toScreen) {
final int temp = fromScreen;







