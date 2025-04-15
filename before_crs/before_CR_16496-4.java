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
for (int i = 0; i < controls.length; i++) {
if (NO_STORE.equals(controls[i])) {
return null;
//Synthetic comment -- @@ -762,7 +763,12 @@
// can only be used in CACHE_MODE_CACHE_ONLY case
if (NO_CACHE.equals(controls[i])) {
ret.expires = 0;
                } else if (controls[i].startsWith(MAX_AGE)) {
int separator = controls[i].indexOf('=');
if (separator < 0) {
separator = controls[i].indexOf(':');








//Synthetic comment -- diff --git a/tests/CoreTests/android/core/HttpHeaderTest.java b/tests/CoreTests/android/core/HttpHeaderTest.java
//Synthetic comment -- index a5d48578..4b78db6 100644

//Synthetic comment -- @@ -19,12 +19,18 @@
import org.apache.http.util.CharArrayBuffer;

import android.net.http.Headers;

public class HttpHeaderTest extends AndroidTestCase {

static final String LAST_MODIFIED = "Last-Modified: Fri, 18 Jun 2010 09:56:47 GMT";
static final String CACHE_CONTROL_MAX_AGE = "Cache-Control:max-age=15";
static final String CACHE_CONTROL_PRIVATE = "Cache-Control: private";

/**
* Tests that cache control header supports multiple instances of the header,
//Synthetic comment -- @@ -59,4 +65,31 @@
h.parseHeader(buffer);
assertEquals("max-age=15,private", h.getCacheControl());
}
}







