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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
//Synthetic comment -- @@ -51,8 +53,10 @@
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -526,22 +530,36 @@
return androidSourceProperty;
}

private static boolean testURL(String androidApiURL) {
boolean valid = false;
InputStream is = null;
try {
URL testURL = new URL(androidApiURL);
            is = testURL.openStream();
valid = true;
} catch (Exception ignore) {
} finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
}
return valid;
}








