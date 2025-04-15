/*If STARTTLS is on last EHLO response line, accept space prefix.

When receiving the EHLO response from the SMTP server, the multiline
answer has "-" prefix in all lines except the last line, where the
prefix is a blank. This is according to RFC 2821 section 4.2.1. This has
also been reported as issue 2309 at code.google.com.

Change-Id:I3feccabed30767d2fa5b06352cd7d1c803e8d59c*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index c785e7f..a253c77 100644

//Synthetic comment -- @@ -140,7 +140,7 @@
* if not.
*/
if (mTransport.canTryTlsSecurity()) {
                if (result.contains("-STARTTLS")) {
executeSimpleCommand("STARTTLS");
mTransport.reopenTls();
/*








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/MockTransport.java b/tests/src/com/android/email/mail/transport/MockTransport.java
//Synthetic comment -- index 0333b94..26ffd09 100644

//Synthetic comment -- @@ -40,6 +40,8 @@

private boolean mSslAllowed = false;
private boolean mTlsAllowed = false;

private boolean mOpen;
private boolean mInputOpen;
//Synthetic comment -- @@ -240,10 +242,30 @@
return line;
}

    public void reopenTls() /* throws MessagingException */ {
SmtpSenderUnitTests.assertTrue(mOpen);
SmtpSenderUnitTests.assertTrue(mTlsAllowed);
        SmtpSenderUnitTests.fail("reopenTls() not implemented");
}

public void setSecurity(int connectionSecurity, boolean trustAllCertificates) {
//Synthetic comment -- @@ -339,4 +361,4 @@
}
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index 5331560..fd18e9b 100644

//Synthetic comment -- @@ -91,12 +91,74 @@
/**
* TODO: Test with SSL negotiation (faked)
* TODO: Test with SSL required but not supported
     * TODO: Test with TLS negotiation (faked)
     * TODO: Test with TLS required but not supported
* TODO: Test other capabilities.
* TODO: Test AUTH LOGIN
*/
    
/**
* Test:  Open and send a single message (sunny day)
*/







