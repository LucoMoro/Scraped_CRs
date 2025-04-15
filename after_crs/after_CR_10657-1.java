/*Make parseUserPart and parseHost symmetric

Currently parseUserPart uses the encoded authority to split the URI
into user and non-user parts, but the parseHost uses the decoded
URI to split the URI into non-host, host, and port parts. This gives
unexpected results when %40 ('@') is used in a URI:

Uri test = Uri.parse("http://bob%40lee@example.com/");
test.getUserInfo() => "bob@lee"
test.getHost() => "lee@example.com"*/




//Synthetic comment -- diff --git a/core/java/android/net/Uri.java b/core/java/android/net/Uri.java
//Synthetic comment -- index 08bd67e..0797949 100644

//Synthetic comment -- @@ -1025,7 +1025,7 @@
}

private String parseHost() {
            String authority = getEncodedAuthority();
if (authority == null) {
return null;
}








//Synthetic comment -- diff --git a/tests/AndroidTests/src/com/android/unit_tests/UriTest.java b/tests/AndroidTests/src/com/android/unit_tests/UriTest.java
//Synthetic comment -- index 130beeb..602cfc3 100644

//Synthetic comment -- @@ -171,6 +171,10 @@
assertEquals("bob lee", uri.getUserInfo());
assertEquals("bob%20lee", uri.getEncodedUserInfo());

        uri = Uri.parse("http://bob%40lee@localhost:42");
        assertEquals("bob@lee", uri.getUserInfo());
        assertEquals("localhost", uri.getHost());

uri = Uri.parse("http://localhost");
assertEquals("localhost", uri.getHost());
assertEquals(-1, uri.getPort());







