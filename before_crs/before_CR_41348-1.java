/*Performance improvements: DiskLruCache, HttpResponseCache.

Add StrictLineReader for efficient reading of lines
consistent with Streams.readAsciiLine(). Use this to improve
DiskLruCache.readJournal() and initialization of
HttpResponseCache$Entry from InputStream.

(cherry-pick of e03b551079aae1204e505f1dc24f2b986ef82ec0.)

Bug: 6739304
Change-Id:If3083031f1368a9bbbd405c91553d7a205fd4e39*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/DiskLruCache.java b/luni/src/main/java/libcore/io/DiskLruCache.java
//Synthetic comment -- index b7d246d..8338983 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package libcore.io;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
//Synthetic comment -- @@ -227,13 +226,14 @@
}

private void readJournal() throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(journalFile));
try {
            String magic = Streams.readAsciiLine(in);
            String version = Streams.readAsciiLine(in);
            String appVersionString = Streams.readAsciiLine(in);
            String valueCountString = Streams.readAsciiLine(in);
            String blank = Streams.readAsciiLine(in);
if (!MAGIC.equals(magic)
|| !VERSION_1.equals(version)
|| !Integer.toString(appVersion).equals(appVersionString)
//Synthetic comment -- @@ -245,13 +245,13 @@

while (true) {
try {
                    readJournalLine(Streams.readAsciiLine(in));
} catch (EOFException endOfJournal) {
break;
}
}
} finally {
            IoUtils.closeQuietly(in);
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/StrictLineReader.java b/luni/src/main/java/libcore/io/StrictLineReader.java
new file mode 100644
//Synthetic comment -- index 0000000..5f8d452

//Synthetic comment -- @@ -0,0 +1,241 @@








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpResponseCache.java b/luni/src/main/java/libcore/net/http/HttpResponseCache.java
//Synthetic comment -- index 2130fd1..1a9dfd1 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package libcore.net.http;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
//Synthetic comment -- @@ -53,7 +52,7 @@
import libcore.io.Base64;
import libcore.io.DiskLruCache;
import libcore.io.IoUtils;
import libcore.io.Streams;

/**
* Cache responses in a directory on the file system. Most clients should use
//Synthetic comment -- @@ -100,7 +99,7 @@
if (snapshot == null) {
return null;
}
            entry = new Entry(new BufferedInputStream(snapshot.getInputStream(ENTRY_METADATA)));
} catch (IOException e) {
// Give up because the cache cannot be read.
return null;
//Synthetic comment -- @@ -369,29 +368,30 @@
*/
public Entry(InputStream in) throws IOException {
try {
                uri = Streams.readAsciiLine(in);
                requestMethod = Streams.readAsciiLine(in);
varyHeaders = new RawHeaders();
                int varyRequestHeaderLineCount = readInt(in);
for (int i = 0; i < varyRequestHeaderLineCount; i++) {
                    varyHeaders.addLine(Streams.readAsciiLine(in));
}

responseHeaders = new RawHeaders();
                responseHeaders.setStatusLine(Streams.readAsciiLine(in));
                int responseHeaderLineCount = readInt(in);
for (int i = 0; i < responseHeaderLineCount; i++) {
                    responseHeaders.addLine(Streams.readAsciiLine(in));
}

if (isHttps()) {
                    String blank = Streams.readAsciiLine(in);
if (!blank.isEmpty()) {
throw new IOException("expected \"\" but was \"" + blank + "\"");
}
                    cipherSuite = Streams.readAsciiLine(in);
                    peerCertificates = readCertArray(in);
                    localCertificates = readCertArray(in);
} else {
cipherSuite = null;
peerCertificates = null;
//Synthetic comment -- @@ -457,17 +457,8 @@
return uri.startsWith("https://");
}

        private int readInt(InputStream in) throws IOException {
            String intString = Streams.readAsciiLine(in);
            try {
                return Integer.parseInt(intString);
            } catch (NumberFormatException e) {
                throw new IOException("expected an int but was \"" + intString + "\"");
            }
        }

        private Certificate[] readCertArray(InputStream in) throws IOException {
            int length = readInt(in);
if (length == -1) {
return null;
}
//Synthetic comment -- @@ -475,7 +466,7 @@
CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
Certificate[] result = new Certificate[length];
for (int i = 0; i < result.length; i++) {
                    String line = Streams.readAsciiLine(in);
byte[] bytes = Base64.decode(line.getBytes(Charsets.US_ASCII));
result[i] = certificateFactory.generateCertificate(
new ByteArrayInputStream(bytes));








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/io/StrictLineReaderTest.java b/luni/src/test/java/libcore/io/StrictLineReaderTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d5d3381

//Synthetic comment -- @@ -0,0 +1,84 @@







