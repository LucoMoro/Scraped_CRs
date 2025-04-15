/*Merge: Load and apply proxy settings from config file in UpdateNoWindow, allowing headless updates to work behind a proxy.

Change-Id:I9d3973ff1e6f17765098670e0cabe852d1352d1f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index 53ddd1e..fb2c9a4 100755

//Synthetic comment -- @@ -63,8 +63,13 @@
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Change the in-memory settings to force the http/https mode
        mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());







