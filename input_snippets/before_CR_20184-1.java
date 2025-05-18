
//<Beginning of snippet n. 0>


mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Change the in-memory settings to force the http/https mode
        mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());

//<End of snippet n. 0>








