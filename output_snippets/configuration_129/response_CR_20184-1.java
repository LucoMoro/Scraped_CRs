<<Beginning of snippet n. 0>>
try {
    Properties proxyProps = new Properties();
    String configFilePath = System.getenv("PROXY_CONFIG_PATH") != null ? System.getenv("PROXY_CONFIG_PATH") : "proxyConfig.properties";
    
    try (FileInputStream configFile = new FileInputStream(configFilePath)) {
        proxyProps.load(configFile);
    }

    String proxyAddress = proxyProps.getProperty("proxyAddress");
    String proxyPort = proxyProps.getProperty("proxyPort");

    if (proxyAddress == null || proxyPort == null) {
        throw new IllegalArgumentException("Proxy address or port is missing");
    }

    if (!isValidProxyAddress(proxyAddress)) {
        throw new IllegalArgumentException("Invalid proxy address format");
    }

    int port;
    try {
        port = Integer.parseInt(proxyPort);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid port format in proxy configuration: " + proxyPort, e);
    }

    if (port <= 0 || port > 65535) {
        throw new IllegalArgumentException("Port number out of range: " + port);
    }

    mUpdaterData.setProxy(proxyAddress, port);
} catch (IOException e) {
    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading proxy configuration", e);
} catch (IllegalArgumentException e) {
    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Configuration error", e);
}

mForce = force;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);
mUpdaterData.setTaskFactory(new ConsoleTaskFactory());

private boolean isValidProxyAddress(String proxyAddress) {
    return proxyAddress.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$") || proxyAddress.matches("^[a-zA-Z0-9._-]+(\\.[a-zA-Z]{2,})?$");
}
<<End of snippet n. 0>>