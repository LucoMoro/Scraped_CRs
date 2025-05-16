
//<Beginning of snippet n. 0>


mSocket = new Socket();
}
mSocket.connect(socketAddress, SOCKET_CONNECT_TIMEOUT);
mIn = new BufferedInputStream(mSocket.getInputStream(), 1024);
mOut = new BufferedOutputStream(mSocket.getOutputStream(), 512);


//<End of snippet n. 0>








