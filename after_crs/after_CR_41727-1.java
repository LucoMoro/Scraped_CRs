/*KeyStoreSpi.engineSetEntry should allow null ProtectionParameter

Change-Id:Ibf5be502a70a63abccb6e45dc6b6ff18b2c63ca9*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyStoreSpi.java b/luni/src/main/java/java/security/KeyStoreSpi.java
//Synthetic comment -- index 01565f5..5ae9a72 100644

//Synthetic comment -- @@ -414,14 +414,14 @@
}

char[] passW = null;
        if (protParam != null) {
            if (protParam instanceof KeyStore.PasswordProtection) {
                try {
                    passW = ((KeyStore.PasswordProtection) protParam).getPassword();
                } catch (IllegalStateException ee) {
                    throw new KeyStoreException("Password was destroyed", ee);
                }
            } else if (protParam instanceof KeyStore.CallbackHandlerProtection) {
try {
passW = getPasswordFromCallBack(protParam);
} catch (Exception e) {








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/java/security/KeyStoreSpiTest.java b/luni/src/test/java/org/apache/harmony/security/tests/java/security/KeyStoreSpiTest.java
//Synthetic comment -- index 4fdddbb..a85459b 100644

//Synthetic comment -- @@ -15,21 +15,15 @@
*  limitations under the License.
*/

package org.apache.harmony.security.tests.java.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.Entry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
//Synthetic comment -- @@ -39,16 +33,15 @@
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Date;
import javax.crypto.SecretKey;
import junit.framework.TestCase;
import org.apache.harmony.security.tests.support.MyKeyStoreSpi;
import org.apache.harmony.security.tests.support.MyLoadStoreParams;

public class KeyStoreSpiTest extends TestCase {

@SuppressWarnings("cast")
//Synthetic comment -- @@ -86,62 +79,73 @@
try {
assertFalse(ksSpi.engineEntryInstanceOf(null,
KeyStore.TrustedCertificateEntry.class));
        } catch (NullPointerException expected) {
}

try {
assertFalse(ksSpi.engineEntryInstanceOf(
"test_engineEntryInstanceOf_Alias1", null));
        } catch (NullPointerException expected) {
}


}

    public void testKeyStoreSpi01() throws IOException,
NoSuchAlgorithmException, CertificateException,
UnrecoverableEntryException, KeyStoreException {
        final boolean[] keyEntryWasSet = new boolean[1];
        KeyStoreSpi ksSpi = new MyKeyStoreSpi() {
            @Override public void engineSetKeyEntry(String alias, Key key, char[] password,
                Certificate[] chain) throws KeyStoreException { keyEntryWasSet[0] = true; }
        };

        BadKeyStoreEntry badEntry = new BadKeyStoreEntry();
        BadKeyStoreProtectionParameter badParameter = new BadKeyStoreProtectionParameter();

        KeyStore.SecretKeyEntry dummyEntry = new KeyStore.SecretKeyEntry(new SecretKey() {
            @Override public String getAlgorithm() { return null; }
            @Override public String getFormat() { return null; }
            @Override public byte[] getEncoded() { return null; }
        });

try {
ksSpi.engineStore(null);
        } catch (UnsupportedOperationException expected) {
}
assertNull("Not null entry", ksSpi.engineGetEntry("aaa", null));
        assertNull("Not null entry", ksSpi.engineGetEntry(null, badParameter));
        assertNull("Not null entry", ksSpi.engineGetEntry("aaa", badParameter));

try {
ksSpi.engineSetEntry("", null, null);
fail("KeyStoreException or NullPointerException must be thrown");
        } catch (KeyStoreException expected) {
        } catch (NullPointerException expected) {
}

try {
ksSpi.engineSetEntry("", new KeyStore.TrustedCertificateEntry(
new MyCertificate("type", new byte[0])), null);
fail("KeyStoreException must be thrown");
        } catch (KeyStoreException expected) {
}

try {
            ksSpi.engineSetEntry("aaa", badEntry, null);
fail("KeyStoreException must be thrown");
        } catch (KeyStoreException expected) {
}

        ksSpi.engineSetEntry("aaa", dummyEntry, null);
        assertTrue(keyEntryWasSet[0]);
}

/**
* Test for <code>KeyStoreSpi()</code> constructor and abstract engine
* methods. Assertion: creates new KeyStoreSpi object.
*/
    public void testKeyStoreSpi02() throws NoSuchAlgorithmException,
UnrecoverableKeyException, CertificateException {
KeyStoreSpi ksSpi = new MyKeyStoreSpi();
assertNull("engineGetKey(..) must return null", ksSpi.engineGetKey("",
//Synthetic comment -- @@ -155,23 +159,23 @@
try {
ksSpi.engineSetKeyEntry("", null, new char[0], new Certificate[0]);
fail("KeyStoreException must be thrown from engineSetKeyEntry(..)");
        } catch (KeyStoreException expected) {
}
try {
ksSpi.engineSetKeyEntry("", new byte[0], new Certificate[0]);
fail("KeyStoreException must be thrown from engineSetKeyEntry(..)");
        } catch (KeyStoreException expected) {
}
try {
ksSpi.engineSetCertificateEntry("", null);
fail("KeyStoreException must be thrown "
+ "from engineSetCertificateEntry(..)");
        } catch (KeyStoreException expected) {
}
try {
ksSpi.engineDeleteEntry("");
fail("KeyStoreException must be thrown from engineDeleteEntry(..)");
        } catch (KeyStoreException expected) {
}
assertNull("engineAliases() must return null", ksSpi.engineAliases());
assertFalse("engineContainsAlias(..) must return false", ksSpi
//Synthetic comment -- @@ -180,7 +184,7 @@
try {
ksSpi.engineStore(null, null);
fail("IOException must be thrown");
        } catch (IOException expected) {
}
}

//Synthetic comment -- @@ -202,35 +206,30 @@
try {
ksSpi.engineLoad(null);
fail("Should throw exception");
        } catch (RuntimeException expected) {
            assertSame(msg, expected.getMessage());
}

// test: protection parameter is null
try {
ksSpi.engineLoad(new MyLoadStoreParams(null));
fail("No expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
}

// test: protection parameter is not instanceof
// PasswordProtection or CallbackHandlerProtection
try {
            ksSpi.engineLoad(new MyLoadStoreParams(new BadKeyStoreProtectionParameter()));
fail("No expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
}
}
}

// These are "Bad" because they are not expected inner subclasses of the KeyStore class.
class BadKeyStoreEntry implements Entry {}
class BadKeyStoreProtectionParameter implements ProtectionParameter {}

@SuppressWarnings("unused")
class MyCertificate extends Certificate {







