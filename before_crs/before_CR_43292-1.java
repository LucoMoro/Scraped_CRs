/*Add tests for underscores in URLs.

We're between a rock and a hard place on this. The RFCs say
that underscores aren't permitted in URLs. But browsers have
historically permitted them, and so there are websites that
depend upon them.

Attempting to support the de-facto standard rather than the
spec is difficult because support isn't universal. Mail servers
don't handle them. Internet Explorer loses cookies on them.

Further complicating things is that we can't easily support
underscores in hostnames in HttpURLConnection without also
adding support for that to java.net.URI. This would cause
awkward behavior in class that can be used for its strict
validation.

I'm recommending we stay spec-compliant. If its a major
problem for anyone, they can fix their servers.

Change-Id:I5135e0de20e11275e2459a67ec9e7c0d07b8a35bhttp://code.google.com/p/android/issues/detail?id=37577*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URITest.java b/luni/src/test/java/libcore/java/net/URITest.java
//Synthetic comment -- index b37358c..b1e567a 100644

//Synthetic comment -- @@ -659,5 +659,13 @@
}
}

// Adding a new test? Consider adding an equivalent test to URLTest.java
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLTest.java b/luni/src/test/java/libcore/java/net/URLTest.java
//Synthetic comment -- index ced8314..b31df93 100644

//Synthetic comment -- @@ -686,5 +686,13 @@
assertEquals("re f", new URL("http://host/file?query#re f").getRef());
}

// Adding a new test? Consider adding an equivalent test to URITest.java
}







