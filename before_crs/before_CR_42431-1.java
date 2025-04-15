/*SDK Manager: fix support for file:// URLs in UrlOpener.

Change-Id:I5996187d0ccd002d9ec3fe8c0fa5d17a96671431*/
//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 96cb003..aa592b7 100644

//Synthetic comment -- @@ -22,13 +22,25 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.utils.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
//Synthetic comment -- @@ -286,4 +298,65 @@
}
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 0667b74..dbb8c62 100755

//Synthetic comment -- @@ -283,6 +283,8 @@
* For details on realm authentication and user/password handling,
* check the underlying {@link UrlOpener#openUrl(String, boolean, ITaskMonitor, Header[])}
* documentation.
*
* @param urlString the URL string to be opened.
* @param headers An optional set of headers to pass when requesting the resource. Can be null.
//Synthetic comment -- @@ -315,6 +317,49 @@
}

/**
* Downloads a small file, typically XML manifests.
* The current {@link Strategy} governs whether the file is served as-is
* from the cache, potentially updated first or directly downloaded.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index baccc27..52724c7 100644

//Synthetic comment -- @@ -175,6 +175,7 @@
@Nullable Header[] headers)
throws IOException, CanceledByUserException {

Pair<InputStream, HttpResponse> result = null;

try {
//Synthetic comment -- @@ -186,6 +187,11 @@
// it could use a better message.
throw new IOException("Unknown Host " + e.getMessage(), e);

} catch (IOException e) {
throw e;

//Synthetic comment -- @@ -194,19 +200,24 @@
throw e;

} catch (Exception e) {
            // If the protocol is not supported by HttpClient (e.g. file:///),
            // revert to the standard java.net.Url.open.
if (DEBUG) {
System.out.printf("[HttpClient Error] %s : %s\n", url, e.toString());
}

try {
result = openWithUrl(url, headers);
            } catch (IOException e2) {
                throw e2;
            } catch (Exception e2) {
                if (DEBUG && !e.equals(e2)) {
                    System.out.printf("[Url Error] %s : %s\n", url, e2.toString());
}
}
}







