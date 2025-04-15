/*Updating KeyStore2Test to track change in behavior of KeyStoreSpi.engineSetEntry

Whether the KeyStore.setEntry call throws with an null
ProtectionParameter depends on the KeyStore algorithm.

    commit d951031b428eb2885dca51e0484d5d29e0caad44
    Author: Brian Carlstrom <bdc@google.com>
    Date:   Fri Aug 17 17:23:24 2012 -0700

        KeyStoreSpi.engineSetEntry should allow null ProtectionParameter

        Change-Id:Ibf5be502a70a63abccb6e45dc6b6ff18b2c63ca9Change-Id:I5df8d577acb8c5e30e20c4606b2a1e8e6b5de0b9*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/java/security/KeyStore2Test.java b/luni/src/test/java/org/apache/harmony/security/tests/java/security/KeyStore2Test.java
//Synthetic comment -- index fd27edc..eb3c524 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import libcore.java.security.StandardNames;
import tests.support.Support_TestProvider;

public class KeyStore2Test extends junit.framework.TestCase {
//Synthetic comment -- @@ -817,8 +818,9 @@

try {
keyTest.setEntry("alias", pke, null);
            assertFalse(StandardNames.IS_RI);  // BKS KeyStore does not require a password
        } catch (Exception maybeExpected) {
            assertTrue(StandardNames.IS_RI);  // JKS KeyStore requires a password
}

keyTest.setEntry("alias", pke, pp);







