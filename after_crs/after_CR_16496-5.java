/*Error in compound cache-control header.

A cache control header containing both no-cache and max-age attribute does not
behave as expected.

Cache-Control: no-cache, max-age=200000

Will set expired to 20000ms seconds, ignoring the no-cache header. My
interpretation is that the no-cache header should not be ignored in
this case.

Change-Id:Iadd1900e4d2c6c0dacc6bb3e7b944cf78ca9b266*/




//Synthetic comment -- diff --git a/core/java/android/webkit/CacheManager.java b/core/java/android/webkit/CacheManager.java
//Synthetic comment -- index d171990..eff8e61 100644

//Synthetic comment -- @@ -752,6 +752,7 @@
String cacheControl = headers.getCacheControl();
if (cacheControl != null) {
String[] controls = cacheControl.toLowerCase().split("[ ,;]");
            boolean noCache = false;
for (int i = 0; i < controls.length; i++) {
if (NO_STORE.equals(controls[i])) {
return null;
//Synthetic comment -- @@ -762,7 +763,12 @@
// can only be used in CACHE_MODE_CACHE_ONLY case
if (NO_CACHE.equals(controls[i])) {
ret.expires = 0;
                    noCache = true;
                // if cache control = no-cache has been received, ignore max-age
                // header, according to http spec:
                // If a request includes the no-cache directive, it SHOULD NOT
                // include min-fresh, max-stale, or max-age.
                } else if (controls[i].startsWith(MAX_AGE) && !noCache) {
int separator = controls[i].indexOf('=');
if (separator < 0) {
separator = controls[i].indexOf(':');








//Synthetic comment -- diff --git a/tests/CoreTests/android/core/HttpHeaderTest.java b/tests/CoreTests/android/core/HttpHeaderTest.java
//Synthetic comment -- index a5d48578..eedbc3f 100644

//Synthetic comment -- @@ -19,12 +19,19 @@
import org.apache.http.util.CharArrayBuffer;

import android.net.http.Headers;
import android.util.Log;
import android.webkit.CacheManager;
import android.webkit.CacheManager.CacheResult;

import java.lang.reflect.Method;

public class HttpHeaderTest extends AndroidTestCase {

static final String LAST_MODIFIED = "Last-Modified: Fri, 18 Jun 2010 09:56:47 GMT";
static final String CACHE_CONTROL_MAX_AGE = "Cache-Control:max-age=15";
static final String CACHE_CONTROL_PRIVATE = "Cache-Control: private";
    static final String CACHE_CONTROL_COMPOUND = "Cache-Control: no-cache, max-age=200000";
    static final String CACHE_CONTROL_COMPOUND2 = "Cache-Control: max-age=200000, no-cache";

/**
* Tests that cache control header supports multiple instances of the header,
//Synthetic comment -- @@ -59,4 +66,39 @@
h.parseHeader(buffer);
assertEquals("max-age=15,private", h.getCacheControl());
}

    // Test that cache behaves correctly when receiving a compund
    // cache-control statement containing no-cache and max-age argument.
    //
    // If a cache control header contains both a max-age arument and
    // a no-cache argument the max-age argument should be ignored.
    // The resource can be cached, but a validity check must be done on
    // every request. Test case checks that the expiry time is 0 for
    // this item, so item will be validated on subsequent requests.
    public void testCacheControlMultipleArguments() throws Exception {
        // get private method CacheManager.parseHeaders()
        Method m = CacheManager.class.getDeclaredMethod("parseHeaders",
                new Class[] {int.class, Headers.class, String.class});
        m.setAccessible(true);

        // create indata
        Headers h = new Headers();
        CharArrayBuffer buffer = new CharArrayBuffer(64);
        buffer.append(CACHE_CONTROL_COMPOUND);
        h.parseHeader(buffer);

        CacheResult c = (CacheResult)m.invoke(null, 200, h, "text/html");

        // Check that expires is set to 0, to ensure that no-cache has overridden
        // the max-age argument
        assertEquals(0, c.getExpires());

        // check reverse order
        buffer.clear();
        buffer.append(CACHE_CONTROL_COMPOUND2);
        h.parseHeader(buffer);

        c = (CacheResult)m.invoke(null, 200, h, "text/html");
        assertEquals(0, c.getExpires());
    }
}







