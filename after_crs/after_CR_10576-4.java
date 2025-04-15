/*If STARTTLS is on last EHLO response line, accept space prefix.

When receiving the EHLO response from the SMTP server, the multiline
answer has "-" prefix in all lines except the last line, where the
prefix is a blank. This is according to RFC 2821 section 4.2.1. This has
also been reported as issue 2309 at code.google.com.

Change-Id:I3e95528844c82594a85e40f1ac1c3fe24507286c*/




//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/MockTransport.java b/tests/src/com/android/email/mail/transport/MockTransport.java
//Synthetic comment -- index 0333b94..26ffd09 100644

//Synthetic comment -- @@ -40,6 +40,8 @@

private boolean mSslAllowed = false;
private boolean mTlsAllowed = false;

    private boolean mTlsReopened = false;

private boolean mOpen;
private boolean mInputOpen;
//Synthetic comment -- @@ -240,10 +242,30 @@
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
    }

    public boolean getTlsReopened() {
        return mTlsReopened;
}

public void setSecurity(int connectionSecurity, boolean trustAllCertificates) {
//Synthetic comment -- @@ -339,4 +361,4 @@
}
}
}
\ No newline at end of file
}








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index 5331560..fd18e9b 100644

//Synthetic comment -- @@ -91,12 +91,74 @@
/**
* TODO: Test with SSL negotiation (faked)
* TODO: Test with SSL required but not supported
* TODO: Test other capabilities.
* TODO: Test AUTH LOGIN
*/

    /**
     * Confirms TLS login
     */
    public void testTlsLogin() throws MessagingException {

        // test with "250-STARTTLS"
        MockTransport mockDash = new MockTransport();
        mockDash.setSecurity(Transport.CONNECTION_SECURITY_TLS, false);
        mockDash.setTlsAllowed(true);
        mSender.setTransport(mockDash);

        // try to open it
        mockDash.expect(null, "220 MockTransport 2000 Ready To Assist You Peewee");
        mockDash.expect("EHLO .*", "250-10.20.30.40 hello");
        mockDash.expect(null, "250-STARTTLS");
        mockDash.expect(null, "250 AUTH LOGIN PLAIN CRAM-MD5");
        mockDash.expect("STARTTLS", "220 Ready to start TLS");
        mockDash.expect("EHLO .*", "250-10.20.30.40 hello");
        mockDash.expect(null, "250-STARTTLS");
        mockDash.expect(null, "250 AUTH LOGIN PLAIN CRAM-MD5");
        mockDash.expect("AUTH PLAIN .*", "235 2.7.0 ... authentication succeeded");
        mSender.open();
        assertTrue("dash", mockDash.getTlsReopened());

        // test with "250 STARTTLS"
        MockTransport mockSpace = new MockTransport();
        mockSpace.setSecurity(Transport.CONNECTION_SECURITY_TLS, false);
        mockSpace.setTlsAllowed(true);
        mSender.setTransport(mockSpace);

        // try to open it
        mockSpace.expect(null, "220 MockTransport 2000 Ready To Assist You Peewee");
        mockSpace.expect("EHLO .*", "250-10.20.30.40 hello");
        mockSpace.expect(null, "250-AUTH LOGIN PLAIN CRAM-MD5");
        mockSpace.expect(null, "250 STARTTLS");
        mockSpace.expect("STARTTLS", "220 Ready to start TLS");
        mockSpace.expect("EHLO .*", "250-10.20.30.40 hello");
        mockSpace.expect(null, "250-AUTH LOGIN PLAIN CRAM-MD5");
        mockSpace.expect(null, "250 STARTTLS");
        mockSpace.expect("AUTH PLAIN .*", "235 2.7.0 ... authentication succeeded");
        mSender.open();
        assertTrue("space", mockSpace.getTlsReopened());
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
        }
    }

/**
* Test:  Open and send a single message (sunny day)
*/







