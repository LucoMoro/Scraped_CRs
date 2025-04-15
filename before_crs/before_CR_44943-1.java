/*Make sure URL.toURILenient throws the proper exception on trailing garbage escape

Previously this would throw IndexOutOfBoundsException instead of the proper checked exception.

Bug: 7369778
Change-Id:I4247240b21a98688bd890c53f654f7a030d72717*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/UriCodec.java b/luni/src/main/java/libcore/net/UriCodec.java
//Synthetic comment -- index 6624474..3db95d0 100644

//Synthetic comment -- @@ -111,7 +111,7 @@
}
if (c == '%' && isPartiallyEncoded) {
// this is an encoded 3-character sequence like "%20"
                    builder.append(s, i, i + 3);
i += 2;
} else if (c == ' ') {
builder.append('+');








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLTest.java b/luni/src/test/java/libcore/java/net/URLTest.java
//Synthetic comment -- index 962088e..01515bb 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.TestCase;
import libcore.util.SerializationTester;
//Synthetic comment -- @@ -694,5 +695,24 @@
assertEquals("a_b.c.d.net", url.getHost());
}

// Adding a new test? Consider adding an equivalent test to URITest.java
}







