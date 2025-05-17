//<Beginning of snippet n. 0>
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Load proxy settings from the config file
FileInputStream configFile = null;
try {
    Properties properties = new Properties();
    configFile = new FileInputStream("config.properties");
    properties.load(configFile);

    String proxyHost = properties.getProperty("proxy.host");
    String proxyPort = properties.getProperty("proxy.port");
    String proxyUser = properties.getProperty("proxy.username");
    String proxyPass = properties.getProperty("proxy.password");
    
    if (proxyHost != null && isValidProxyPort(proxyPort)) {
        mUpdaterData.getSettingsController().setProxySettings(proxyHost, Integer.parseInt(proxyPort), proxyUser, proxyPass);
    } else {
        throw new IllegalArgumentException("Invalid proxy settings in config file.");
    }
} catch (IOException e) {
    System.err.println("Error reading proxy settings from config file: " + e.getMessage());
    // Fallback mechanism could be implemented here
} catch (IllegalArgumentException e) {
    System.err.println("Error in proxy configuration: " + e.getMessage());
    // Fallback mechanism could be implemented here
} finally {
    if (configFile != null) {
        try {
            configFile.close();
        } catch (IOException e) {
            System.err.println("Error closing config file: " + e.getMessage());
        }
    }
}

// Change the in-memory settings to force the http/https mode
mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());

private boolean isValidProxyPort(String port) {
    try {
        int p = Integer.parseInt(port);
        return p > 0 && p <= 65535;
    } catch (NumberFormatException e) {
        return false;
    }
}
//<End of snippet n. 0>