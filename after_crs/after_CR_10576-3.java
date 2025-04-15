/*Changes adhering to Andy Standler's suggestions for ChangeI2e1c230c.

Change-Id:I5f34afa7e0abbeb646c59dfcbed58ff660f5b127*/




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








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/MockTransport.java b/tests/src/com/android/email/mail/transport/MockTransport.java
//Synthetic comment -- index 0333b94..ee084ca 100644

//Synthetic comment -- @@ -40,6 +40,8 @@

private boolean mSslAllowed = false;
private boolean mTlsAllowed = false;

    private boolean mTlsReopened = false;

private boolean mOpen;
private boolean mInputOpen;
//Synthetic comment -- @@ -240,10 +242,31 @@
return line;
}

    public void setTlsAllowed(boolean tlsAllowed) {
        mTlsAllowed = tlsAllowed;
    }

    public boolean getTlsAllowed() {
        return mTlsAllowed;
    }

    public void setSslAllowed(boolean sslAllowed) {
        mSslAllowed = sslAllowed;
    }

    public boolean getSslAllowed() {
        return mSslAllowed;
    }

    public void reopenTls() {
SmtpSenderUnitTests.assertTrue(mOpen);
SmtpSenderUnitTests.assertTrue(mTlsAllowed);
        mTlsReopened = true;
        //SmtpSenderUnitTests.fail("reopenTls() not implemented");
    }

    public boolean getTlsReopened() {
        return mTlsReopened;
}

public void setSecurity(int connectionSecurity, boolean trustAllCertificates) {
//Synthetic comment -- @@ -339,4 +362,4 @@
}
}
}
\ No newline at end of file
}








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index 5331560..1243a10 100644

//Synthetic comment -- @@ -96,7 +96,76 @@
* TODO: Test other capabilities.
* TODO: Test AUTH LOGIN
*/

    /**
     * Confirms TLS login
     */
    public void testTlsLogin() throws MessagingException {
        
        // test with "250-STARTTLS"
        MockTransport mtDash = new MockTransport();
        mtDash.setSecurity(Transport.CONNECTION_SECURITY_TLS, false);
        mtDash.setTlsAllowed(true);
        mSender.setTransport(mtDash);

        // try to open it
        mtDash.expect(null, "220 MockTransport 2000 Ready To Assist You Peewee");
        mtDash.expect("EHLO .*", "250-10.20.30.40 hello");
        mtDash.expect(null, "250-AUTH LOGIN PLAIN CRAM-MD5");
        mtDash.expect(null, "250-STARTTLS");
        mtDash.expect(null, "250+OK");
        mtDash.expect("STARTTLS", "220 Ready to start TLS");
        mtDash.expect("EHLO .*", "250-10.20.30.40 hello");
        mtDash.expect(null, "250-AUTH LOGIN PLAIN CRAM-MD5");
        mtDash.expect(null, "250-STARTTLS");
        mtDash.expect(null, "250+OK");
        mtDash.expect("AUTH PLAIN .*", "235 2.7.0 ... authentication succeeded");
        mSender.open();
        if(mtDash.getTlsReopened() == false)
            fail("TLS not reopened.");

        // test with "250 STARTTLS"
        MockTransport mtSpace = new MockTransport();
        mtSpace.setSecurity(Transport.CONNECTION_SECURITY_TLS, false);
        mtSpace.setTlsAllowed(true);
        mSender.setTransport(mtSpace);

        // try to open it
        mtSpace.expect(null, "220 MockTransport 2000 Ready To Assist You Peewee");
        mtSpace.expect("EHLO .*", "250-10.20.30.40 hello");
        mtSpace.expect(null, "250-AUTH LOGIN PLAIN CRAM-MD5");
        mtSpace.expect(null, "250 STARTTLS");
        mtSpace.expect("STARTTLS", "220 Ready to start TLS");
        mtSpace.expect("EHLO .*", "250-10.20.30.40 hello");
        mtSpace.expect(null, "250-AUTH LOGIN PLAIN CRAM-MD5");
        mtSpace.expect(null, "250 STARTTLS");
        mtSpace.expect("AUTH PLAIN .*", "235 2.7.0 ... authentication succeeded");
        mSender.open();
        if(mtSpace.getTlsReopened() == false)
            fail("TLS not reopened.");
    }

    /**
     * Confirms TLS required but not supported
     */
    public void testTlsRequiredNotSupported() throws MessagingException {
        MockTransport mockTransport = new MockTransport();
        mockTransport.setSecurity(Transport.CONNECTION_SECURITY_TLS, false);
        mSender.setTransport(mockTransport);

        // try to open it
        mockTransport.expect(null, "220 MockTransport 2000 Ready To Assist You Peewee");
        mockTransport.expect("EHLO .*", "");
        setupOpen(mockTransport, "");
        try {
            mSender.open();
            fail("Should not be able to open() without TLS.");
        } catch (MessagingException me) {
            // good - expected
            // TODO maybe expect a particular exception?
        }
    }    

/**
* Test:  Open and send a single message (sunny day)
*/







