/*Launcher: Avoid showing clings if no touchscreen

If the device has no touchscreen the clings are displayed indicating that
"To add an app to your Home screen, touch &amp; hold it.".
Furthermore, having no touchscreen is very difficult to get rid of the
clings because the focus remains on the Launcher.

Change-Id:I26182955fe3d44c51e82b140b5cc2ee3baacf6caConflicts:

	src/com/android/launcher2/Launcher.java*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index c221815..d67ad0f 100644

//Synthetic comment -- @@ -3795,12 +3795,16 @@
}
}
public void showFirstRunAllAppsCling(int[] position) {
        final Configuration configuration = getResources().getConfiguration();
        // Avoid showing the clings if no touchscreen
        if ( configuration.touchscreen != Configuration.TOUCHSCREEN_NOTOUCH ) {
            // Enable the clings only if they have not been dismissed before
            if (isClingsEnabled() &&
                    !mSharedPrefs.getBoolean(Cling.ALLAPPS_CLING_DISMISSED_KEY, false)) {
                initCling(R.id.all_apps_cling, position, true, 0);
            } else {
                removeCling(R.id.all_apps_cling);
            }
}
}
public Cling showFirstRunFoldersCling() {







