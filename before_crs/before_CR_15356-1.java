/*Fix for bug # 2337042  URL that starts with "/."

This is a potential fix for the above bug, in which "." or "/." was
being inserted into the beginning of urls.  This was not causing
any particular problems but is inconsistent with other browsers
and potentially confusing.  I have added a check to URLUtil to
remove any leading periods.

Change-Id:Ie0280faae257f6855a3e49d5f00c6742b9e9cf2d*/
//Synthetic comment -- diff --git a/core/java/android/webkit/URLUtil.java b/core/java/android/webkit/URLUtil.java
//Synthetic comment -- index 232ed36..075a3a9 100644

//Synthetic comment -- @@ -56,6 +56,14 @@
if (inUrl.endsWith(".") == true) {
inUrl = inUrl.substring(0, inUrl.length() - 1);
}

try {
webAddress = new WebAddress(inUrl);







