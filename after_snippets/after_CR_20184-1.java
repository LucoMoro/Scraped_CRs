
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>








