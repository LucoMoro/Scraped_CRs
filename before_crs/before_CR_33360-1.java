/*Fix STARTTLS handshake error in Email's SmtpSender class

The Email.apk checks for "-STARTTLS" in the SMTP server's EHLO response
but SMTP servers may respond with STARTTLS on the last line as such:

250 STARTTLS

Or even as part of a line as such:

250-XSECURITY=NONE,STARTTLS

Checking for "-STARTTLS" in this case will break. The fix is to simply check
for "STARTTLS" instead.

Seehttp://code.google.com/p/android/issues/detail?id=19109Andhttp://code.google.com/p/android/issues/detail?id=2309Change-Id:I3a590a4398cb664f46875650550986a67f320f76*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index 71a8630..a9b13a6 100644

//Synthetic comment -- @@ -136,7 +136,7 @@
* if not.
*/
if (mTransport.canTryTlsSecurity()) {
                if (result.contains("-STARTTLS")) {
executeSimpleCommand("STARTTLS");
mTransport.reopenTls();
/*







