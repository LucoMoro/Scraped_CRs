/*Mms: Support HTTP chunked transfer-encoding

Falis to download Mms if the server only supports chuncked data
transfer. Fix this issue by adding chunked transfer support.

Change-Id:Id99576392ff843af6d0711ba8cb625bff4f760f6*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/HttpUtils.java b/src/com/android/mms/transaction/HttpUtils.java
//Synthetic comment -- index 9d73461..5391469 100644

//Synthetic comment -- @@ -39,7 +39,9 @@
import android.util.Config;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
//Synthetic comment -- @@ -52,6 +54,7 @@
private static final boolean DEBUG = false;
private static final boolean LOCAL_LOGV = DEBUG ? Config.LOGD : Config.LOGV;

    private static final int BUF_SIZE = 4096;
public static final int HTTP_POST_METHOD = 1;
public static final int HTTP_GET_METHOD = 2;

//Synthetic comment -- @@ -219,6 +222,28 @@
Log.e(TAG, "Error closing input stream: " + e.getMessage());
}
}
                    } else if (entity.isChunked()) {
                        // Deal with Http 1.1 chunked transfer encoding.
                        ByteArrayOutputStream bas = new ByteArrayOutputStream();
                        byte[] tmp = new byte[BUF_SIZE];
                        int readed = 0;
                        try {
                            InputStream is = entity.getContent();
                            while ((readed = is.read(tmp)) != -1) {
                                bas.write(tmp, 0, readed);
                            }
                            body = bas.toByteArray();
                        } catch (Exception e) {
                            Log.e (TAG, "Error reading/writing http entity: " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            try {
                                bas.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "Error closing output streams: " + e.getMessage());
                            }
                        }
}
} finally {
if (entity != null) {







