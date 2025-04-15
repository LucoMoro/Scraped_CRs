/*Add support for chunked encoding when downloading MMS PDUs

Some networks use chunked encoding for MMS delivery, meaning that
the content length is not known beforehand. The Android MMS
application did not support chunked encoding, so downloading MMS
on these networks would simply fail.

The change adds chunked encoding functionality by reading data
from the stream until EOF, instead of relying on the Content-Length
HTTP header.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/HttpUtils.java b/src/com/android/mms/transaction/HttpUtils.java
//Synthetic comment -- index 9d73461..1821c0b 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.util.Config;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
//Synthetic comment -- @@ -55,6 +56,8 @@
public static final int HTTP_POST_METHOD = 1;
public static final int HTTP_GET_METHOD = 2;

// This is the value to use for the "Accept-Language" header.
// Once it becomes possible for the user to change the locale
// setting, this should no longer be static.  We should call
//Synthetic comment -- @@ -205,11 +208,35 @@

HttpEntity entity = response.getEntity();
byte[] body = null;
if (entity != null) {
try {
                    if (entity.getContentLength() > 0) {
body = new byte[(int) entity.getContentLength()];
                        DataInputStream dis = new DataInputStream(entity.getContent());
try {
dis.readFully(body);
} finally {







