//<Beginning of snippet n. 0>
mSocket = new Socket();
mSocket.connect(socketAddress, SOCKET_CONNECT_TIMEOUT);
mSocket.setSoTimeout(READ_TIMEOUT);
mIn = new BufferedInputStream(mSocket.getInputStream(), 1024);
mOut = new BufferedOutputStream(mSocket.getOutputStream(), 512);

try {
    // Read operation goes here
} catch (SocketTimeoutException e) {
    // Provide UI feedback for timeout
    notifyUserTimeout();
}
//<End of snippet n. 0>