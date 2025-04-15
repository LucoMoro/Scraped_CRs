/*If STARTTLS is on last EHLO response line, accept space prefix.

When receiving the EHLO response from the SMTP server, the multiline
answer has "-" prefix in all lines except the last line, where the
prefix is a blank. This is according to RFC 2821 section 4.2.1. This has
also been reported as issue 2309 at code.google.com.

Change-Id:I379d362e46184d2e2470020fe6584cfab5fc0a2b*/
//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index fd18e9b..1243a10 100644

//Synthetic comment -- @@ -91,6 +91,8 @@
/**
* TODO: Test with SSL negotiation (faked)
* TODO: Test with SSL required but not supported
* TODO: Test other capabilities.
* TODO: Test AUTH LOGIN
*/
//Synthetic comment -- @@ -99,44 +101,48 @@
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
//Synthetic comment -- @@ -156,8 +162,9 @@
fail("Should not be able to open() without TLS.");
} catch (MessagingException me) {
// good - expected
}
    }

/**
* Test:  Open and send a single message (sunny day)







