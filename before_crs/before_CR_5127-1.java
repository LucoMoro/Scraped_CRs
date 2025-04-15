/*Add 'x-wap-profile' to http headers.*/
//Synthetic comment -- diff --git a/core/java/android/webkit/FrameLoader.java b/core/java/android/webkit/FrameLoader.java
//Synthetic comment -- index ebfebd0..cffe286 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.net.http.EventHandler;
import android.net.http.RequestHandle;
import android.util.Config;
import android.util.Log;
import android.webkit.CacheManager.CacheResult;
//Synthetic comment -- @@ -53,6 +54,10 @@

private static final String LOGTAG = "webkit";

/*
* Construct the Accept_Language once. If the user changes language, then
* the phone will be rebooted.
//Synthetic comment -- @@ -90,6 +95,11 @@
mIsHighPriority = highPriority;
mCacheMode = WebSettings.LOAD_NORMAL;
mUserAgent = userAgent;
}

public void setReferrer(String ref) {
//Synthetic comment -- @@ -362,6 +372,14 @@
}

mHeaders.put("User-Agent", mUserAgent);
}

/**







