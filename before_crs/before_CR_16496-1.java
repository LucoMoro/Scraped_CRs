/*Error in compound cache-control header.

A cache control header containing both no-cache and max-age attribute does not
behave as expected.

Cache-Control: no-cache, max-age=200000

Will set expired to 20000ms seconds, ignoring the no-cache header. My
interpretation is that the no-cache header should not be ignored in
this case.

Change-Id:Iadd1900e4d2c6c0dacc6bb3e7b944cf78ca9b266*/
//Synthetic comment -- diff --git a/core/java/android/webkit/CacheManager.java b/core/java/android/webkit/CacheManager.java
//Synthetic comment -- index d5058b0..bf1def9 100644

//Synthetic comment -- @@ -752,6 +752,7 @@
String cacheControl = headers.getCacheControl();
if (cacheControl != null) {
String[] controls = cacheControl.toLowerCase().split("[ ,;]");
for (int i = 0; i < controls.length; i++) {
if (NO_STORE.equals(controls[i])) {
return null;
//Synthetic comment -- @@ -762,7 +763,8 @@
// can only be used in CACHE_MODE_CACHE_ONLY case
if (NO_CACHE.equals(controls[i])) {
ret.expires = 0;
                } else if (controls[i].startsWith(MAX_AGE)) {
int separator = controls[i].indexOf('=');
if (separator < 0) {
separator = controls[i].indexOf(':');







