/*If STARTTLS is on last EHLO response line, accept space prefix.

When receiving the EHLO response from the SMTP server, the multiline
answer has "-" prefix in all lines except the last line, where the
prefix is a blank. This is according to RFC 2821 section 4.2.1. This has
also been reported as issue 2309 at code.google.com.*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index b7f585a..ba6f5e4 100644

//Synthetic comment -- @@ -151,7 +151,7 @@
* if not.
*/
if (mTransport.canTryTlsSecurity()) {
                if (result.matches("[ -]STARTTLS")) {
executeSimpleCommand("STARTTLS");
mTransport.reopenTls();
/*







