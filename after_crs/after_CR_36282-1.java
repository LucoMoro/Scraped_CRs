/*Change-Id:Iaa27dce2d4f66e50800fb6c1e542f4e9cc2a01f1Signed-off-by: Jun-ya Kato <kato@win6.jp>*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/MailTransport.java b/src/com/android/email/mail/transport/MailTransport.java
//Synthetic comment -- index 751be50..0aa5525 100644

//Synthetic comment -- @@ -165,13 +165,13 @@
}

try {
if (canTrySslSecurity()) {
                mSocket = SSLUtils.getSSLSocketFactory(canTrustAllCertificates())
                        .createSocket(getHost(), getPort());
} else {
                mSocket = new Socket(getHost(), getPort());
}
            mSocket.setSoTimeout(SOCKET_CONNECT_TIMEOUT);
// After the socket connects to an SSL server, confirm that the hostname is as expected
if (canTrySslSecurity() && !canTrustAllCertificates()) {
verifyHostname(mSocket, getHost());








//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index 71a8630..a9b13a6 100644

//Synthetic comment -- @@ -136,7 +136,7 @@
* if not.
*/
if (mTransport.canTryTlsSecurity()) {
                if (result.contains("STARTTLS")) {
executeSimpleCommand("STARTTLS");
mTransport.reopenTls();
/*







