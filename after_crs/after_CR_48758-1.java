/*Connection to RTSP is failing.

Add a new feature to support rtsp type

Change-Id:Ib44b1974ee0d14aeafcfe49ea72b04f713bf0320Author: bxu10X <bxu10X@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18856*/




//Synthetic comment -- diff --git a/src/com/android/browser/IntentHandler.java b/src/com/android/browser/IntentHandler.java
//Synthetic comment -- index f0998a4..ea4c069 100644

//Synthetic comment -- @@ -30,6 +30,10 @@
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.Log;
import android.widget.Toast;
import android.widget.EditText;
import android.content.ActivityNotFoundException;

import com.android.browser.UI.ComboViews;
import com.android.browser.search.SearchEngine;
//Synthetic comment -- @@ -107,6 +111,11 @@
urlData = new UrlData(mSettings.getHomePage());
}

            if (launchRtspIfNeeded(urlData.mUrl)) {
                Log.i("rtsp", "Launch media player.");
                return;
            }

if (intent.getBooleanExtra(Browser.EXTRA_CREATE_NEW_TAB, false)
|| urlData.isPreloaded()) {
Tab t = mController.openTab(urlData);
//Synthetic comment -- @@ -202,6 +211,25 @@
}
}

    protected boolean launchRtspIfNeeded(String url) {
        if (!url.startsWith("rtsp://")) {
            return false;
        }
        Log.i("rtsp", "onNewIntent, starts with rtsp, will launch media player.");
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
        i.addCategory(Intent.CATEGORY_BROWSABLE);
        try {
            mActivity.startActivityIfNeeded(i, -1);
        } catch (ActivityNotFoundException ex) {
            // ignore the error. If no application can handle the URL,
            // eg about:blank, assume the browser can handle it.
            Toast.makeText(mActivity,
                    com.android.internal.R.string.httpErrorUnsupportedScheme,
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

protected static UrlData getUrlDataFromIntent(Intent intent) {
String url = "";
Map<String, String> headers = null;








//Synthetic comment -- diff --git a/src/com/android/browser/UrlUtils.java b/src/com/android/browser/UrlUtils.java
//Synthetic comment -- index e24000c..60ad424 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
static final Pattern ACCEPTED_URI_SCHEMA = Pattern.compile(
"(?i)" + // switch on case insensitive matching
"(" +    // begin group for schema
            "(?:http|https|file|rtsp):\\/\\/" +
"|(?:inline|data|about|javascript):" +
")" +
"(.*)" );







