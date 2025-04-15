/*Tracking DSA Signature.verify behavioral change

See also:

    commit 3ff7a80532c9edbcca3331de2b9e87bbf16a0c96
    Author: Brian Carlstrom <bdc@google.com>
    Date:   Mon Jul 23 23:33:56 2012 -0700

        Signature.verify should not throw if called twice

Bug:http://code.google.com/p/android/issues/detail?id=34933Bug: 6870225

(cherry picked from commit b354e381cac5ffe6f884a4513127d68324779c3b)

Change-Id:Ifbd407d8228befb36fd0a57cd0c846c9760d3d4c*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/java/security/Signature2Test.java b/luni/src/test/java/org/apache/harmony/security/tests/java/security/Signature2Test.java
//Synthetic comment -- index 39423ee..7886e3e 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import java.security.spec.DSAParameterSpec;
import java.util.HashSet;
import java.util.Set;
import libcore.java.security.StandardNames;

public class Signature2Test extends junit.framework.TestCase {

//Synthetic comment -- @@ -467,10 +468,17 @@
} catch (IllegalArgumentException expected) {
}

        if (StandardNames.IS_RI) {
            try {
                sig.verify(signature, signature.length, 0);
                fail();
            } catch (SignatureException expected) {
            }
        } else {
            // Calling Signature.verify a second time should not throw
            // http://code.google.com/p/android/issues/detail?id=34933
            boolean verified = sig.verify(signature, signature.length, 0);
            assertFalse(verified);
}

try {







