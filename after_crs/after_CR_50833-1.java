/*Prevent cursor from causing global reference leaks.

To fix this the ContentQueryMap needs to be closed
when onStop is called so any global references can
be reclaimed.

Change-Id:I0c679c10b0d75ab0aac2fe499563c57d934e318a*/




//Synthetic comment -- diff --git a/src/com/android/settings/LocationSettings.java b/src/com/android/settings/LocationSettings.java
//Synthetic comment -- index cfbbe26..0e50188 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
if (mSettingsObserver != null) {
mContentQueryMap.deleteObserver(mSettingsObserver);
}
        mContentQueryMap.close();
}

private PreferenceScreen createPreferenceHierarchy() {







