//<Beginning of snippet n. 0>
mSocket = new Socket();
mSocket.connect(socketAddress, SOCKET_CONNECT_TIMEOUT);
mSocket.setSoTimeout(SOCKET_READ_TIMEOUT); // Set the socket read timeout
mIn = new BufferedInputStream(mSocket.getInputStream(), 1024);
mOut = new BufferedOutputStream(mSocket.getOutputStream(), 512);

try {
    // Perform read operation
} catch (SocketTimeoutException e) {
    // Handle timeout - resource cleanup and UI notification
    // e.g., show notification to user and stop progress indicator
} finally {
    // Ensure that resources are released if needed
}
//<End of snippet n. 0>