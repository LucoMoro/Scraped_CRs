/*Fix URLConnectionTest#test_getAllowUserInteraction.

Also improve the documentation, make it possible to run these tests
individually outside of CTS with vogar, and remove a few more URLs of
external web servers.

We should clean up all tests to remove all reliance on external web servers.

Bug:http://code.google.com/p/android/issues/detail?id=35400Change-Id:I28e78b7375ee554b3afe98e5249676e8bbbbec0e*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URLConnection.java b/luni/src/main/java/java/net/URLConnection.java
//Synthetic comment -- index 7cf71d5..c832bfb 100644

//Synthetic comment -- @@ -123,8 +123,8 @@
protected boolean doInput = true;

/**
     * Specifies whether this {@code URLConnection} allows user interaction as
     * it is needed for authentication purposes.
*/
protected boolean allowUserInteraction = defaultAllowUserInteraction;

//Synthetic comment -- @@ -167,11 +167,7 @@
public abstract void connect() throws IOException;

/**
     * Returns the option value which indicates whether user interaction is allowed
     * on this {@code URLConnection}.
     *
     * @return the value of the option {@code allowUserInteraction}.
     * @see #allowUserInteraction
*/
public boolean getAllowUserInteraction() {
return allowUserInteraction;
//Synthetic comment -- @@ -342,11 +338,7 @@
}

/**
     * Returns the default setting whether this connection allows user interaction.
     *
     * @return the value of the default setting {@code
     *         defaultAllowUserInteraction}.
     * @see #allowUserInteraction
*/
public static boolean getDefaultAllowUserInteraction() {
return defaultAllowUserInteraction;
//Synthetic comment -- @@ -804,16 +796,7 @@
}

/**
     * Sets the flag indicating whether this connection allows user interaction
     * or not. This method can only be called prior to the connection
     * establishment.
     *
     * @param newValue
     *            the value of the flag to be set.
     * @throws IllegalStateException
     *             if this method attempts to change the flag after the
     *             connection has been established.
     * @see #allowUserInteraction
*/
public void setAllowUserInteraction(boolean newValue) {
checkNotConnected();
//Synthetic comment -- @@ -837,14 +820,7 @@
}

/**
     * Sets the default value for the flag indicating whether this connection
     * allows user interaction or not. Existing {@code URLConnection}s are
     * unaffected.
     *
     * @param allows
     *            the default value of the flag to be used for new connections.
     * @see #defaultAllowUserInteraction
     * @see #allowUserInteraction
*/
public static void setDefaultAllowUserInteraction(boolean allows) {
defaultAllowUserInteraction = allows;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/url/FileURLConnection.java b/luni/src/main/java/libcore/net/url/FileURLConnection.java
//Synthetic comment -- index bc41169..b4654cd 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
public class FileURLConnection extends URLConnection {

    String fileName;

private InputStream is;

//Synthetic comment -- @@ -56,11 +56,11 @@
*/
public FileURLConnection(URL url) {
super(url);
        fileName = url.getFile();
        if (fileName == null) {
            fileName = "";
}
        fileName = UriCodec.decode(fileName);
}

/**
//Synthetic comment -- @@ -73,7 +73,7 @@
*/
@Override
public void connect() throws IOException {
        File f = new File(fileName);
if (f.isDirectory()) {
isDir = true;
is = getDirectoryListing(f);
//Synthetic comment -- @@ -196,7 +196,7 @@
@Override
public java.security.Permission getPermission() throws IOException {
if (permission == null) {
            String path = fileName;
if (File.separatorChar != '/') {
path = path.replace('/', File.separatorChar);
}








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java
//Synthetic comment -- index ab675e2..07e3de5 100644

//Synthetic comment -- @@ -219,8 +219,6 @@
public void setUp() throws Exception {
super.setUp();

//        ftpURL = new URL(Support_Configuration.testFTPURL);

server = new Support_TestWebServer();
port = server.initServer();
url = new URL("http://localhost:" + port + "/test1");
//Synthetic comment -- @@ -241,9 +239,6 @@
server.close();
((HttpURLConnection) uc).disconnect();
((HttpURLConnection) uc2).disconnect();
//        if (((FtpURLConnection) ftpURLCon).getInputStream() !=  null) {
//        ((FtpURLConnection) ftpURLCon).getInputStream().close();
//        }
}

/**
//Synthetic comment -- @@ -310,66 +305,21 @@
// TODO: test User-Agent?
}

    /**
     * @throws IOException
     * {@link java.net.URLConnection#getAllowUserInteraction()}
     */
    public void test_getAllowUserInteraction() throws IOException {
uc.setAllowUserInteraction(false);
        assertFalse("getAllowUserInteraction should have returned false", uc
                .getAllowUserInteraction());

uc.setAllowUserInteraction(true);
        assertTrue("getAllowUserInteraction should have returned true", uc
                .getAllowUserInteraction());

uc.connect();

try {
uc.setAllowUserInteraction(false);
            fail("Exception expected");
        } catch (IllegalStateException e) {
            //ok
}

        // test if setAllowUserInteraction works
        URL serverURL = new URL("http://onearth.jpl.nasa.gov/landsat.cgi");

        // connect to server
        URLConnection uc2 = serverURL.openConnection();
        HttpURLConnection conn = (HttpURLConnection) uc2;
        uc2.setAllowUserInteraction(true);

        uc2.setDoInput(true);
        uc2.setDoOutput(true);

        // get reference to stream to post to
        OutputStream os = uc2.getOutputStream();

        InputStream in = uc2.getInputStream();


        int contentLength = uc2.getContentLength();
        String contentType = uc2.getContentType();
        int numBytesRead = 0;
        int allBytesRead = 0;

        byte[] buffer = new byte[4096];

        do {

        numBytesRead = in.read(buffer);
        allBytesRead += allBytesRead + numBytesRead;

        } while (numBytesRead > 0);

        assertTrue(allBytesRead > 0);

        uc2.connect();

        numBytesRead = in.read(buffer);

        assertEquals(-1, numBytesRead);
}

/**
//Synthetic comment -- @@ -407,8 +357,9 @@
buf = r.readLine();
assertTrue("Incorrect content returned from fileURL: "+buf,
testString.equals(buf.trim()));
} else {
            fail("Some unkown type is returned "+obj.toString());
}

//Exception test
//Synthetic comment -- @@ -476,6 +427,8 @@
} catch (NullPointerException e) {
// expected
}
}

/**
//Synthetic comment -- @@ -514,43 +467,28 @@
/**
* {@link java.net.URLConnection#getContentLength()}
*/
    public void test_getContentLength() {
        assertEquals(testString.getBytes().length,
                fileURLCon.getContentLength());
assertEquals(Support_TestWebData.test1.length, uc.getContentLength());
assertEquals(Support_TestWebData.test2.length, uc2.getContentLength());

assertNotNull(jarURLCon.getContentLength());
assertNotNull(gifURLCon.getContentLength());
}

    /**
     * {@link java.net.URLConnection#getContentType()}
     */
    public void test_getContentType() throws IOException, MalformedURLException {

assertTrue("getContentType failed: " + fileURLCon.getContentType(),
fileURLCon.getContentType().contains("text/plain"));

URLConnection htmlFileCon = openHTMLFile();
String contentType = htmlFileCon.getContentType();
if (contentType != null) {
assertTrue(contentType.equalsIgnoreCase("text/html"));
}


        /*
        contentType = uc.getContentType();
        if (contentType != null) {
        assertTrue(contentType.equalsIgnoreCase("text/html"));
        }

        contentType = gifURLCon.getContentType();
        if (contentType != null) {
        assertTrue(contentType.equalsIgnoreCase("image/gif"));
        }
        */

}

/**
//Synthetic comment -- @@ -614,15 +552,13 @@
assertFalse("Should have been set to false", uc2.getDoInput());

fileURLCon.connect();
        fileURLCon.getInputStream();

uc2.connect();
try {
uc2.getInputStream();
        } catch (Throwable e) {
            // ok
}

}

/**
//Synthetic comment -- @@ -917,8 +853,7 @@

public void test_getOutputStream() throws IOException {
String posted = "this is a test";
        URLConnection uc3 = new URL(Support_Configuration.hTTPURLgoogle)
                .openConnection();
uc3.setDoOutput(true);
uc3.connect();

//Synthetic comment -- @@ -931,18 +866,16 @@

int code = ((HttpURLConnection) uc3).getResponseCode();


// writing to url not allowed
assertEquals("Got different responseCode ", 405, code);


// try exception testing
try {
fileURLCon.setDoOutput(true);
fileURLCon.connect();
fileURLCon.getOutputStream();
        } catch (UnknownServiceException e) {
            // ok cannot write to fileURL
}

((HttpURLConnection) uc2).disconnect();








//Synthetic comment -- diff --git a/support/src/test/java/tests/support/Support_Configuration.java b/support/src/test/java/tests/support/Support_Configuration.java
//Synthetic comment -- index fed0bc8..9cb617d 100644

//Synthetic comment -- @@ -91,18 +91,8 @@
// than one addresses returned for this host name as needed by a test
// END android-changed

    public static String testURL = "harmony.apache.org";

    public static String hTTPURLwExpiration = "http://phpwiki.sourceforge.net/phpwiki-1.2/";

public static String hTTPURLwLastModified = "http://www.php.net/manual/en/function.explode.php";

    public static String hTTPURLyahoo = "http://news.yahoo.com/";

    public static String hTTPURLgoogle = "http://www.google.com/ie";

    public static String testContentEncoding = "http://www.amazon.com/";

public static int SpecialInetTestAddressNumber = 4;

/**








//Synthetic comment -- diff --git a/support/src/test/java/tests/support/resource/Support_Resources.java b/support/src/test/java/tests/support/resource/Support_Resources.java
//Synthetic comment -- index 80a53fb..ddcb881 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
package tests.support.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -34,12 +35,25 @@
public static final String RESOURCE_PACKAGE_NAME = "tests.resources";

public static InputStream getStream(String name) {
String path = RESOURCE_PACKAGE + name;
InputStream result = Support_Resources.class.getResourceAsStream(path);
        if (result == null) {
            throw new IllegalArgumentException("No such resource: " + path);
}
        return result;
}

public static String getURL(String name) {







