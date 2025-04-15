/*Fix URLConnectionTest#test_getAllowUserInteraction.

Also improve the documentation, make it possible to run these tests
individually outside of CTS with vogar, and remove a few more URLs.

We should clean up all tests to remove all reliance on external web servers.

Bug:http://code.google.com/p/android/issues/detail?id=35400Change-Id:I28e78b7375ee554b3afe98e5249676e8bbbbec0e*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URLConnection.java b/luni/src/main/java/java/net/URLConnection.java
//Synthetic comment -- index 7cf71d5..c832bfb 100644

//Synthetic comment -- @@ -123,8 +123,8 @@
protected boolean doInput = true;

/**
     * Unused by Android. This field can be accessed via {@link #getAllowUserInteraction}
     * and {@link #setAllowUserInteraction}.
*/
protected boolean allowUserInteraction = defaultAllowUserInteraction;

//Synthetic comment -- @@ -167,11 +167,7 @@
public abstract void connect() throws IOException;

/**
     * Returns {@code allowUserInteraction}. Unused by Android.
*/
public boolean getAllowUserInteraction() {
return allowUserInteraction;
//Synthetic comment -- @@ -342,11 +338,7 @@
}

/**
     * Returns the default value of {@code allowUserInteraction}. Unused by Android.
*/
public static boolean getDefaultAllowUserInteraction() {
return defaultAllowUserInteraction;
//Synthetic comment -- @@ -804,16 +796,7 @@
}

/**
     * Sets {@code allowUserInteraction}. Unused by Android.
*/
public void setAllowUserInteraction(boolean newValue) {
checkNotConnected();
//Synthetic comment -- @@ -837,14 +820,7 @@
}

/**
     * Sets the default value for {@code allowUserInteraction}. Unused by Android.
*/
public static void setDefaultAllowUserInteraction(boolean allows) {
defaultAllowUserInteraction = allows;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/url/FileURLConnection.java b/luni/src/main/java/libcore/net/url/FileURLConnection.java
//Synthetic comment -- index bc41169..b4654cd 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
public class FileURLConnection extends URLConnection {

    private String filename;

private InputStream is;

//Synthetic comment -- @@ -56,11 +56,11 @@
*/
public FileURLConnection(URL url) {
super(url);
        filename = url.getFile();
        if (filename == null) {
            filename = "";
}
        filename = UriCodec.decode(filename);
}

/**
//Synthetic comment -- @@ -73,7 +73,7 @@
*/
@Override
public void connect() throws IOException {
        File f = new File(filename);
if (f.isDirectory()) {
isDir = true;
is = getDirectoryListing(f);
//Synthetic comment -- @@ -196,7 +196,7 @@
@Override
public java.security.Permission getPermission() throws IOException {
if (permission == null) {
            String path = filename;
if (File.separatorChar != '/') {
path = path.replace('/', File.separatorChar);
}








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java
//Synthetic comment -- index ab675e2..07e3de5 100644

//Synthetic comment -- @@ -219,8 +219,6 @@
public void setUp() throws Exception {
super.setUp();

server = new Support_TestWebServer();
port = server.initServer();
url = new URL("http://localhost:" + port + "/test1");
//Synthetic comment -- @@ -241,9 +239,6 @@
server.close();
((HttpURLConnection) uc).disconnect();
((HttpURLConnection) uc2).disconnect();
}

/**
//Synthetic comment -- @@ -310,66 +305,21 @@
// TODO: test User-Agent?
}

    public void test_getAllowUserInteraction() throws Exception {
uc.setAllowUserInteraction(false);
        assertFalse(uc.getAllowUserInteraction());

uc.setAllowUserInteraction(true);
        assertTrue(uc.getAllowUserInteraction());

uc.connect();

        // Can't call the setter after connecting.
try {
uc.setAllowUserInteraction(false);
            fail();
        } catch (IllegalStateException expected) {
}
}

/**
//Synthetic comment -- @@ -407,8 +357,9 @@
buf = r.readLine();
assertTrue("Incorrect content returned from fileURL: "+buf,
testString.equals(buf.trim()));
            i.close();
} else {
            fail("Some unknown type is returned "+obj.toString());
}

//Exception test
//Synthetic comment -- @@ -476,6 +427,8 @@
} catch (NullPointerException e) {
// expected
}

        fileURLCon.getInputStream().close();
}

/**
//Synthetic comment -- @@ -514,43 +467,28 @@
/**
* {@link java.net.URLConnection#getContentLength()}
*/
    public void test_getContentLength() throws Exception {
        assertEquals(testString.getBytes().length, fileURLCon.getContentLength());
assertEquals(Support_TestWebData.test1.length, uc.getContentLength());
assertEquals(Support_TestWebData.test2.length, uc2.getContentLength());

assertNotNull(jarURLCon.getContentLength());
assertNotNull(gifURLCon.getContentLength());

        fileURLCon.getInputStream().close();
}

    public void test_getContentType() throws Exception {
assertTrue("getContentType failed: " + fileURLCon.getContentType(),
fileURLCon.getContentType().contains("text/plain"));

        fileURLCon.getInputStream().close();

URLConnection htmlFileCon = openHTMLFile();
String contentType = htmlFileCon.getContentType();
if (contentType != null) {
assertTrue(contentType.equalsIgnoreCase("text/html"));
}
}

/**
//Synthetic comment -- @@ -614,15 +552,13 @@
assertFalse("Should have been set to false", uc2.getDoInput());

fileURLCon.connect();
        fileURLCon.getInputStream().close();

uc2.connect();
try {
uc2.getInputStream();
        } catch (Throwable expected) {
}
}

/**
//Synthetic comment -- @@ -917,8 +853,7 @@

public void test_getOutputStream() throws IOException {
String posted = "this is a test";
        URLConnection uc3 = new URL("http://www.google.com/ie").openConnection();
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
            fileURLCon.getInputStream().close();
fileURLCon.getOutputStream();
        } catch (UnknownServiceException expected) {
}

((HttpURLConnection) uc2).disconnect();








//Synthetic comment -- diff --git a/support/src/test/java/tests/support/Support_Configuration.java b/support/src/test/java/tests/support/Support_Configuration.java
//Synthetic comment -- index fed0bc8..9cb617d 100644

//Synthetic comment -- @@ -91,18 +91,8 @@
// than one addresses returned for this host name as needed by a test
// END android-changed

public static String hTTPURLwLastModified = "http://www.php.net/manual/en/function.explode.php";

public static int SpecialInetTestAddressNumber = 4;

/**








//Synthetic comment -- diff --git a/support/src/test/java/tests/support/resource/Support_Resources.java b/support/src/test/java/tests/support/resource/Support_Resources.java
//Synthetic comment -- index 80a53fb..ddcb881 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
package tests.support.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -34,12 +35,25 @@
public static final String RESOURCE_PACKAGE_NAME = "tests.resources";

public static InputStream getStream(String name) {
        // If we have the resources packaged up in our jar file, get them that way.
String path = RESOURCE_PACKAGE + name;
InputStream result = Support_Resources.class.getResourceAsStream(path);
        if (result != null) {
            return result;
}
        // Otherwise, if we're in an Android build tree, get the files directly.
        String ANDROID_BUILD_TOP = System.getenv("ANDROID_BUILD_TOP");
        if (ANDROID_BUILD_TOP != null) {
            File resource = new File(ANDROID_BUILD_TOP + "/libcore/support/src/test/java" + path);
            if (resource.exists()) {
                try {
                    return new FileInputStream(resource);
                } catch (IOException ex) {
                    throw new IllegalArgumentException("Couldn't open: " + resource, ex);
                }
            }
        }
        throw new IllegalArgumentException("No such resource: " + path);
}

public static String getURL(String name) {







