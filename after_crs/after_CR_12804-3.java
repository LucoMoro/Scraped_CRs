/*Trim the mime type portion of Content-Type.

This change allows emails with a "Content-Type: text/plain ; charset=iso-8859-1" header to be parsed as plain text.
Added unit tests for whitespace and the charset parameter.*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 1b0cb9f..2e106df 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
}
String[] parts = unfold(header).split(";");
if (name == null) {
            return parts[0].trim();
}
String lowerCaseName = name.toLowerCase();
for (String part : parts) {








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/internet/MimeMessageTest.java b/tests/src/com/android/email/mail/internet/MimeMessageTest.java
//Synthetic comment -- index be82f96..1edd1f2 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//Synthetic comment -- @@ -420,5 +422,48 @@
}
}

    /**
     * Test for parsing headers with extra whitespace and commennts.
     *
     * The lines up to Content-Type were copied directly out of RFC 2822
     * "Section A.5. White space, comments, and other oddities"
     */
    public void testWhiteSpace() throws MessagingException, IOException {
        String entireMessage =
            "From: Pete(A wonderful \\) chap) <pete(his account)@silly.test(his host)>\r\n"+
            "To:A Group(Some people)\r\n"+
            "     :Chris Jones <c@(Chris's host.)public.example>,\r\n"+
            "         joe@example.org,\r\n"+
            "  John <jdoe@one.test> (my dear friend); (the end of the group)\r\n"+
            "Cc:(Empty list)(start)Undisclosed recipients  :(nobody(that I know))  ;\r\n"+
            "Date: Thu,\r\n"+
            "      13\r\n"+
            "        Feb\r\n"+
            "          1969\r\n"+
            "      23:32\r\n"+
            "               -0330 (Newfoundland Time)\r\n"+
            "Message-ID:              <testabcd.1234@silly.test>\r\n"+
            "Content-Type:                \r\n"+
            "          TEXT/hTML \r\n"+
            "       ; x-blah=\"y-blah\" ; \r\n"+
            "       CHARSET=\"us-ascii\" ; (comment)\r\n"+
            "\r\n"+
            "<html><body>Testing.</body></html>\r\n";
        MimeMessage mm = null;
        mm = new MimeMessage(new ByteArrayInputStream(
            entireMessage.getBytes("us-ascii")));
        assertTrue(mm.getMimeType(), MimeUtility.mimeTypeMatches("text/html",mm.getMimeType()));
        assertEquals(new Date(-27723480000L),mm.getSentDate());
        assertEquals("<testabcd.1234@silly.test>",mm.getMessageId());
        Address[] toAddresses = mm.getRecipients(MimeMessage.RecipientType.TO);
        assertEquals("joe@example.org", toAddresses[1].getAddress());
        assertEquals("jdoe@one.test", toAddresses[2].getAddress());


        // Note: The parentheses in the middle of email addresses are not removed.
        //assertEquals("c@public.example", toAddresses[0].getAddress());
        //assertEquals("pete@silly.test",mm.getFrom()[0].getAddress());
    }

// TODO more test for writeTo()
}








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/internet/MimeUtilityTest.java b/tests/src/com/android/email/mail/internet/MimeUtilityTest.java
//Synthetic comment -- index 5f53f63..d648cce 100644

//Synthetic comment -- @@ -350,7 +350,94 @@
gotText = MimeUtility.getTextFromPart(p);
assertEquals(theText, gotText);
}

    /** Test for usage of Content-Type in getTextFromPart(Part part).
     * 
     * For example 'Content-Type: text/html; charset=utf-8'
     * 
     *  If the body part has no mime-type, refuses to parse content as text.
     *  If the mime-type does not match text/*, it will not get parsed.
     *  Then, the charset parameter is used, with a default of ASCII.
     *
     *  This test works by using a string that is valid Unicode, and is also
     *  valid when decoded from UTF-8 bytes into Windows-1252 (so that
     *  auto-detection is not possible), and checks that the correct conversion
     *  was made, based on the Content-Type header.
     *  
     */
    public void testContentTypeCharset() throws MessagingException {
        final String UNICODE_EXPECT = "This is some happy unicode text \u263a";
        // What you get if you encode to UTF-8 (\xe2\x98\xba) and reencode with Windows-1252
        final String WINDOWS1252_EXPECT = "This is some happy unicode text \u00e2\u02dc\u00ba";
        TextBody tb = new TextBody(UNICODE_EXPECT);
        MimeBodyPart p = new MimeBodyPart();

        String gotText, mimeType, charset;
        // TEST 0: Standard Content-Type header; no extraneous spaces or fields
        p.setBody(tb);
        // We call setHeader after setBody, since setBody overwrites Content-Type
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE, "text/html; charset=utf-8");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        assertEquals(UNICODE_EXPECT, gotText);

        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE, "text/html; charset=windows-1252");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        assertEquals(WINDOWS1252_EXPECT, gotText);

        // TEST 1: Extra fields and quotes in Content-Type (from RFC 2045)
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
                    "text/html; prop1 = \"test\"; charset = \"utf-8\"; prop2 = \"test\"");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        assertEquals(UNICODE_EXPECT, gotText);

        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
                    "text/html; prop1 = \"test\"; charset = \"windows-1252\"; prop2 = \"test\"");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        assertEquals(WINDOWS1252_EXPECT, gotText);

        // TEST 2: Mixed case in Content-Type header:
        // RFC 2045 says that content types, subtypes and parameter names
        // are case-insensitive.

        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE, "TEXT/HtmL ; CHARseT=utf-8");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        assertEquals(UNICODE_EXPECT, gotText);

        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE, "TEXT/HtmL ; CHARseT=windows-1252");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        assertEquals(WINDOWS1252_EXPECT, gotText);

        // TEST 3: Comments in Content-Type header field (from RFC 2045)
        // Thunderbird permits comments after the end of a parameter, as in this example.
        // Not something that I have seen in the real world outside RFC 2045.

        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
                    "text/html; charset=utf-8 (Plain text)");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        // Note: This test does not pass.
        //assertEquals(UNICODE_EXPECT, gotText);

        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
                    "text/html; charset=windows-1252 (Plain text)");
        gotText = MimeUtility.getTextFromPart(p);
        assertTrue(MimeUtility.mimeTypeMatches(p.getMimeType(), "text/html"));
        // Note: These tests does not pass.
        //assertEquals(WINDOWS1252_EXPECT, gotText);
    }

/** Tests for various aspects of mimeTypeMatches(String mimeType, String matchAgainst) */
public void testMimeTypeMatches() {







