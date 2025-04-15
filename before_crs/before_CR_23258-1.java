/*Prevent AppWidgetService from beeing used before loaded

The widgets in the mAppWidgetIds-list can sometimes
be accessed before they have been properly loaded. By synchronizing on
the mAppWidgetIds-list other threads are forced to wait until
the widgets have been loaded by the systemReady-method.

Change-Id:I4ec008cdbe9a70cabed0e439003f5251fe7a7a7cSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AppWidgetService.java b/services/java/com/android/server/AppWidgetService.java
//Synthetic comment -- index 731fb22..5950082 100644

//Synthetic comment -- @@ -124,34 +124,36 @@
}

public void systemReady(boolean safeMode) {
        mSafeMode = safeMode;

        loadAppWidgetList();
        loadStateLocked();

        // Register for the boot completed broadcast, so we can send the
        // ENABLE broacasts.  If we try to send them now, they time out,
        // because the system isn't ready to handle them yet.
        mContext.registerReceiver(mBroadcastReceiver,
                new IntentFilter(Intent.ACTION_BOOT_COMPLETED), null, null);

        // Register for configuration changes so we can update the names
        // of the widgets when the locale changes.
        mContext.registerReceiver(mBroadcastReceiver,
                new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED), null, null);

        // Register for broadcasts about package install, etc., so we can
        // update the provider list.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        mContext.registerReceiver(mBroadcastReceiver, filter);
        // Register for events related to sdcard installation.
        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mContext.registerReceiver(mBroadcastReceiver, sdFilter);
}

@Override







