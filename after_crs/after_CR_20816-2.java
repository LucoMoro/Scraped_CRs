/*Change Email body decoding function as a decoder from apache

When decoding the Email body, email application used google default base64 function.
But it caused problem, so particular email did not sync any more.
So we have changed decoding function as a decoder from apache in order not to occur problem

Issue : Connection error happen when sync specific message

Change-Id:I7581123f21fbb4015153ca6f4a0c14c0f6a769fcSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 47cd615..d2b7f9c 100644

//Synthetic comment -- @@ -31,8 +31,7 @@
import org.apache.james.mime4j.util.CharsetUtil;

import android.util.Log;
import org.apache.james.mime4j.decoder.Base64InputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -361,7 +360,7 @@
in = new QuotedPrintableInputStream(in);
}
else if ("base64".equalsIgnoreCase(contentTransferEncoding)) {
                in = new Base64InputStream(in);
}
}








