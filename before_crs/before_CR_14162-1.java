/*If STARTTLS is on last EHLO response line, accept space prefix.

When receiving the EHLO response from the SMTP server, the multiline
answer has "-" prefix in all lines except the last line, where the
prefix is a blank. This is according to RFC 2821 section 4.2.1. This has
also been reported as issue 2309 at code.google.com.

Change-Id:I24ae780cce61fd13b79a7d16343838ab1a91abbc*/
//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/MockTransport.java b/tests/src/com/android/email/mail/transport/MockTransport.java
//Synthetic comment -- index ee084ca..26ffd09 100644

//Synthetic comment -- @@ -262,7 +262,6 @@
SmtpSenderUnitTests.assertTrue(mOpen);
SmtpSenderUnitTests.assertTrue(mTlsAllowed);
mTlsReopened = true;
        //SmtpSenderUnitTests.fail("reopenTls() not implemented");
}

public boolean getTlsReopened() {








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index 1243a10..fd18e9b 100644

//Synthetic comment -- @@ -91,8 +91,6 @@
/**
* TODO: Test with SSL negotiation (faked)
* TODO: Test with SSL required but not supported
     * TODO: Test with TLS negotiation (faked)
     * TODO: Test with TLS required but not supported
* TODO: Test other capabilities.
* TODO: Test AUTH LOGIN
*/
//Synthetic comment -- @@ -101,48 +99,44 @@
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
//Synthetic comment -- @@ -162,9 +156,8 @@
fail("Should not be able to open() without TLS.");
} catch (MessagingException me) {
// good - expected
            // TODO maybe expect a particular exception?
}
    }    

/**
* Test:  Open and send a single message (sunny day)







