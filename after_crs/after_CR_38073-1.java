/*SDK Manager fix.

This change the XML fetcher to cope with:
- input streams that do not support mark/reset when the
  caller expects one as such.
- the XML parser/validator closing the input stream
  when the caller still needs it open.

(cherry picked from commit f453507f61785ebb5594b5cdb867286f9e848fdb)

Change-Id:I4cc94aaad1e5a3ffedd9d615b2bde8f7ca8ce6ab*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index 0880645..55021a3 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.io.NonClosingInputStream;
import com.android.sdklib.io.NonClosingInputStream.CloseBehavior;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;

//Synthetic comment -- @@ -127,10 +129,11 @@
defaultNames[i] = SdkAddonsListConstants.getDefaultName(version);
}

        InputStream xml = fetchXmlUrl(url, cache, monitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
                closeStream(xml);
xml = null;
}
}
//Synthetic comment -- @@ -153,10 +156,11 @@
if (newUrl.equals(url)) {
continue;
}
                xml = fetchXmlUrl(newUrl, cache, subMonitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
                        closeStream(xml);
xml = null;
} else {
url = newUrl;
//Synthetic comment -- @@ -190,6 +194,7 @@
} else if (version > SdkAddonsListConstants.NS_LATEST_VERSION) {
// The schema used is more recent than what is supported by this tool.
// We don't have an upgrade-path support yet, so simply ignore the document.
                closeStream(xml);
return null;
}
}
//Synthetic comment -- @@ -223,6 +228,7 @@

// Stop here if we failed to validate the XML. We don't want to load it.
if (validatedDoc == null) {
            closeStream(xml);
return null;
}

//Synthetic comment -- @@ -239,13 +245,13 @@
// done
monitor.incProgress(1);

        closeStream(xml);
return result;
}

/**
* Fetches the document at the given URL and returns it as a stream. Returns
     * null if anything wrong happens.
*
* @param urlString The URL to load, as a string.
* @param monitor {@link ITaskMonitor} related to this URL.
//Synthetic comment -- @@ -253,12 +259,17 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchXmlUrl(String urlString,
DownloadCache cache,
ITaskMonitor monitor,
Exception[] outException) {
try {
            InputStream xml = cache.openCachedUrl(urlString, monitor);
            if (xml != null) {
                xml.mark(500000);
                xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
            }
            return xml;
} catch (Exception e) {
if (outException != null) {
outException[0] = e;
//Synthetic comment -- @@ -269,6 +280,21 @@
}

/**
     * Closes the stream, ignore any exception from InputStream.close().
     * If the stream is a NonClosingInputStream, sets it to CloseBehavior.CLOSE first.
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            if (is instanceof NonClosingInputStream) {
                ((NonClosingInputStream) is).setCloseBehavior(CloseBehavior.CLOSE);
            }
            try {
                is.close();
            } catch (IOException ignore) {}
        }
    }

    /**
* Manually parses the root element of the XML to extract the schema version
* at the end of the xmlns:sdk="http://schemas.android.com/sdk/android/addons-list/$N"
* declaration.
//Synthetic comment -- @@ -285,6 +311,7 @@
// Get an XML document
Document doc = null;
try {
            assert xml.markSupported();
xml.reset();

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -323,6 +350,7 @@
// Failed to create XML document builder
// Failed to parse XML document
// Failed to read XML document
            //--For debug--System.err.println("getXmlSchemaVersion exception: " + e.toString());
}

if (doc == null) {
//Synthetic comment -- @@ -403,6 +431,7 @@
validatorFound[0] = Boolean.TRUE;

// Reset the stream if it supports that operation.
            assert xml.markSupported();
xml.reset();

// Validation throws a bunch of possible Exceptions on failure.
//Synthetic comment -- @@ -462,6 +491,7 @@
factory.setNamespaceAware(true);

DocumentBuilder builder = factory.newDocumentBuilder();
            assert xml.markSupported();
xml.reset();
Document doc = builder.parse(new InputSource(xml));









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 8d7b47f..2f154a6 100755

//Synthetic comment -- @@ -254,7 +254,7 @@
* Instead the HttpClient library returns a progressive download stream.
* <p/>
* For details on realm authentication and user/password handling,
     * check the underlying {@link UrlOpener#openUrl(String, boolean, ITaskMonitor, Header[])}
* documentation.
*
* @param urlString the URL string to be opened.
//Synthetic comment -- @@ -271,8 +271,11 @@
if (DEBUG) {
System.out.println(String.format("%s : Direct download", urlString)); //$NON-NLS-1$
}
        Pair<InputStream, HttpResponse> result = UrlOpener.openUrl(
                urlString,
                false /*needsMarkResetSupport*/,
                monitor,
                null /*headers*/);
return result.getFirst();
}

//Synthetic comment -- @@ -286,7 +289,7 @@
* method.
* <p/>
* For details on realm authentication and user/password handling,
     * check the underlying {@link UrlOpener#openUrl(String, boolean, ITaskMonitor, Header[])}
* documentation.
*
* @param urlString the URL string to be opened.
//Synthetic comment -- @@ -301,9 +304,14 @@
*/
public InputStream openCachedUrl(String urlString, ITaskMonitor monitor)
throws IOException, CanceledByUserException {
        // Don't cache in direct mode.
        if (mStrategy == Strategy.DIRECT) {
            Pair<InputStream, HttpResponse> result = UrlOpener.openUrl(
                    urlString,
                    true /*needsMarkResetSupport*/,
                    monitor,
                    null /*headers*/);
            return result.getFirst();
}

File cached = new File(mCacheRoot, getCacheFilename(urlString));
//Synthetic comment -- @@ -561,7 +569,8 @@
byte[] result = new byte[inc];

try {
            Pair<InputStream, HttpResponse> r =
                UrlOpener.openUrl(urlString, true /*needsMarkResetSupport*/, monitor, headers);

is = r.getFirst();
HttpResponse response = r.getSecond();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkStats.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkStats.java
//Synthetic comment -- index 4958505..60bb89d 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.io.NonClosingInputStream;
import com.android.sdklib.io.NonClosingInputStream.CloseBehavior;
import com.android.sdklib.repository.SdkStatsConstants;
import com.android.sdklib.util.SparseArray;

//Synthetic comment -- @@ -159,7 +161,7 @@
Document validatedDoc = null;
String validatedUri = null;

        InputStream xml = fetchXmlUrl(url, cache, monitor.createSubMonitor(1), exception);

if (xml != null) {
monitor.setDescription("Validate XML");
//Synthetic comment -- @@ -181,6 +183,7 @@
} else if (version > SdkStatsConstants.NS_LATEST_VERSION) {
// The schema used is more recent than what is supported by this tool.
// We don't have an upgrade-path support yet, so simply ignore the document.
                closeStream(xml);
return;
}
}
//Synthetic comment -- @@ -214,6 +217,7 @@

// Stop here if we failed to validate the XML. We don't want to load it.
if (validatedDoc == null) {
            closeStream(xml);
return;
}

//Synthetic comment -- @@ -227,12 +231,12 @@

// done
monitor.incProgress(1);
        closeStream(xml);
}

/**
* Fetches the document at the given URL and returns it as a stream. Returns
     * null if anything wrong happens.
*
* @param urlString The URL to load, as a string.
* @param monitor {@link ITaskMonitor} related to this URL.
//Synthetic comment -- @@ -240,12 +244,17 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchXmlUrl(String urlString,
DownloadCache cache,
ITaskMonitor monitor,
Exception[] outException) {
try {
            InputStream xml = cache.openCachedUrl(urlString, monitor);
            if (xml != null) {
                xml.mark(500000);
                xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
            }
            return xml;
} catch (Exception e) {
if (outException != null) {
outException[0] = e;
//Synthetic comment -- @@ -256,6 +265,21 @@
}

/**
     * Closes the stream, ignore any exception from InputStream.close().
     * If the stream is a NonClosingInputStream, sets it to CloseBehavior.CLOSE first.
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            if (is instanceof NonClosingInputStream) {
                ((NonClosingInputStream) is).setCloseBehavior(CloseBehavior.CLOSE);
            }
            try {
                is.close();
            } catch (IOException ignore) {}
        }
    }

    /**
* Manually parses the root element of the XML to extract the schema version
* at the end of the xmlns:sdk="http://schemas.android.com/sdk/android/addons-list/$N"
* declaration.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index d31a286..5da92e9 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import org.apache.http.protocol.HttpContext;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
//Synthetic comment -- @@ -61,7 +62,7 @@

/**
* This class holds methods for adding URLs management.
 * @see #openUrl(String, boolean, ITaskMonitor, Header[])
*/
public class UrlOpener {

//Synthetic comment -- @@ -121,13 +122,21 @@
* available in the memory cache.
*
* @param url the URL string to be opened.
     * @param needsMarkResetSupport Indicates the caller <em>must</em> have an input stream that
     *      supports the mark/reset operations (as indicated by {@link InputStream#markSupported()}.
     *      Implementation detail: If the original stream does not, it will be fetched and wrapped
     *      into a {@link ByteArrayInputStream}. This can only work sanely if the resource is a
     *      small file that can fit in memory. It also means the caller has no chance of showing
     *      a meaningful download progress. If unsure, callers should set this to false.
     * @param monitor {@link ITaskMonitor} to output status.
* @param headers An optional array of HTTP headers to use in the GET request.
     * @return Returns a {@link Pair} with {@code first} holding an {@link InputStream}
     *      and {@code second} holding an {@link HttpResponse}.
     *      The input stream can be null. The response is never null and contains
     *      at least a code; for http requests that provide them the response
     *      also contains locale, headers and an status line.
     *      The returned pair is never null.
     *      The caller must only accept the stream if the response code is 200 or similar.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
//Synthetic comment -- @@ -135,6 +144,7 @@
*/
static @NonNull Pair<InputStream, HttpResponse> openUrl(
@NonNull String url,
            boolean needsMarkResetSupport,
@NonNull ITaskMonitor monitor,
@Nullable Header[] headers)
throws IOException, CanceledByUserException {
//Synthetic comment -- @@ -154,15 +164,60 @@
}
}

        // If the caller requires an InputStream that supports mark/reset, let's
        // make sure we have such a stream.
        if (result != null && needsMarkResetSupport) {
            InputStream is = result.getFirst();
            if (is != null) {
                if (!is.markSupported()) {
                    try {
                        // Consume the whole input stream and offer a byte array stream instead.
                        // This can only work sanely if the resource is a small file that can
                        // fit in memory. It also means the caller has no chance of showing
                        // a meaningful download progress.
                        InputStream is2 = toByteArrayInputStream(is);
                        if (is2 != null) {
                            result = Pair.of(is2, result.getSecond());
                            try {
                                is.close();
                            } catch (Exception ignore) {}
                        }
                    } catch (Exception e3) {
                        // Ignore. If this can't work, caller will fail later.
                    }
                }
            }
        }

if (result == null) {
HttpResponse outResponse = new BasicHttpResponse(
                    new ProtocolVersion("HTTP", 1, 0),  //$NON-NLS-1$
                    424, "");                           //$NON-NLS-1$;  // 424=Method Failure
result = Pair.of(null, outResponse);
}

return result;
}

    // ByteArrayInputStream is the duct tape of input streams.
    private static InputStream toByteArrayInputStream(InputStream is) throws IOException {
        int inc = 4096;
        int curr = 0;
        byte[] result = new byte[inc];

        int n;
        while ((n = is.read(result, curr, result.length - curr)) != -1) {
            curr += n;
            if (curr == result.length) {
                byte[] temp = new byte[curr + inc];
                System.arraycopy(result, 0, temp, 0, curr);
                result = temp;
            }
        }

        return new ByteArrayInputStream(result, 0, curr);
    }

private static Pair<InputStream, HttpResponse> openWithUrl(
String url,
Header[] inHeaders) throws IOException {
//Synthetic comment -- @@ -257,9 +312,10 @@

if (DEBUG) {
try {
                URI uri = new URI(url);
ProxySelector sel = routePlanner.getProxySelector();
                if (sel != null && uri.getScheme().startsWith("httP")) {               //$NON-NLS-1$
                    List<Proxy> list = sel.select(uri);
System.out.printf(
"SdkLib.UrlOpener:\n  Connect to: %s\n  Proxy List: %s\n", //$NON-NLS-1$
url,
//Synthetic comment -- @@ -267,7 +323,7 @@
}
} catch (Exception e) {
System.out.printf(
                        "SdkLib.UrlOpener: Failed to get proxy info for %s: %s\n",     //$NON-NLS-1$
url, e.toString());
}
}
//Synthetic comment -- @@ -280,7 +336,7 @@
int statusCode = response.getStatusLine().getStatusCode();

if (DEBUG) {
                System.out.printf("  Status: %d\n", statusCode);                       //$NON-NLS-1$
}

// check whether any authentication is required








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkRepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkRepoSource.java
//Synthetic comment -- index 9fe5574..3ad809a 100755

//Synthetic comment -- @@ -181,6 +181,7 @@
}

// Reset the stream if it supports that operation.
        assert xml.markSupported();
xml.reset();

// Get an XML document








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index 8187480..a0d515a 100755

//Synthetic comment -- @@ -34,6 +34,8 @@
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.io.NonClosingInputStream;
import com.android.sdklib.io.NonClosingInputStream.CloseBehavior;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -358,10 +360,11 @@
String[] defaultNames = getDefaultXmlFileUrls();
String firstDefaultName = defaultNames.length > 0 ? defaultNames[0] : "";

        InputStream xml = fetchXmlUrl(url, cache, monitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
                closeStream(xml);
xml = null;
}
}
//Synthetic comment -- @@ -385,10 +388,11 @@
if (newUrl.equals(url)) {
continue;
}
                xml = fetchXmlUrl(newUrl, cache, subMonitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
                        closeStream(xml);
xml = null;
} else {
url = newUrl;
//Synthetic comment -- @@ -414,7 +418,7 @@
}
url += firstDefaultName;

            xml = fetchXmlUrl(url, cache, monitor.createSubMonitor(1), exception);
usingAlternateUrl = true;
} else {
monitor.incProgress(1);
//Synthetic comment -- @@ -487,7 +491,8 @@
}
url += firstDefaultName;

                        closeStream(xml);
                        xml = fetchXmlUrl(url, cache, subMonitor.createSubMonitor(1),
null /* outException */);
subMonitor.incProgress(1);
// Loop to try the alternative document
//Synthetic comment -- @@ -589,6 +594,7 @@

// done
monitor.incProgress(1);
        closeStream(xml);
}

private void setDefaultDescription() {
//Synthetic comment -- @@ -610,8 +616,6 @@
/**
* Fetches the document at the given URL and returns it as a string. Returns
* null if anything wrong happens and write errors to the monitor.
*
* @param urlString The URL to load, as a string.
* @param monitor {@link ITaskMonitor} related to this URL.
//Synthetic comment -- @@ -619,12 +623,17 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchXmlUrl(String urlString,
DownloadCache cache,
ITaskMonitor monitor,
Exception[] outException) {
try {
            InputStream xml = cache.openCachedUrl(urlString, monitor);
            if (xml != null) {
                xml.mark(500000);
                xml = new NonClosingInputStream(xml).setCloseBehavior(CloseBehavior.RESET);
            }
            return xml;
} catch (Exception e) {
if (outException != null) {
outException[0] = e;
//Synthetic comment -- @@ -635,6 +644,21 @@
}

/**
     * Closes the stream, ignore any exception from InputStream.close().
     * If the stream is a NonClosingInputStream, sets it to CloseBehavior.CLOSE first.
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            if (is instanceof NonClosingInputStream) {
                ((NonClosingInputStream) is).setCloseBehavior(CloseBehavior.CLOSE);
            }
            try {
                is.close();
            } catch (IOException ignore) {}
        }
    }

    /**
* Validates this XML against one of the requested SDK Repository schemas.
* If the XML was correctly validated, returns the schema that worked.
* If it doesn't validate, returns null and stores the error in outError[0].
//Synthetic comment -- @@ -662,6 +686,7 @@
validatorFound[0] = Boolean.TRUE;

// Reset the stream if it supports that operation.
            assert xml.markSupported();
xml.reset();

// Validation throws a bunch of possible Exceptions on failure.
//Synthetic comment -- @@ -702,6 +727,7 @@
// Get an XML document
Document doc = null;
try {
            assert xml.markSupported();
xml.reset();

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -946,6 +972,7 @@
factory.setNamespaceAware(true);

DocumentBuilder builder = factory.newDocumentBuilder();
            assert xml.markSupported();
xml.reset();
Document doc = builder.parse(new InputSource(xml));









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/NonClosingInputStream.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/NonClosingInputStream.java
new file mode 100755
//Synthetic comment -- index 0000000..e21e47a

//Synthetic comment -- @@ -0,0 +1,104 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.io;

import com.android.annotations.NonNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Wraps an {@link InputStream} to change its closing behavior:
 * this makes it possible to ignore close operations or have them perform a
 * {@link InputStream#reset()} instead (if supported by the underlying stream)
 * or plain ignored.
 */
public class NonClosingInputStream extends FilterInputStream {

    private final InputStream mInputStream;
    private CloseBehavior mCloseBehavior = CloseBehavior.CLOSE;

    public enum CloseBehavior {
        /**
         * The behavior of {@link NonClosingInputStream#close()} is to close the
         * underlying input stream. This is the default.
         */
        CLOSE,
        /**
         * The behavior of {@link NonClosingInputStream#close()} is to ignore the
         * close request and do nothing.
         */
        IGNORE,
        /**
         * The behavior of {@link NonClosingInputStream#close()} is to call
         * {@link InputStream#reset()} on the underlying stream. This will
         * only succeed if the underlying stream supports it, e.g. it must
         * have {@link InputStream#markSupported()} return true <em>and</em>
         * the caller should have called {@link InputStream#mark(int)} at some
         * point before.
         */
        RESET
    }

    /**
     * Wraps an existing stream into this filtering stream.
     * @param in A non-null input stream.
     */
    public NonClosingInputStream(@NonNull InputStream in) {
        super(in);
        mInputStream = in;
    }

    /**
     * Returns the current {@link CloseBehavior}.
     * @return the current {@link CloseBehavior}. Never null.
     */
    public @NonNull CloseBehavior getCloseBehavior() {
        return mCloseBehavior;
    }

    /**
     * Changes the current {@link CloseBehavior}.
     *
     * @param closeBehavior A new non-null {@link CloseBehavior}.
     * @return Self for chaining.
     */
    public NonClosingInputStream setCloseBehavior(@NonNull CloseBehavior closeBehavior) {
        mCloseBehavior = closeBehavior;
        return this;
    }

    /**
     * Performs the requested {@code close()} operation, depending on the current
     * {@link CloseBehavior}.
     */
    @Override
    public void close() throws IOException {
        switch (mCloseBehavior) {
        case IGNORE:
            break;
        case RESET:
            mInputStream.reset();
            break;
        case CLOSE:
            mInputStream.close();
            break;
        }
    }
}







