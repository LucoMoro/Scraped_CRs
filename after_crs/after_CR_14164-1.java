/*If STARTTLS is on last EHLO response line, accept space prefix.

When receiving the EHLO response from the SMTP server, the multiline
answer has "-" prefix in all lines except the last line, where the
prefix is a blank. This is according to RFC 2821 section 4.2.1. This has
also been reported as issue 2309 at code.google.com.

Change-Id:I1a1abba58d8b82e832567a0eeec7029152d68369*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index c785e7f..a253c77 100644

//Synthetic comment -- @@ -140,7 +140,7 @@
* if not.
*/
if (mTransport.canTryTlsSecurity()) {
                if (result.contains("-STARTTLS") || result.contains(" STARTTLS")) {
executeSimpleCommand("STARTTLS");
mTransport.reopenTls();
/*







