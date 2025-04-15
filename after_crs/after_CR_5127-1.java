/*Add 'x-wap-profile' to http headers.*/




//Synthetic comment -- diff --git a/core/java/android/webkit/FrameLoader.java b/core/java/android/webkit/FrameLoader.java
//Synthetic comment -- index ebfebd0..cffe286 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.net.http.EventHandler;
import android.net.http.RequestHandle;
import android.provider.Settings;
import android.util.Config;
import android.util.Log;
import android.webkit.CacheManager.CacheResult;
//Synthetic comment -- @@ -53,6 +54,10 @@

private static final String LOGTAG = "webkit";

    // for UAProf: add by Eden_Huang
    private static final String HDR_KEY_UAPROFILE = "x-wap-profile";
    private static String UAPROFILE_URL;
    
/*
* Construct the Accept_Language once. If the user changes language, then
* the phone will be rebooted.
//Synthetic comment -- @@ -90,6 +95,11 @@
mIsHighPriority = highPriority;
mCacheMode = WebSettings.LOAD_NORMAL;
mUserAgent = userAgent;
        
        // for UAProf: add by Eden_Huang
        UAPROFILE_URL = Settings.Gservices.getString(
                    mListener.getContext().getContentResolver(),
                    Settings.Gservices.MMS_X_WAP_PROFILE_URL);
}

public void setReferrer(String ref) {
//Synthetic comment -- @@ -362,6 +372,14 @@
}

mHeaders.put("User-Agent", mUserAgent);
        
        // for UAProf: add by Eden_Huang
        if ((UAPROFILE_URL != null) && (UAPROFILE_URL.length() != 0)) {
            String uaProf = mHeaders.get(HDR_KEY_UAPROFILE);
            if (uaProf == null || uaProf.length() == 0) {
                mHeaders.put(HDR_KEY_UAPROFILE, UAPROFILE_URL);
            }
        }
}

/**







