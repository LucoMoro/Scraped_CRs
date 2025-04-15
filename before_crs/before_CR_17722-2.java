/*Add support for chunked encoding when downloading MMS PDUs

Some networks use chunked encoding for MMS delivery, meaning that
the content length is not known beforehand. The Android MMS
application did not support chunked encoding, so downloading MMS
on these networks would simply fail.

The change adds chunked encoding functionality by reading data
from the stream until EOF, instead of relying on the Content-Length
HTTP header.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/HttpUtils.java b/src/com/android/mms/transaction/HttpUtils.java
//Synthetic comment -- index 9d73461..168803f 100644

//Synthetic comment -- @@ -39,7 +39,8 @@
import android.util.Config;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
//Synthetic comment -- @@ -55,6 +56,8 @@
public static final int HTTP_POST_METHOD = 1;
public static final int HTTP_GET_METHOD = 2;

// This is the value to use for the "Accept-Language" header.
// Once it becomes possible for the user to change the locale
// setting, this should no longer be static.  We should call
//Synthetic comment -- @@ -207,23 +210,26 @@
byte[] body = null;
if (entity != null) {
try {
                    if (entity.getContentLength() > 0) {
                        body = new byte[(int) entity.getContentLength()];
                        DataInputStream dis = new DataInputStream(entity.getContent());
try {
                            dis.readFully(body);
                        } finally {
                            try {
                                dis.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Error closing input stream: " + e.getMessage());
                            }
}
}
} finally {
                    if (entity != null) {
                        entity.consumeContent();
                    }
}
}
return body;







