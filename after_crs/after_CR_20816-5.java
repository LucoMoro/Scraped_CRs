/*Fix a decoding problem on the Email body

When decoding the Email body, email application used google default base64
function.  But it caused problem, so particular email did not sync any more.
So we have changed decoding function as a decoder from apache in order
not to occur problem

Change-Id:I7581123f21fbb4015153ca6f4a0c14c0f6a769fcSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 47cd615..008328a 100644

//Synthetic comment -- @@ -26,13 +26,12 @@

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.decoder.Base64InputStream;
import org.apache.james.mime4j.decoder.DecoderUtil;
import org.apache.james.mime4j.decoder.QuotedPrintableInputStream;
import org.apache.james.mime4j.util.CharsetUtil;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -361,7 +360,7 @@
in = new QuotedPrintableInputStream(in);
}
else if ("base64".equalsIgnoreCase(contentTransferEncoding)) {
                in = new Base64InputStream(in);
}
}









//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/internet/MimeUtilityTest.java b/tests/src/com/android/email/mail/internet/MimeUtilityTest.java
//Synthetic comment -- index 8052a62..b0a82fe 100644

//Synthetic comment -- @@ -24,6 +24,10 @@
import com.android.email.mail.MessageTestUtils.MessageBuilder;
import com.android.email.mail.MessageTestUtils.MultipartBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;
//Synthetic comment -- @@ -49,6 +53,16 @@

/** a string without any unicode */
private final String SHORT_PLAIN = "abcd";

    /** Good base64 string */
    private final String GOOD_BASE64 =
                              "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /** Bad base64 string */
    private final String BAD_BASE64 = "CgpTZW50IGZyb20gU2Ftc3VuZyB0YWJsZXQ=";

    /** Encoding type */
    private final String BASE64 = "base64";

/** long subject which will be split into two MIME/Base64 chunks */
private final String LONG_UNICODE_SPLIT =
//Synthetic comment -- @@ -501,7 +515,20 @@
assertTrue(MimeUtility.mimeTypeMatches("match/this", arrayTwo));
}

    // TODO:  tests for decodeBody(InputStream in, String contentTransferEncoding)

    /**
     * Confirm that decodeBody() does not crash when
     * given bad Base64 data.
     */
    public void testDecodeBody_BadBase64() throws IOException {
        InputStream in = new ByteArrayInputStream(GOOD_BASE64.getBytes("us-ascii"));
        assertNotNull(MimeUtility.decodeBody(in, BASE64));

        in = new ByteArrayInputStream(BAD_BASE64.getBytes("us-ascii"));
        assertNotNull(MimeUtility.decodeBody(in, BASE64));
    }

// TODO:  tests for collectParts(Part part, ArrayList<Part> viewables, ArrayList<Part> attachments)

}







