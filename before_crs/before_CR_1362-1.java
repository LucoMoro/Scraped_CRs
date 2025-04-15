/*Perform case-insensitive matches in mimeTypeMatches to support User-Agent's which use uppercased Content-Type.*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 46e3eb2..69564fb 100644

//Synthetic comment -- @@ -6,6 +6,7 @@
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.decoder.Base64InputStream;
//Synthetic comment -- @@ -189,7 +190,9 @@
* @return
*/
public static boolean mimeTypeMatches(String mimeType, String matchAgainst) {
        return mimeType.matches(matchAgainst.replaceAll("\\*", "\\.\\*"));
}

/**
//Synthetic comment -- @@ -201,9 +204,9 @@
*/
public static boolean mimeTypeMatches(String mimeType, String[] matchAgainst) {
for (String matchType : matchAgainst) {
            if (mimeType.matches(matchType.replaceAll("\\*", "\\.\\*"))) {
                return true;
            }
}
return false;
}







