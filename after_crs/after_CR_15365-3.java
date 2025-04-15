/*Bug fix 2337042 <base> URL + <img> URL = URL that starts with "/."

The Android browser inserts "/." at the beginning of the URL path
when processing an <img>  in an HTML document with a <base> tag.

Adding check to WebAddress to remove any leading '.' from the path
portion of a URI.  This must take place within WebAddress as
there is no earlier point at which the URL is parsed into a URI.

Change-Id:Ie53b07bb173e7e384fdb0b406a04cf26a9b27dc4*/




//Synthetic comment -- diff --git a/core/java/android/net/WebAddress.java b/core/java/android/net/WebAddress.java
//Synthetic comment -- index 4101ab4..fc07591 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
static Pattern sAddressPattern = Pattern.compile(
/* scheme    */ "(?:(http|https|file)\\:\\/\\/)?" +
/* authority */ "(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?" +
            /* host      */ "([-" + GOOD_IRI_CHAR + "%_]+(?:\\.[-" + GOOD_IRI_CHAR + "%_]+)*\\.*|\\[[0-9a-fA-F:\\.]+\\])?" +
/* port      */ "(?:\\:([0-9]*))?" +
/* path      */ "(\\/?[^#]*)?" +
/* anchor    */ ".*", Pattern.CASE_INSENSITIVE);







