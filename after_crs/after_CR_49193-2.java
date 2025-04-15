/*Should favor most specific CN when working with distinguished names

This reverts a regression introduced in commit 1331404bf45cb2f220ee9aa2c0c108ce59453a74
that was caught by tests.api.javax.net.ssl.HostnameVerifierTest.testVerify

Bug: 7894348
Bug:http://code.google.com/p/android/issues/detail?id=41662Change-Id:Iec8000b716e3d99ca7af4aa2c3fd7b43e22c68cd*/




//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java b/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java
//Synthetic comment -- index a26a9ec..c3c1606 100644

//Synthetic comment -- @@ -39,6 +39,9 @@
private char[] chars;

public DistinguishedNameParser(X500Principal principal) {
        // RFC2253 is used to ensure we get attributes in the reverse
        // order of the underlying ASN.1 encoding, so that the most
        // significant values of repeated attributes occur first.
this.dn = principal.getName(X500Principal.RFC2253);
this.length = this.dn.length();
}
//Synthetic comment -- @@ -357,15 +360,11 @@
if (attType == null) {
return null;
}
while (true) {
String attValue = "";

if (pos == length) {
                return null;
}

switch (chars[pos]) {
//Synthetic comment -- @@ -384,12 +383,15 @@
attValue = escapedAV();
}

            // Values are ordered from most specific to least specific
            // due to the RFC2253 formatting. So take the first match
            // we see.
if (attributeType.equalsIgnoreCase(attType)) {
                return attValue;
}

if (pos >= length) {
                return null;
}

if (chars[pos] == ',' || chars[pos] == ';') {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/DistinguishedNameParserTest.java b/luni/src/test/java/libcore/javax/net/ssl/DistinguishedNameParserTest.java
//Synthetic comment -- index 19430de..723c697 100644

//Synthetic comment -- @@ -21,35 +21,35 @@
import junit.framework.TestCase;

public final class DistinguishedNameParserTest extends TestCase {
    public void testGetFirstCn() {
        assertFirstCn("", null);
        assertFirstCn("ou=xxx", null);
        assertFirstCn("ou=xxx,cn=xxx", "xxx");
        assertFirstCn("ou=xxx+cn=yyy,cn=zzz+cn=abc", "yyy");
        assertFirstCn("cn=a,cn=b", "a");
        assertFirstCn("cn=Cc,cn=Bb,cn=Aa", "Cc");
        assertFirstCn("cn=imap.gmail.com", "imap.gmail.com");
}

public void testGetFirstCnWithOid() {
        assertFirstCn("2.5.4.3=a,ou=xxx", "a");
}

public void testGetFirstCnWithQuotedStrings() {
        assertFirstCn("cn=\"\\\" a ,=<>#;\"", "\" a ,=<>#;");
        assertFirstCn("cn=abc\\,def", "abc,def");
}

public void testGetFirstCnWithUtf8() {
        assertFirstCn("cn=Lu\\C4\\8Di\\C4\\87", "\u004c\u0075\u010d\u0069\u0107");
}

public void testGetFirstCnWithWhitespace() {
        assertFirstCn("ou=a, cn=  a  b  ,o=x", "a  b");
        assertFirstCn("cn=\"  a  b  \" ,o=x", "  a  b  ");
}

    private void assertFirstCn(String dn, String expected) {
X500Principal principal = new X500Principal(dn);
assertEquals(dn, expected, new DistinguishedNameParser(principal).findMostSpecific("cn"));
}







