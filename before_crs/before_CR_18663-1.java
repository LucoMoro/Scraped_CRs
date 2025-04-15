/*Mms: Support HTTP chunked transfer-encoding

Falis to download Mms if the server only supports chuncked data
transfer. Fix this issue by adding chunked transfer support.

Change-Id:Id99576392ff843af6d0711ba8cb625bff4f760f6*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/HttpUtils.java b/src/com/android/mms/transaction/HttpUtils.java
//Synthetic comment -- index 9d73461..5391469 100644

//Synthetic comment -- @@ -39,7 +39,9 @@
import android.util.Config;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
//Synthetic comment -- @@ -52,6 +54,7 @@
private static final boolean DEBUG = false;
private static final boolean LOCAL_LOGV = DEBUG ? Config.LOGD : Config.LOGV;

public static final int HTTP_POST_METHOD = 1;
public static final int HTTP_GET_METHOD = 2;

//Synthetic comment -- @@ -219,6 +222,28 @@
Log.e(TAG, "Error closing input stream: " + e.getMessage());
}
}
}
} finally {
if (entity != null) {







