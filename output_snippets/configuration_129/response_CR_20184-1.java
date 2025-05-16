<<Beginning of snippet n. 0>>
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

Properties properties = new Properties();
try (InputStream input = new FileInputStream("config.properties")) {
    properties.load(input);
    String proxyHost = properties.getProperty("proxy.host");
    String proxyPort = properties.getProperty("proxy.port");
    
    if (proxyHost != null && proxyPort != null) {
        mUpdaterData.getSettingsController().setProxySettings(proxyHost, Integer.parseInt(proxyPort));
        sdkLog.info("Proxy settings loaded: Host = " + proxyHost + ", Port = " + proxyPort);
    } else {
        sdkLog.warning("Proxy settings could not be loaded from config file.");
    }
} catch (IOException e) {
    sdkLog.severe("Could not load configuration file: " + e.getMessage());
} catch (NumberFormatException e) {
    sdkLog.severe("Invalid proxy port number in configuration: " + e.getMessage());
}

// Change the in-memory settings to force the http/https mode
mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());
<<End of snippet n. 0>>