/*Fix for bug 2337042 URL starts with additional '/.'

The Android browser inserts "/." at the beginning of the URL path
when processing an <img> tag in an HTML document with a <base> tag.

Adding check to WebAddress to remove any leading '.' from the path
portion of a URI.

Change-Id:I3b2aa9c33906b8b9af931a96c773453885193eee*/




//Synthetic comment -- diff --git a/core/java/android/net/WebAddress.java b/core/java/android/net/WebAddress.java
//Synthetic comment -- index f4ae66a..ba95ce2 100644

//Synthetic comment -- @@ -91,6 +91,11 @@
}
t = m.group(MATCH_GROUP_PATH);
if (t != null && t.length() > 0) {
            	/* handle bug 2337042 where a leading '.' is not properly
            	   cleared.*/
            	if (t.charAt(0) == '.') {
		    t = t.substring(1);
            	}
/* handle busted myspace frontpage redirect with
missing initial "/" */
if (t.charAt(0) == '/') {







