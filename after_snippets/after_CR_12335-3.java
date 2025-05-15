
//<Beginning of snippet n. 0>


launcher.runOnUiThread(action);
}

            synchronized(LauncherModel.this) {
                if (!mStopped) {
                    mApplicationsLoaded = true;
                } else {
                    if (DEBUG_LOADERS) d(LOG_TAG, "  ----> applications loader stopped (" + mId + ")");                                
                }
}
mRunning = false;
}
c.close();
}

            synchronized(LauncherModel.this) {
if (!mStopped) {
                    if (DEBUG_LOADERS)  {
                        d(LOG_TAG, "  --> done loading workspace");
                        d(LOG_TAG, "  ----> worskpace items=" + desktopItems.size());
                        d(LOG_TAG, "  ----> worskpace widgets=" + desktopAppWidgets.size());
}

                    // Create a copy of the lists in case the workspace loader is restarted
                    // and the list are cleared before the UI can go through them
                    final ArrayList<ItemInfo> uiDesktopItems =
                            new ArrayList<ItemInfo>(desktopItems);
                    final ArrayList<LauncherAppWidgetInfo> uiDesktopWidgets =
                            new ArrayList<LauncherAppWidgetInfo>(desktopAppWidgets);

                    if (!mStopped) {
                        d(LOG_TAG, "  ----> items cloned, ready to refresh UI");                
                        launcher.runOnUiThread(new Runnable() {
                            public void run() {
                                if (DEBUG_LOADERS) d(LOG_TAG, "  ----> onDesktopItemsLoaded()");
                                launcher.onDesktopItemsLoaded(uiDesktopItems, uiDesktopWidgets);
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
                } else {
                    if (DEBUG_LOADERS) d(LOG_TAG, "  ----> worskpace loader was stopped");
                }
}
mRunning = false;
}

//<End of snippet n. 0>








