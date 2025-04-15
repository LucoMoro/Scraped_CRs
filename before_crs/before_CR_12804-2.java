/*Trim the mime type portion of Content-Type.

This change allows emails with a "Content-Type: text/plain ; charset=iso-8859-1" header to be parsed as plain text.
Added unit tests for whitespace and the charset parameter.*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 1b0cb9f..2e106df 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
}
String[] parts = unfold(header).split(";");
if (name == null) {
            return parts[0];
}
String lowerCaseName = name.toLowerCase();
for (String part : parts) {








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/internet/MimeMessageTest.java b/tests/src/com/android/email/mail/internet/MimeMessageTest.java
//Synthetic comment -- index be82f96..b1c6206 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//Synthetic comment -- @@ -420,5 +422,46 @@
}
}

// TODO more test for writeTo()
}








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/internet/MimeUtilityTest.java b/tests/src/com/android/email/mail/internet/MimeUtilityTest.java
//Synthetic comment -- index 5f53f63..affb9f1 100644

//Synthetic comment -- @@ -350,7 +350,120 @@
gotText = MimeUtility.getTextFromPart(p);
assertEquals(theText, gotText);
}
    // TODO: Tests of charset decoding in getTextFromPart()

/** Tests for various aspects of mimeTypeMatches(String mimeType, String matchAgainst) */
public void testMimeTypeMatches() {







