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

import com.android.browser.UI.ComboViews;
import com.android.browser.search.SearchEngine;
//Synthetic comment -- @@ -107,6 +111,11 @@
urlData = new UrlData(mSettings.getHomePage());
}

if (intent.getBooleanExtra(Browser.EXTRA_CREATE_NEW_TAB, false)
|| urlData.isPreloaded()) {
Tab t = mController.openTab(urlData);
//Synthetic comment -- @@ -202,6 +211,25 @@
}
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
            "(?:http|https|file):\\/\\/" +
"|(?:inline|data|about|javascript):" +
")" +
"(.*)" );







