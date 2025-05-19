<<Beginning of snippet n. 0>>
mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Load proxy configuration from the configuration file
Properties proxyProperties = new Properties();
String configPath = System.getenv("CONFIG_PATH"); // Dynamic configuration path
if (configPath == null || configPath.isEmpty()) {
    sdkLog.error("Configuration path not set.");
    return;
}

try (InputStream input = new FileInputStream(configPath)) {
    proxyProperties.load(input);
    String proxyHost = proxyProperties.getProperty("proxy.host");
    String proxyPort = proxyProperties.getProperty("proxy.port");
    String proxyUser = proxyProperties.getProperty("proxy.user");
    String proxyPass = proxyProperties.getProperty("proxy.pass");

    if (proxyHost == null || proxyHost.isEmpty()) {
        sdkLog.error("Missing proxy.host property.");
        return;
    }
    if (proxyPort == null || proxyPort.isEmpty()) {
        sdkLog.error("Missing proxy.port property.");
        return;
    }
    if (proxyUser == null || proxyUser.isEmpty()) {
        sdkLog.error("Missing proxy.user property.");
        return;
    }
    if (proxyPass == null || proxyPass.isEmpty()) {
        sdkLog.error("Missing proxy.pass property. This will not be logged for security reasons.");
        return;
    }

    int port;
    try {
        port = Integer.parseInt(proxyPort);
        if (port < 1 || port > 65535) {
            sdkLog.error("Proxy port is out of valid range (1-65535).");
            return;
        }
    } catch (NumberFormatException e) {
        sdkLog.error("Invalid proxy port number: " + e.getMessage());
        return;
    }

    if (!isValidProxyHost(proxyHost)) {
        sdkLog.error("Invalid proxy.host property: " + proxyHost);
        return;
    }

    // Apply proxy settings to UpdaterData
    mUpdaterData.setProxySettings(proxyHost, port, proxyUser, proxyPass);
} catch (IOException e) {
    sdkLog.error("Failed to load proxy configuration: " + e.getMessage());
    return;
} catch (Exception e) {
    sdkLog.error("Unexpected error: " + e.getMessage());
    return;
}

// Change the in-memory settings to force the http/https mode
mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

// Use a factory that only outputs to the given ISdkLog.
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());

private boolean isValidProxyHost(String proxyHost) {
    return proxyHost.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b") || 
           proxyHost.matches("^(?!-)[A-Za-z0-9-]{1,63}(?:\\.[A-Za-z0-9-]{1,63})*$");
}
<<End of snippet n. 0>>