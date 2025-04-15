/*Set timeout for blocking read. If connection is lost while
reading data from socket, the thread blocks and subsequent
commands will never have a chance to run. UI progress will
keep spinning for a long time.

Change-Id:I5a3d096c8eb5d923f58be512670c7aabd2e4faea*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/MailTransport.java b/src/com/android/email/mail/transport/MailTransport.java
//Synthetic comment -- index 420d3de..80d00e1 100644

//Synthetic comment -- @@ -153,6 +153,7 @@
mSocket = new Socket();
}
mSocket.connect(socketAddress, SOCKET_CONNECT_TIMEOUT);
mIn = new BufferedInputStream(mSocket.getInputStream(), 1024);
mOut = new BufferedOutputStream(mSocket.getOutputStream(), 512);








