//<Beginning of snippet n. 0>
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Load Proxy Settings
try {
    Properties proxyProperties = new Properties();
    FileInputStream in = new FileInputStream("proxy_config.properties");
    proxyProperties.load(in);
    String proxyHost = proxyProperties.getProperty("proxyHost");
    String proxyPort = proxyProperties.getProperty("proxyPort");
    String proxyUser = proxyProperties.getProperty("proxyUser");
    String proxyPassword = proxyProperties.getProperty("proxyPassword");

    if (proxyHost != null && proxyPort != null) {
        mUpdaterData.getSettingsController().setSetting("proxyHost", proxyHost);
        mUpdaterData.getSettingsController().setSetting("proxyPort", proxyPort);
        if (proxyUser != null && proxyPassword != null) {
            mUpdaterData.getSettingsController().setSetting("proxyUser", proxyUser);
            mUpdaterData.getSettingsController().setSetting("proxyPassword", proxyPassword);
        }
    }
} catch (FileNotFoundException e) {
    sdkLog.error("Proxy configuration file not found.");
} catch (IOException e) {
    sdkLog.error("Error reading proxy configuration file.");
}

// Change the in-memory settings to force the http/https mode
mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());
//<End of snippet n. 0>