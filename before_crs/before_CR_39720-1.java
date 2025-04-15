/*Fix HttpURLConnection CloseGuard warning due to lack of close on GZIPInputStream

java.lang.Throwable: Explicit termination method 'end' not called
       at dalvik.system.CloseGuard.open(CloseGuard.java:184)
       at java.util.zip.Inflater.<init>(Inflater.java:82)
       at java.util.zip.GZIPInputStream.<init>(GZIPInputStream.java:96)
       at java.util.zip.GZIPInputStream.<init>(GZIPInputStream.java:81)
       at libcore.net.http.HttpEngine.initContentStream(HttpEngine.java:523)
       at libcore.net.http.HttpEngine.readResponse(HttpEngine.java:831)
       at libcore.net.http.HttpURLConnectionImpl.getResponse(HttpURLConnectionImpl.java:274)
       at libcore.net.http.HttpURLConnectionImpl.getResponseCode(HttpURLConnectionImpl.java:486)
       at ...

Bug: 6602529
Change-Id:I9b49cbca561f8780d08844e566820087fdffc4d7*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/CloseGuard.java b/dalvik/src/main/java/dalvik/system/CloseGuard.java
//Synthetic comment -- index 136be2f..df36867 100644

//Synthetic comment -- @@ -197,7 +197,7 @@
/**
* If CloseGuard is enabled, logs a warning if the caller did not
* properly cleanup by calling an explicit close method
     * before finalization. If CloseGuard is disable, no action is
* performed.
*/
public void warnIfOpen() {
//Synthetic comment -- @@ -223,7 +223,7 @@
* Default Reporter which reports CloseGuard violations to the log.
*/
private static final class DefaultReporter implements Reporter {
        public void report (String message, Throwable allocationSite) {
System.logW(message, allocationSite);
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpURLConnectionImpl.java b/luni/src/main/java/libcore/net/http/HttpURLConnectionImpl.java
//Synthetic comment -- index a59df55..4cd9084 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import java.util.List;
import java.util.Map;
import libcore.io.Base64;

/**
* This implementation uses HttpEngine to send requests and receive responses.
//Synthetic comment -- @@ -87,6 +88,9 @@
@Override public final void disconnect() {
// Calling disconnect() before a connection exists should have no effect.
if (httpEngine != null) {
httpEngine.release(false);
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLConnectionTest.java b/luni/src/test/java/libcore/java/net/URLConnectionTest.java
//Synthetic comment -- index 347242a..4a9dd6a 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import static com.google.mockwebserver.SocketPolicy.DISCONNECT_AT_START;
import static com.google.mockwebserver.SocketPolicy.SHUTDOWN_INPUT_AT_END;
import static com.google.mockwebserver.SocketPolicy.SHUTDOWN_OUTPUT_AT_END;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -76,6 +78,7 @@
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import junit.framework.TestCase;
import libcore.java.security.TestKeyStore;
import libcore.javax.net.ssl.TestSSLContext;
import libcore.net.http.HttpResponseCache;
//Synthetic comment -- @@ -818,6 +821,50 @@
assertEquals(200, connection.getResponseCode());
}

public void testDefaultRequestProperty() throws Exception {
URLConnection.setDefaultRequestProperty("X-testSetDefaultRequestProperty", "A");
assertNull(URLConnection.getDefaultRequestProperty("X-setDefaultRequestProperty"));







