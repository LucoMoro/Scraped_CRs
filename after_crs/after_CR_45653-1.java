/*Attempt to set timeout for URL connection

The timeout apparently isn't respected on all implementations.
Also adds a cache, since this method is called repeatedly
for each container (one per project?).

Change-Id:I2a818815c706bd1a1ec182e38c52543079dad9e1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index bb1e40a..f3d6371 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
//Synthetic comment -- @@ -51,8 +53,10 @@
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -526,22 +530,36 @@
return androidSourceProperty;
}

    /**
     * Cache results for testURL: Some are expensive to compute, and this is
     * called repeatedly (perhaps for each open project)
     */
    private static final Map<String, Boolean> sRecentUrlValidCache =
            Maps.newHashMapWithExpectedSize(4);

    @SuppressWarnings("resource") // Eclipse does not handle Closeables#closeQuietly
private static boolean testURL(String androidApiURL) {
        Boolean cached = sRecentUrlValidCache.get(androidApiURL);
        if (cached != null) {
            return cached.booleanValue();
        }
boolean valid = false;
InputStream is = null;
try {
URL testURL = new URL(androidApiURL);
            URLConnection connection = testURL.openConnection();
            // Only try for 5 seconds (though some implementations ignore this flag)
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            is = connection.getInputStream();
valid = true;
} catch (Exception ignore) {
} finally {
            Closeables.closeQuietly(is);
}

        sRecentUrlValidCache.put(androidApiURL, valid);

return valid;
}








