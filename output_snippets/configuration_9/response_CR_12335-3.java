//<Beginning of snippet n. 0>
synchronized (this) {
    if (!mStopped) {
        mApplicationsLoaded = true;

        if (DEBUG_LOADERS) {
            d(LOG_TAG, "  --> done loading workspace");
            d(LOG_TAG, "  ----> workspace items=" + desktopItems.size());
            d(LOG_TAG, "  ----> workspace widgets=" + desktopAppWidgets.size());
        }

        final ArrayList<ItemInfo> uiDesktopItems = new ArrayList<ItemInfo>(desktopItems);
        final ArrayList<LauncherAppWidgetInfo> uiDesktopWidgets = new ArrayList<LauncherAppWidgetInfo>(desktopAppWidgets);

        d(LOG_TAG, "  ----> items cloned, ready to refresh UI");
        launcher.runOnUiThread(new Runnable() {
            public void run() {
                synchronized (MyClass.this) {
                    if (!mStopped) {
                        if (DEBUG_LOADERS) d(LOG_TAG, "  ----> onDesktopItemsLoaded()");
                        launcher.onDesktopItemsLoaded(uiDesktopItems, uiDesktopWidgets);
                    }
                }
            }
        });

        if (mLoadApplications) {
            if (DEBUG_LOADERS) {
                d(LOG_TAG, "  ----> loading applications from workspace loader");
            }
            startApplicationsLoader(launcher, mIsLaunching);
        }

        mDesktopItemsLoaded = true;
    } else {
        if (DEBUG_LOADERS) d(LOG_TAG, "  ----> workspace loader was stopped");
    }
    mRunning = false;
}
c.close();
//<End of snippet n. 0>