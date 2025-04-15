/*Bug fix 2337042 <base> URL + <img> URL = URL that starts with "/."

The Android browser inserts "/." at the beginning of the URL path
when processing an <img>  in an HTML document with a <base> tag.

Adding check to WebAddress to remove any leading '.' from the path
portion of a URI.  This must take place within WebAddress as
there is no earlier point at which the URL is parsed into a URI.

Change-Id:Ie53b07bb173e7e384fdb0b406a04cf26a9b27dc4*/
//Synthetic comment -- diff --git a/core/java/android/net/WebAddress.java b/core/java/android/net/WebAddress.java
//Synthetic comment -- index f4ae66a..c95833a 100644

//Synthetic comment -- @@ -91,6 +91,11 @@
}
t = m.group(MATCH_GROUP_PATH);
if (t != null && t.length() > 0) {
/* handle busted myspace frontpage redirect with
missing initial "/" */
if (t.charAt(0) == '/') {







