/*SDK Manager fix.

This change the XML fetcher to cope with:
- input streams that do not support mark/reset when the
  caller expects one as such.
- the XML parser/validator closing the input stream
  when the caller still needs it open.

Change-Id:I5af24b7b8545f9c5f60a13d5dc06ebfc4bb9b0dd*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index 0880645..dab848a 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;

//Synthetic comment -- @@ -127,10 +129,11 @@
defaultNames[i] = SdkAddonsListConstants.getDefaultName(version);
}

        InputStream xml = fetchUrl(url, cache, monitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
xml = null;
}
}
//Synthetic comment -- @@ -153,10 +156,11 @@
if (newUrl.equals(url)) {
continue;
}
                xml = fetchUrl(newUrl, cache, subMonitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
xml = null;
} else {
url = newUrl;
//Synthetic comment -- @@ -190,6 +194,7 @@
} else if (version > SdkAddonsListConstants.NS_LATEST_VERSION) {
// The schema used is more recent than what is supported by this tool.
// We don't have an upgrade-path support yet, so simply ignore the document.
return null;
}
}
//Synthetic comment -- @@ -223,6 +228,7 @@

// Stop here if we failed to validate the XML. We don't want to load it.
if (validatedDoc == null) {
return null;
}

//Synthetic comment -- @@ -239,13 +245,13 @@
// done
monitor.incProgress(1);

return result;
}

/**
* Fetches the document at the given URL and returns it as a stream. Returns
     * null if anything wrong happens. References: <br/>
     * URL Connection:
*
* @param urlString The URL to load, as a string.
* @param monitor {@link ITaskMonitor} related to this URL.
//Synthetic comment -- @@ -253,12 +259,17 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchUrl(String urlString,
DownloadCache cache,
ITaskMonitor monitor,
Exception[] outException) {
try {
            return cache.openCachedUrl(urlString, monitor);
} catch (Exception e) {
if (outException != null) {
outException[0] = e;
//Synthetic comment -- @@ -269,6 +280,21 @@
}

/**
* Manually parses the root element of the XML to extract the schema version
* at the end of the xmlns:sdk="http://schemas.android.com/sdk/android/addons-list/$N"
* declaration.
//Synthetic comment -- @@ -285,6 +311,7 @@
// Get an XML document
Document doc = null;
try {
xml.reset();

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -323,6 +350,7 @@
// Failed to create XML document builder
// Failed to parse XML document
// Failed to read XML document
}

if (doc == null) {
//Synthetic comment -- @@ -403,6 +431,7 @@
validatorFound[0] = Boolean.TRUE;

// Reset the stream if it supports that operation.
xml.reset();

// Validation throws a bunch of possible Exceptions on failure.
//Synthetic comment -- @@ -462,6 +491,7 @@
factory.setNamespaceAware(true);

DocumentBuilder builder = factory.newDocumentBuilder();
xml.reset();
Document doc = builder.parse(new InputSource(xml));









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 8d7b47f..2f154a6 100755

//Synthetic comment -- @@ -254,7 +254,7 @@
* Instead the HttpClient library returns a progressive download stream.
* <p/>
* For details on realm authentication and user/password handling,
     * check the underlying {@link UrlOpener#openUrl(String, ITaskMonitor, Header[])}
* documentation.
*
* @param urlString the URL string to be opened.
//Synthetic comment -- @@ -271,8 +271,11 @@
if (DEBUG) {
System.out.println(String.format("%s : Direct download", urlString)); //$NON-NLS-1$
}
        Pair<InputStream, HttpResponse> result =
            UrlOpener.openUrl(urlString, monitor, null /*headers*/);
return result.getFirst();
}

//Synthetic comment -- @@ -286,7 +289,7 @@
* method.
* <p/>
* For details on realm authentication and user/password handling,
     * check the underlying {@link UrlOpener#openUrl(String, ITaskMonitor, Header[])}
* documentation.
*
* @param urlString the URL string to be opened.
//Synthetic comment -- @@ -301,9 +304,14 @@
*/
public InputStream openCachedUrl(String urlString, ITaskMonitor monitor)
throws IOException, CanceledByUserException {
        // Don't cache in direct mode. Don't try to cache non-http URLs.
        if (mStrategy == Strategy.DIRECT || !urlString.startsWith("http")) {        //$NON-NLS-1$
            return openDirectUrl(urlString, monitor);
}

File cached = new File(mCacheRoot, getCacheFilename(urlString));
//Synthetic comment -- @@ -561,7 +569,8 @@
byte[] result = new byte[inc];

try {
            Pair<InputStream, HttpResponse> r = UrlOpener.openUrl(urlString, monitor, headers);

is = r.getFirst();
HttpResponse response = r.getSecond();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkStats.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkStats.java
//Synthetic comment -- index 4958505..60bb89d 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.repository.SdkStatsConstants;
import com.android.sdklib.util.SparseArray;

//Synthetic comment -- @@ -159,7 +161,7 @@
Document validatedDoc = null;
String validatedUri = null;

        InputStream xml = fetchUrl(url, cache, monitor.createSubMonitor(1), exception);

if (xml != null) {
monitor.setDescription("Validate XML");
//Synthetic comment -- @@ -181,6 +183,7 @@
} else if (version > SdkStatsConstants.NS_LATEST_VERSION) {
// The schema used is more recent than what is supported by this tool.
// We don't have an upgrade-path support yet, so simply ignore the document.
return;
}
}
//Synthetic comment -- @@ -214,6 +217,7 @@

// Stop here if we failed to validate the XML. We don't want to load it.
if (validatedDoc == null) {
return;
}

//Synthetic comment -- @@ -227,12 +231,12 @@

// done
monitor.incProgress(1);
}

/**
* Fetches the document at the given URL and returns it as a stream. Returns
     * null if anything wrong happens. References: <br/>
     * URL Connection:
*
* @param urlString The URL to load, as a string.
* @param monitor {@link ITaskMonitor} related to this URL.
//Synthetic comment -- @@ -240,12 +244,17 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchUrl(String urlString,
DownloadCache cache,
ITaskMonitor monitor,
Exception[] outException) {
try {
            return cache.openCachedUrl(urlString, monitor);
} catch (Exception e) {
if (outException != null) {
outException[0] = e;
//Synthetic comment -- @@ -256,6 +265,21 @@
}

/**
* Manually parses the root element of the XML to extract the schema version
* at the end of the xmlns:sdk="http://schemas.android.com/sdk/android/addons-list/$N"
* declaration.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index d31a286..092c60d 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import org.apache.http.protocol.HttpContext;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
//Synthetic comment -- @@ -61,7 +62,7 @@

/**
* This class holds methods for adding URLs management.
 * @see #openUrl(String, ITaskMonitor, Header[])
*/
public class UrlOpener {

//Synthetic comment -- @@ -121,13 +122,22 @@
* available in the memory cache.
*
* @param url the URL string to be opened.
* @param monitor {@link ITaskMonitor} which is related to this URL
     *            fetching.
* @param headers An optional array of HTTP headers to use in the GET request.
     * @return Returns an {@link InputStream} holding the URL content and
     *      the HttpResponse (locale, headers and an status line).
     *      This never returns null; an exception is thrown instead in case of
     *      error or if the user canceled an authentication dialog.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
//Synthetic comment -- @@ -135,6 +145,7 @@
*/
static @NonNull Pair<InputStream, HttpResponse> openUrl(
@NonNull String url,
@NonNull ITaskMonitor monitor,
@Nullable Header[] headers)
throws IOException, CanceledByUserException {
//Synthetic comment -- @@ -154,15 +165,71 @@
}
}

if (result == null) {
HttpResponse outResponse = new BasicHttpResponse(
new ProtocolVersion("HTTP", 1, 0),
                    404, "");  //$NON-NLS-1$;
result = Pair.of(null, outResponse);
}
return result;
}

private static Pair<InputStream, HttpResponse> openWithUrl(
String url,
Header[] inHeaders) throws IOException {
//Synthetic comment -- @@ -257,9 +324,10 @@

if (DEBUG) {
try {
ProxySelector sel = routePlanner.getProxySelector();
                if (sel != null) {
                    List<Proxy> list = sel.select(new URI(url));
System.out.printf(
"SdkLib.UrlOpener:\n  Connect to: %s\n  Proxy List: %s\n", //$NON-NLS-1$
url,
//Synthetic comment -- @@ -267,7 +335,7 @@
}
} catch (Exception e) {
System.out.printf(
                        "SdkLib.UrlOpener: Failed to get proxy info for %s: %s\n", //$NON-NLS-1$
url, e.toString());
}
}
//Synthetic comment -- @@ -280,7 +348,7 @@
int statusCode = response.getStatusLine().getStatusCode();

if (DEBUG) {
                System.out.printf("  Status: %d", statusCode); //$NON-NLS-1$
}

// check whether any authentication is required








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkRepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkRepoSource.java
//Synthetic comment -- index 9fe5574..3ad809a 100755

//Synthetic comment -- @@ -181,6 +181,7 @@
}

// Reset the stream if it supports that operation.
xml.reset();

// Get an XML document








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index 8187480..a0d515a 100755

//Synthetic comment -- @@ -34,6 +34,8 @@
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -358,10 +360,11 @@
String[] defaultNames = getDefaultXmlFileUrls();
String firstDefaultName = defaultNames.length > 0 ? defaultNames[0] : "";

        InputStream xml = fetchUrl(url, cache, monitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
xml = null;
}
}
//Synthetic comment -- @@ -385,10 +388,11 @@
if (newUrl.equals(url)) {
continue;
}
                xml = fetchUrl(newUrl, cache, subMonitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
xml = null;
} else {
url = newUrl;
//Synthetic comment -- @@ -414,7 +418,7 @@
}
url += firstDefaultName;

            xml = fetchUrl(url, cache, monitor.createSubMonitor(1), exception);
usingAlternateUrl = true;
} else {
monitor.incProgress(1);
//Synthetic comment -- @@ -487,7 +491,8 @@
}
url += firstDefaultName;

                        xml = fetchUrl(url, cache, subMonitor.createSubMonitor(1),
null /* outException */);
subMonitor.incProgress(1);
// Loop to try the alternative document
//Synthetic comment -- @@ -589,6 +594,7 @@

// done
monitor.incProgress(1);
}

private void setDefaultDescription() {
//Synthetic comment -- @@ -610,8 +616,6 @@
/**
* Fetches the document at the given URL and returns it as a string. Returns
* null if anything wrong happens and write errors to the monitor.
     * References: <br/>
     * URL Connection:
*
* @param urlString The URL to load, as a string.
* @param monitor {@link ITaskMonitor} related to this URL.
//Synthetic comment -- @@ -619,12 +623,17 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchUrl(String urlString,
DownloadCache cache,
ITaskMonitor monitor,
Exception[] outException) {
try {
            return cache.openCachedUrl(urlString, monitor);
} catch (Exception e) {
if (outException != null) {
outException[0] = e;
//Synthetic comment -- @@ -635,6 +644,21 @@
}

/**
* Validates this XML against one of the requested SDK Repository schemas.
* If the XML was correctly validated, returns the schema that worked.
* If it doesn't validate, returns null and stores the error in outError[0].
//Synthetic comment -- @@ -662,6 +686,7 @@
validatorFound[0] = Boolean.TRUE;

// Reset the stream if it supports that operation.
xml.reset();

// Validation throws a bunch of possible Exceptions on failure.
//Synthetic comment -- @@ -702,6 +727,7 @@
// Get an XML document
Document doc = null;
try {
xml.reset();

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -946,6 +972,7 @@
factory.setNamespaceAware(true);

DocumentBuilder builder = factory.newDocumentBuilder();
xml.reset();
Document doc = builder.parse(new InputSource(xml));









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/NonClosingInputStream.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/NonClosingInputStream.java
new file mode 100755
//Synthetic comment -- index 0000000..975b3cf

//Synthetic comment -- @@ -0,0 +1,104 @@







