/*Changes adhering to Andy Standler's suggestions for ChangeI2e1c230c.

Change-Id:I5f34afa7e0abbeb646c59dfcbed58ff660f5b127*/
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
//Synthetic comment -- index 0333b94..ee084ca 100644

//Synthetic comment -- @@ -40,6 +40,8 @@

private boolean mSslAllowed = false;
private boolean mTlsAllowed = false;

private boolean mOpen;
private boolean mInputOpen;
//Synthetic comment -- @@ -240,10 +242,31 @@
return line;
}

    public void reopenTls() /* throws MessagingException */ {
SmtpSenderUnitTests.assertTrue(mOpen);
SmtpSenderUnitTests.assertTrue(mTlsAllowed);
        SmtpSenderUnitTests.fail("reopenTls() not implemented");
}

public void setSecurity(int connectionSecurity, boolean trustAllCertificates) {
//Synthetic comment -- @@ -339,4 +362,4 @@
}
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index 5331560..1243a10 100644

//Synthetic comment -- @@ -96,7 +96,76 @@
* TODO: Test other capabilities.
* TODO: Test AUTH LOGIN
*/
    
/**
* Test:  Open and send a single message (sunny day)
*/







