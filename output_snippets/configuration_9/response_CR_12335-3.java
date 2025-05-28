//<Beginning of snippet n. 0>
volatile boolean loadingComplete = false;
synchronized (this) {
    if (!mStopped) {
        launcher.runOnUiThread(action);
        mApplicationsLoaded = true;
        mRunning = false;

        if (DEBUG_LOADERS) {
            d(LOG_TAG, "  --> done loading workspace");
            d(LOG_TAG, "  ----> workspace items=" + desktopItems.size());
            d(LOG_TAG, "  ----> workspace widgets=" + desktopAppWidgets.size());
        }

        final ArrayList<ItemInfo> uiDesktopItems = new ArrayList<ItemInfo>(desktopItems);
        final ArrayList<LauncherAppWidgetInfo> uiDesktopWidgets = new ArrayList<LauncherAppWidgetInfo>(desktopAppWidgets);

        if (!mStopped) {
            d(LOG_TAG, "  ----> items cloned, ready to refresh UI");
            launcher.runOnUiThread(new Runnable() {
                public void run() {
                    synchronized (YourClassName.this) {
                        if (!mStopped) {
                            if (DEBUG_LOADERS) d(LOG_TAG, "  ----> onDesktopItemsLoaded()");
                            launcher.onDesktopItemsLoaded(uiDesktopItems, uiDesktopWidgets);
                        }
                    }
                }
            });
        }

        if (mLoadApplications) {
            if (DEBUG_LOADERS) {
                d(LOG_TAG, "  ----> loading applications from workspace loader");
            }
            startApplicationsLoader(launcher, mIsLaunching);
        }
        mDesktopItemsLoaded = true;
        loadingComplete = true;
    } else {
        if (DEBUG_LOADERS) d(LOG_TAG, "  ----> workspace loader was stopped");
        mRunning = false;
    }
}
c.close();
if (loadingComplete) {
    synchronized (this) {
        if (!mStopped) {
            // Notify that loading is complete
        }
    }
}
//<End of snippet n. 0>