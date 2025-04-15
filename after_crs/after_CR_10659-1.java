/*Make Uri.parseUserPart, parseHost, and parsePort symmetric

Currently parseUserPart uses the encoded authority to split the URI
into user and non-user parts, but the parseHost and parsePort uses
the decoded URI to split the URI into non-host, host, and port parts.
This gives unexpected results when %40 ('@') and %3a (':') is used
in a URI:

Uri test = Uri.parse("http://bob%40lee%3ajr@example.com:42/");
test.getUserInfo() => "bob@lee:jr"
test.getHost() => "lee:jr@example.com" (should be "example.com")
test.getPort() => -1 (should be 42)*/




//Synthetic comment -- diff --git a/core/java/android/net/Uri.java b/core/java/android/net/Uri.java
//Synthetic comment -- index 08bd67e..a74585e 100644

//Synthetic comment -- @@ -1025,7 +1025,7 @@
}

private String parseHost() {
            String authority = getEncodedAuthority();
if (authority == null) {
return null;
}
//Synthetic comment -- @@ -1034,9 +1034,11 @@
int userInfoSeparator = authority.indexOf('@');
int portSeparator = authority.indexOf(':', userInfoSeparator);

            String encodedHost = portSeparator == NOT_FOUND
? authority.substring(userInfoSeparator + 1)
: authority.substring(userInfoSeparator + 1, portSeparator);

            return decode(encodedHost);
}

private volatile int port = NOT_CALCULATED;
//Synthetic comment -- @@ -1048,7 +1050,7 @@
}

private int parsePort() {
            String authority = getEncodedAuthority();
if (authority == null) {
return -1;
}
//Synthetic comment -- @@ -1062,7 +1064,7 @@
return -1;
}

            String portString = decode(authority.substring(portSeparator + 1));
try {
return Integer.parseInt(portString);
} catch (NumberFormatException e) {








//Synthetic comment -- diff --git a/tests/AndroidTests/src/com/android/unit_tests/UriTest.java b/tests/AndroidTests/src/com/android/unit_tests/UriTest.java
//Synthetic comment -- index 130beeb..3191cc9 100644

//Synthetic comment -- @@ -171,6 +171,11 @@
assertEquals("bob lee", uri.getUserInfo());
assertEquals("bob%20lee", uri.getEncodedUserInfo());

        uri = Uri.parse("http://bob%40lee%3ajr@local%68ost:4%32");
        assertEquals("bob@lee:jr", uri.getUserInfo());
        assertEquals("localhost", uri.getHost());
        assertEquals(42, uri.getPort());

uri = Uri.parse("http://localhost");
assertEquals("localhost", uri.getHost());
assertEquals(-1, uri.getPort());







