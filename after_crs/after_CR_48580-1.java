/*Should favor last CN when working with distinguished names

Bug: 7894348
Bug:http://code.google.com/p/android/issues/detail?id=41662Change-Id:I3814d653b628f6af12ce1ba59b39b1c7cc45e124*/




//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/DefaultHostnameVerifier.java b/luni/src/main/java/javax/net/ssl/DefaultHostnameVerifier.java
//Synthetic comment -- index e68baca..d73a5b7 100644

//Synthetic comment -- @@ -80,7 +80,8 @@

if (!hasDns) {
X500Principal principal = certificate.getSubjectX500Principal();
            // RFC 2818 advises uses the most specific name for matching.
            String cn = new DistinguishedNameParser(principal).findMostSpecific("cn");
if (cn != null) {
return verifyHostName(hostName, cn);
}








//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java b/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java
//Synthetic comment -- index abe11a0..5d784f7 100644

//Synthetic comment -- @@ -340,12 +340,14 @@
}

/**
     * Parses the DN and returns the most significant attribute value
     * for an attribute type. Values are ordered from least specific
     * to most specific.
*
* @param attributeType attribute type to look for (e.g. "ca")
     * @return value of the last attribute of the type found, or null if none found
*/
    public String findMostSpecific(String attributeType) {
// Initialize internal state.
pos = 0;
beg = 0;
//Synthetic comment -- @@ -357,11 +359,12 @@
if (attType == null) {
return null;
}
        String result = null;
while (true) {
String attValue = "";

if (pos == length) {
                return result;
}

switch (chars[pos]) {
//Synthetic comment -- @@ -381,11 +384,11 @@
}

if (attributeType.equalsIgnoreCase(attType)) {
                result = attValue;
}

if (pos >= length) {
                return result;
}

if (chars[pos] == ',' || chars[pos] == ';') {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/DefaultHostnameVerifierTest.java b/luni/src/test/java/libcore/javax/net/ssl/DefaultHostnameVerifierTest.java
//Synthetic comment -- index 7cb7792..69ae1c1 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import java.util.List;
import java.util.Set;
import javax.net.ssl.DefaultHostnameVerifier;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;

//Synthetic comment -- @@ -42,39 +41,6 @@

private final DefaultHostnameVerifier verifier = new DefaultHostnameVerifier();

public void testVerify() {
assertTrue(verifier.verify("imap.g.com", new StubX509Certificate("cn=imap.g.com")));
assertFalse(verifier.verify("imap.g.com", new StubX509Certificate("cn=imap2.g.com")));








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/DistinguishedNameParserTest.java b/luni/src/test/java/libcore/javax/net/ssl/DistinguishedNameParserTest.java
new file mode 100644
//Synthetic comment -- index 0000000..19430de

//Synthetic comment -- @@ -0,0 +1,56 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package libcore.javax.net.ssl;

import javax.net.ssl.DistinguishedNameParser;
import javax.security.auth.x500.X500Principal;
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
}







