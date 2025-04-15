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
//Synthetic comment -- index be82f96..b1c6206 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//Synthetic comment -- @@ -420,5 +422,46 @@
}
}

    public void testWhiteSpace() throws MessagingException, IOException {
        // The lines up to Content-Type were copied directly out of RFC 2822
        // "Section A.5. White space, comments, and other oddities"

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


        // Error: The parentheeses in the middle of email addresses are not removed.
        //assertEquals("c@public.example", toAddresses[0].getAddress());
        // Error: The parentheeses in the middle of email addresses are not removed.
        //assertEquals("pete@silly.test",mm.getFrom()[0].getAddress());
    }

// TODO more test for writeTo()
}








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/internet/MimeUtilityTest.java b/tests/src/com/android/email/mail/internet/MimeUtilityTest.java
//Synthetic comment -- index 5f53f63..affb9f1 100644

//Synthetic comment -- @@ -350,7 +350,120 @@
gotText = MimeUtility.getTextFromPart(p);
assertEquals(theText, gotText);
}

    /** Tests for getTextFromPart(Part part) and getHeaderParameter(String,String) */
    /* From RFC 2045:
       The type, subtype, and parameter names are not case sensitive.  For
          example, TEXT, Text, and TeXt are all equivalent top-level media
          types.  Parameter values are normally case sensitive, but sometimes
          are interpreted in a case-insensitive fashion, depending on the
          intended use.
    */
    public void testContentTypeCharset() throws MessagingException {
        final String theText = "This is some happy unicode text \u263a";
        // What you get if you encode to UTF-8 (\xe2\x98\xba) and reencode with Windows-1252
        final String windows1252Text =
            "This is some happy unicode text \u00e2\u02dc\u00ba";
        TextBody tb = new TextBody(theText);
        MimeBodyPart p = new MimeBodyPart();

        // We call setHeader after setBody, since setBody overwrites Content-Type
        String gotText, mimeType, charset;
        // 0a. test common case
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html; charset=utf-8");
        gotText = MimeUtility.getTextFromPart(p);
        mimeType = p.getMimeType();
        assertTrue(mimeType, MimeUtility.mimeTypeMatches(mimeType, "text/html"));
        charset = MimeUtility.getHeaderParameter(p.getContentType(), "charset");
        assertEquals(charset, "utf-8");
        assertEquals(theText, gotText);

        // 0b. test common case (windows 1252)
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html; charset=windows-1252");
        gotText = MimeUtility.getTextFromPart(p);
        mimeType = p.getMimeType();
        assertTrue(mimeType, MimeUtility.mimeTypeMatches(mimeType, "text/html"));
        charset = MimeUtility.getHeaderParameter(p.getContentType(), "charset");
        assertEquals(charset, "windows-1252");
        assertEquals(windows1252Text, gotText);

        // 1a. Quotes in Content-Type (from RFC 2045)
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html; charset = \"utf-8\"");
        gotText = MimeUtility.getTextFromPart(p);
        assertEquals(theText, gotText);

        // 1b. Quotes in Content-Type (from RFC 2045)
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html; charset = \"windows-1252\"");
        gotText = MimeUtility.getTextFromPart(p);
        mimeType = p.getMimeType();
        assertTrue(mimeType, MimeUtility.mimeTypeMatches(mimeType, "text/html"));
        charset = MimeUtility.getHeaderParameter(p.getContentType(), "charset");
        assertEquals(charset, "windows-1252");
        assertEquals(windows1252Text, gotText);

        // 2a. test spaces and additional options
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html ; charset=utf-8 ; format=flowed ; delsp=yes");
        gotText = MimeUtility.getTextFromPart(p);
        assertEquals(theText, gotText);

        // 2b. test spaces and additional options
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html ; charset=windows-1252 ; format=flowed ; delsp=yes");
        gotText = MimeUtility.getTextFromPart(p);
        mimeType = p.getMimeType();
        assertTrue(mimeType, MimeUtility.mimeTypeMatches(mimeType, "text/html"));
        charset = MimeUtility.getHeaderParameter(p.getContentType(), "charset");
        assertEquals(charset, "windows-1252");
        assertEquals(windows1252Text, gotText);

        // 3a. mixed case is OK
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "TEXT/HtmL ; CHARseT=UTF-8");
        gotText = MimeUtility.getTextFromPart(p);
        assertEquals(theText, gotText);

        // 3b. mixed case is OK
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "TEXT/HtmL ; CHARseT=WINDOWS-1252");
        gotText = MimeUtility.getTextFromPart(p);
        mimeType = p.getMimeType();
        assertTrue(mimeType, MimeUtility.mimeTypeMatches(mimeType, "text/html"));
        charset = MimeUtility.getHeaderParameter(p.getContentType(), "charset");
        assertEquals(charset, "WINDOWS-1252");
        assertEquals(windows1252Text, gotText);

// Error: These tests do not pass.
// Thunderbird permits comments after the end of a parameter, as in this example.
// Not something that I have seen in the real world outside RFC 2045.
/*
        // 4a. Comments in Content-Type header field (from RFC 2045)
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html; charset=utf-8 (Plain text)");
        gotText = MimeUtility.getTextFromPart(p);
        assertEquals(theText, gotText);

        // 4b. Comments in Content-Type header field (from RFC 2045)
        p.setBody(tb);
        p.setHeader(MimeHeader.HEADER_CONTENT_TYPE,
            "text/html; charset=Windows-1252 (Plain text)");
        gotText = MimeUtility.getTextFromPart(p);
        assertEquals(windows1252Text, gotText);
*/
    }

/** Tests for various aspects of mimeTypeMatches(String mimeType, String matchAgainst) */
public void testMimeTypeMatches() {







