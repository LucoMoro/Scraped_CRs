/*Should favor most specific CN when working with distinguished names

This reverts a regression introduced in commit 1331404bf45cb2f220ee9aa2c0c108ce59453a74
that was caught by tests.api.javax.net.ssl.HostnameVerifierTest.testVerify

Bug: 7894348
Bug:http://code.google.com/p/android/issues/detail?id=41662Change-Id:Iec8000b716e3d99ca7af4aa2c3fd7b43e22c68cd*/
//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java b/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java
//Synthetic comment -- index a26a9ec..f8dae18 100644

//Synthetic comment -- @@ -39,6 +39,9 @@
private char[] chars;

public DistinguishedNameParser(X500Principal principal) {
this.dn = principal.getName(X500Principal.RFC2253);
this.length = this.dn.length();
}
//Synthetic comment -- @@ -357,15 +360,11 @@
if (attType == null) {
return null;
}
        // Values are ordered from least specific to most specific. We
        // remember the most recent choice in result and return it
        // when we reach the end of the input.
        String result = null;
while (true) {
String attValue = "";

if (pos == length) {
                return result;
}

switch (chars[pos]) {
//Synthetic comment -- @@ -384,12 +383,15 @@
attValue = escapedAV();
}

if (attributeType.equalsIgnoreCase(attType)) {
                result = attValue;
}

if (pos >= length) {
                return result;
}

if (chars[pos] == ',' || chars[pos] == ';') {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/DistinguishedNameParserTest.java b/luni/src/test/java/libcore/javax/net/ssl/DistinguishedNameParserTest.java
//Synthetic comment -- index 19430de..723c697 100644

//Synthetic comment -- @@ -21,35 +21,35 @@
import junit.framework.TestCase;

public final class DistinguishedNameParserTest extends TestCase {
    public void testGetLastCn() {
        assertLastCn("", null);
        assertLastCn("ou=xxx", null);
        assertLastCn("ou=xxx,cn=xxx", "xxx");
        assertLastCn("ou=xxx+cn=yyy,cn=zzz+cn=abc", "abc");
        assertLastCn("cn=a,cn=b", "b");
        assertLastCn("cn=Cc,cn=Bb,cn=Aa", "Aa");
        assertLastCn("cn=imap.gmail.com", "imap.gmail.com");
}

public void testGetFirstCnWithOid() {
        assertLastCn("2.5.4.3=a,ou=xxx", "a");
}

public void testGetFirstCnWithQuotedStrings() {
        assertLastCn("cn=\"\\\" a ,=<>#;\"", "\" a ,=<>#;");
        assertLastCn("cn=abc\\,def", "abc,def");
}

public void testGetFirstCnWithUtf8() {
        assertLastCn("cn=Lu\\C4\\8Di\\C4\\87", "\u004c\u0075\u010d\u0069\u0107");
}

public void testGetFirstCnWithWhitespace() {
        assertLastCn("ou=a, cn=  a  b  ,o=x", "a  b");
        assertLastCn("cn=\"  a  b  \" ,o=x", "  a  b  ");
}

    private void assertLastCn(String dn, String expected) {
X500Principal principal = new X500Principal(dn);
assertEquals(dn, expected, new DistinguishedNameParser(principal).findMostSpecific("cn"));
}







