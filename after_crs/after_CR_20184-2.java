/*Load and apply proxy settings from config file in UpdateNoWindow, allowing headless updates to work behind a proxy.

Change-Id:I0deb8981c5fec0c7ae3eddbbde529537c02fdc1c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index f390e72..a2f5576 100755

//Synthetic comment -- @@ -63,8 +63,13 @@
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

        // Read and apply settings from settings file, so that http/https proxy is set
        SettingsController settingsController = mUpdaterData.getSettingsController();
        settingsController.loadSettings();
        settingsController.applySettings();

// Change the in-memory settings to force the http/https mode
        settingsController.setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());







