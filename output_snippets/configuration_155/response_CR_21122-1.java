//<Beginning of snippet n. 0>
@Test
public void testCreateSocket() throws Exception {
    new SSLCertificateSocketFactory(100);
    int port = 443;
    String host = "test.server.com";
    int connectionTimeout = 5000;

    if (!isServerAvailable(host) && !isServerAvailable("fallback.server.com")) {
        fail("Unable to connect to both primary and fallback server.");
    }

    try {
        Socket socket = createSocket(host, port, connectionTimeout);
    } catch (IOException e) {
        // Logging primary connection attempt failure
        System.err.println("Primary connection failed: " + e.getMessage());
        throw e;
    }
}

private Socket createSocket(String host, int port, int timeout) throws IOException {
    InetAddress inetAddress = InetAddress.getByName(host);
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress(inetAddress, port), timeout);
    return socket;
}

private boolean isServerAvailable(String host) {
    try {
        InetAddress inetAddress = InetAddress.getByName(host);
        return inetAddress.isReachable(5000);
    } catch (IOException e) {
        return false;
    }
}
//<End of snippet n. 0>