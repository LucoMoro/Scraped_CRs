/*Tracking DSA Signature.verify behavioral change

See also:

    commit 3ff7a80532c9edbcca3331de2b9e87bbf16a0c96
    Author: Brian Carlstrom <bdc@google.com>
    Date:   Mon Jul 23 23:33:56 2012 -0700

        Signature.verify should not throw if called twice

Bug:http://code.google.com/p/android/issues/detail?id=34933Bug: 6870225
Change-Id:Ifa4324e0d3454adcc08667e91677485bdfdcf1d5*/
//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/java/security/Signature2Test.java b/luni/src/test/java/org/apache/harmony/security/tests/java/security/Signature2Test.java
//Synthetic comment -- index 39423ee..ccb4097 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import java.security.spec.DSAParameterSpec;
import java.util.HashSet;
import java.util.Set;

public class Signature2Test extends junit.framework.TestCase {

//Synthetic comment -- @@ -467,11 +468,19 @@
} catch (IllegalArgumentException expected) {
}

        try {
            sig.verify(signature, signature.length, 0);
            fail();
        } catch (SignatureException expected) {
}

try {
sig.verify(signature, 0, signature.length * 2);







