/*Fix connection behavior to a server with multiple IP addrs

Even if multiple IP addresses of mail server are obtained via DNS,
Email application only attempts to connect to the fisrt IP address and
does not try to connect to the second one and later one (if available).

For instance {smtp,imap,pop}.google.com publish two different IP addresses.
| ;; ANSWER SECTION:
| smtp.gmail.com.         299     IN      CNAME   gmail-smtp-msa.l.google.com.
| gmail-smtp-msa.l.google.com. 299 IN     A       173.194.79.108
| gmail-smtp-msa.l.google.com. 299 IN     A       173.194.79.109

It's better behavior that a TCP client application trys to connect to
all IP addresses for redundancy and load-balancing from ASP operator's
view.

Change-Id:Iaa27dce2d4f66e50800fb6c1e542f4e9cc2a01f1Signed-off-by: Jun-ya Kato <kato@win6.jp>*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/MailTransport.java b/src/com/android/email/mail/transport/MailTransport.java
//Synthetic comment -- index 751be50..0aa5525 100644

//Synthetic comment -- @@ -165,13 +165,13 @@
}

try {
            SocketAddress socketAddress = new InetSocketAddress(getHost(), getPort());
if (canTrySslSecurity()) {
                mSocket = SSLUtils.getSSLSocketFactory(canTrustAllCertificates()).createSocket();
} else {
                mSocket = new Socket();
}
            mSocket.connect(socketAddress, SOCKET_CONNECT_TIMEOUT);
// After the socket connects to an SSL server, confirm that the hostname is as expected
if (canTrySslSecurity() && !canTrustAllCertificates()) {
verifyHostname(mSocket, getHost());







