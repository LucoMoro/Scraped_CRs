/*Get the text in title tag of loaderror.html as expected value to prevent locale issue.

Change-Id:I5be1a4b04a09e7bc974a421bcb40e240ebb91a45*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/TestHtmlConstants.java b/tests/tests/webkit/src/android/webkit/cts/TestHtmlConstants.java
//Synthetic comment -- index 63354d4..dfdcf2b 100644

//Synthetic comment -- @@ -16,6 +16,13 @@

package android.webkit.cts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

/**
* This class defines constants for webkit test assets.
*/
//Synthetic comment -- @@ -73,4 +80,24 @@
}
return "file:///android_asset/" + assetName;
}

    //get the string from loaderror.html
    public static final String getTitleString(Context c) {
        String beginTag = "<title>";
        String endTag = "</title>";
        InputStream is = c.getResources().openRawResource(com.android.internal.R.raw.loaderror);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().substring(sb.toString().indexOf(beginTag) + beginTag.length(),
                sb.toString().indexOf(endTag));
    }

}








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 1bc8a4f..122bdff 100644

//Synthetic comment -- @@ -218,7 +218,7 @@

// Files on the file system should not be loaded.
mOnUiThread.loadUrlAndWaitForCompletion(TestHtmlConstants.LOCAL_FILESYSTEM_URL);
        assertEquals(TestHtmlConstants.getTitleString(getInstrumentation().getContext()), mOnUiThread.getTitle());
}

public void testAccessCacheMode() throws Throwable {







