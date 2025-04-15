/*Move the start of the Listener of AppWidgetHost from onCreate to
    finishBindingItems to improve Home Screen performance in loading
    process.

Change-Id:Id16d71eb52989abc8cc31067089b607a9c5dee08*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 2742f2f..4ec1266 100644

//Synthetic comment -- @@ -224,7 +224,6 @@

mAppWidgetManager = AppWidgetManager.getInstance(this);
mAppWidgetHost = new LauncherAppWidgetHost(this, APPWIDGET_HOST_ID);
        mAppWidgetHost.startListening();

if (PROFILE_STARTUP) {
android.os.Debug.startMethodTracing(
//Synthetic comment -- @@ -2246,6 +2245,7 @@
}

mWorkspaceLoading = false;
}

/**







