/*SDK Manager: fix support for file:// URLs in UrlOpener.

Change-Id:I5996187d0ccd002d9ec3fe8c0fa5d17a96671431*/




//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 96cb003..057ad3d 100644

//Synthetic comment -- @@ -22,13 +22,25 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.repository.CanceledByUserException;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.utils.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
//Synthetic comment -- @@ -286,4 +298,65 @@
}
}
}

    public void testLocalFileDownload() throws IOException, CanceledByUserException {
        Main main = new Main();
        main.setLogger(getLog());
        SdkManager sdkman = getSdkManager();
        main.setSdkManager(sdkman);
        getLog().clear();

        IAndroidTarget target = sdkman.getTargets()[0];
        File sourceProps = new File(target.getLocation(), SdkConstants.FN_SOURCE_PROP);
        assertTrue(sourceProps.isFile());

        String urlStr = getFileUrl(sourceProps);
        assertTrue(urlStr.startsWith("file:///"));

        DownloadCache cache = new DownloadCache(Strategy.DIRECT);
        NullTaskMonitor monitor = new NullTaskMonitor(getLog());
        Pair<InputStream, Integer> result = cache.openDirectUrl(urlStr, monitor);
        assertNotNull(result);
        assertEquals(200, result.getSecond().intValue());

        int len = (int) sourceProps.length();
        byte[] buf = new byte[len];
        FileInputStream is = new FileInputStream(sourceProps);
        is.read(buf);
        is.close();
        String expected = new String(buf, "UTF-8");

        buf = new byte[len];
        result.getFirst().read(buf);
        result.getFirst().close();
        String actual = new String(buf, "UTF-8");
        assertEquals(expected, actual);
    }

    private String getFileUrl(File file) throws IOException {
        // Note: to create a file:// URL, one would typically use something like
        // f.toURI().toURL().toString(). However this generates a broken path on
        // Windows, namely "C:\\foo" is converted to "file:/C:/foo" instead of
        // "file:///C:/foo" (i.e. there should be 3 / after "file:"). So we'll
        // do the correct thing manually.

        String path = file.getCanonicalPath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        // A file:// should start with 3 // (2 for file:// and 1 to make it an absolute
        // path. On Windows that should look like file:///C:/. Linux/Mac will already
        // have that leading / in their path so we need to compensate for windows.
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // For some reason the URL class doesn't add the mandatory "//" after
        // the "file:" protocol name, so it has to be hacked into the path.
        URL url = new URL("file", null, "//" + path);  //$NON-NLS-1$ //$NON-NLS-2$
        String result = url.toString();
        return result;

    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 0667b74..e02023d 100755

//Synthetic comment -- @@ -283,6 +283,8 @@
* For details on realm authentication and user/password handling,
* check the underlying {@link UrlOpener#openUrl(String, boolean, ITaskMonitor, Header[])}
* documentation.
     * <p/>
     * The resulting input stream may not support mark/reset.
*
* @param urlString the URL string to be opened.
* @param headers An optional set of headers to pass when requesting the resource. Can be null.
//Synthetic comment -- @@ -315,6 +317,49 @@
}

/**
     * This is a simplified convenience method that calls
     * {@link #openDirectUrl(String, Header[], ITaskMonitor)}
     * without passing any specific HTTP headers  and returns the resulting input stream
     * and the HTTP status code.
     * See the original method's description for details on its behavior.
     * <p/>
     * {@link #openDirectUrl(String, Header[], ITaskMonitor)} can accept customized
     * HTTP headers to send with the requests and also returns the full HTTP
     * response -- status line with code and protocol and all headers.
     * <p/>
     * The resulting input stream may not support mark/reset.
     *
     * @param urlString the URL string to be opened.
     * @param monitor {@link ITaskMonitor} which is related to this URL
     *                 fetching.
     * @return Returns a pair with a {@link InputStream} and an HTTP status code.
     *              The pair is never null.
     *              The input stream can be null in case of error, although in general the
     *              method will probably throw an exception instead.
     *              The caller should look at the response code's status and only accept the
     *              input stream if it's the desired code (e.g. 200 or 206).
     * @throws IOException Exception thrown when there are problems retrieving
     *                 the URL or its content.
     * @throws CanceledByUserException Exception thrown if the user cancels the
     *              authentication dialog.
     * @see #openDirectUrl(String, Header[], ITaskMonitor)
     */
    public Pair<InputStream, Integer> openDirectUrl(
            @NonNull  String urlString,
            @NonNull  ITaskMonitor monitor)
                throws IOException, CanceledByUserException {
        if (DEBUG) {
            System.out.println(String.format("%s : Direct download", urlString)); //$NON-NLS-1$
        }
        Pair<InputStream, HttpResponse> result = UrlOpener.openUrl(
                urlString,
                false /*needsMarkResetSupport*/,
                monitor,
                null /*headers*/);
        return Pair.of(result.getFirst(), result.getSecond().getStatusLine().getStatusCode());
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

        Exception fallbackOnJavaUrlConnect = null;
Pair<InputStream, HttpResponse> result = null;

try {
//Synthetic comment -- @@ -186,6 +187,11 @@
// it could use a better message.
throw new IOException("Unknown Host " + e.getMessage(), e);

        } catch (ClientProtocolException e) {
            // We get this when HttpClient fails to accept the current protocol,
            // e.g. when processing file:// URLs.
            fallbackOnJavaUrlConnect = e;

} catch (IOException e) {
throw e;

//Synthetic comment -- @@ -194,19 +200,24 @@
throw e;

} catch (Exception e) {
if (DEBUG) {
System.out.printf("[HttpClient Error] %s : %s\n", url, e.toString());
}

            fallbackOnJavaUrlConnect = e;
        }

        if (fallbackOnJavaUrlConnect != null) {
            // If the protocol is not supported by HttpClient (e.g. file:///),
            // revert to the standard java.net.Url.open.

try {
result = openWithUrl(url, headers);
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                if (DEBUG && !fallbackOnJavaUrlConnect.equals(e)) {
                    System.out.printf("[Url Error] %s : %s\n", url, e.toString());
}
}
}







