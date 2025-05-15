//<Beginning of snippet n. 0>
SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
SSLSocket mSocket = (SSLSocket) factory.createSocket();
mSocket.connect(socketAddress, SOCKET_CONNECT_TIMEOUT);
mSocket.setSoTimeout(5000); // Set timeout for read operations
mIn = new BufferedInputStream(mSocket.getInputStream(), 1024);
mOut = new BufferedOutputStream(mSocket.getOutputStream(), 512);
//<End of snippet n. 0>