/*Fix a race condition that caused the Launcher to be blank when triggered.*/
//Synthetic comment -- diff --git a/src/com/android/launcher/LauncherModel.java b/src/com/android/launcher/LauncherModel.java
//Synthetic comment -- index d69bd13..54ab531 100644

//Synthetic comment -- @@ -483,6 +483,7 @@
private class ApplicationsLoader implements Runnable {
private final WeakReference<Launcher> mLauncher;

private volatile boolean mStopped;
private volatile boolean mRunning;
private final boolean mIsLaunching;
//Synthetic comment -- @@ -496,7 +497,9 @@
}

void stop() {
            mStopped = true;
}

boolean isRunning() {
//Synthetic comment -- @@ -541,10 +544,12 @@
launcher.runOnUiThread(action);
}

            if (!mStopped) {
                mApplicationsLoaded = true;
            } else {
                if (DEBUG_LOADERS) d(LOG_TAG, "  ----> applications loader stopped (" + mId + ")");                                
}
mRunning = false;
}
//Synthetic comment -- @@ -718,6 +723,7 @@
}

private class DesktopItemsLoader implements Runnable {
private volatile boolean mStopped;
private volatile boolean mRunning;

//Synthetic comment -- @@ -737,7 +743,9 @@
}

void stop() {
            mStopped = true;
}

boolean isRunning() {
//Synthetic comment -- @@ -982,7 +990,11 @@
startApplicationsLoader(launcher, mIsLaunching);
}

                mDesktopItemsLoaded = true;
} else {
if (DEBUG_LOADERS) d(LOG_TAG, "  ----> worskpace loader was stopped");
}







