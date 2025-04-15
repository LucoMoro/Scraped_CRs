/*Make debug key expire in 30 years (instead of 1).

The 1 year expiration on the default debug key
is made annoying since we don't regenerate it
automatically when it expires.

Also added a simple unit test to check key creation
and expiration date.

SDK Bug:http://code.google.com/p/android/issues/detail?id=15370Change-Id:Ie1ee14d8888275c2dae282bfb1235af54753ac0e*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/DebugKeyProvider.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/DebugKeyProvider.java
//Synthetic comment -- index bef7ccf..d31414c 100644

//Synthetic comment -- @@ -36,49 +36,49 @@
* <p/>This provider uses a custom keystore to create and store a key with a known password.
*/
public class DebugKeyProvider {

public interface IKeyGenOutput {
public void out(String message);
public void err(String message);
}

private static final String PASSWORD_STRING = "android";
private static final char[] PASSWORD_CHAR = PASSWORD_STRING.toCharArray();
private static final String DEBUG_ALIAS = "AndroidDebugKey";

// Certificate CN value. This is a hard-coded value for the debug key.
// Android Market checks against this value in order to refuse applications signed with
// debug keys.
private static final String CERTIFICATE_DESC = "CN=Android Debug,O=Android,C=US";

private KeyStore.PrivateKeyEntry mEntry;

public static class KeytoolException extends Exception {
/** default serial uid */
private static final long serialVersionUID = 1L;
private String mJavaHome = null;
private String mCommandLine = null;

KeytoolException(String message) {
super(message);
}

KeytoolException(String message, String javaHome, String commandLine) {
super(message);

mJavaHome = javaHome;
mCommandLine = commandLine;
}

public String getJavaHome() {
return mJavaHome;
}

public String getCommandLine() {
return mCommandLine;
}
}

/**
* Creates a provider using a keystore at the given location.
* <p/>The keystore, and a new random android debug key are created if they do not yet exist.
//Synthetic comment -- @@ -91,16 +91,16 @@
* @param output an optional {@link IKeyGenOutput} object to get the stdout and stderr
* of the keytool process call.
* @throws KeytoolException If the creation of the debug key failed.
     * @throws AndroidLocationException
*/
public DebugKeyProvider(String osKeyStorePath, String storeType, IKeyGenOutput output)
throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
UnrecoverableEntryException, IOException, KeytoolException, AndroidLocationException {

if (osKeyStorePath == null) {
osKeyStorePath = getDefaultKeyStoreOsPath();
}

if (loadKeyEntry(osKeyStorePath, storeType) == false) {
// create the store with the key
createNewStore(osKeyStorePath, storeType, output);
//Synthetic comment -- @@ -109,7 +109,7 @@

/**
* Returns the OS path to the default debug keystore.
     *
* @return The OS path to the default debug keystore.
* @throws KeytoolException
* @throws AndroidLocationException
//Synthetic comment -- @@ -134,7 +134,7 @@
if (mEntry != null) {
return mEntry.getPrivateKey();
}

return null;
}

//Synthetic comment -- @@ -150,7 +150,7 @@

return null;
}

/**
* Loads the debug key from the keystore.
* @param osKeyStorePath the OS path to the keystore.
//Synthetic comment -- @@ -172,7 +172,7 @@
} catch (FileNotFoundException e) {
return false;
}

return true;
}

//Synthetic comment -- @@ -193,9 +193,9 @@
private void createNewStore(String osKeyStorePath, String storeType, IKeyGenOutput output)
throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
UnrecoverableEntryException, IOException, KeytoolException {

if (KeystoreHelper.createNewStore(osKeyStorePath, storeType, PASSWORD_STRING, DEBUG_ALIAS,
                PASSWORD_STRING, CERTIFICATE_DESC, 30 /* validity*/, output)) {
loadKeyEntry(osKeyStorePath, storeType);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/DebugKeyProviderTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/build/DebugKeyProviderTest.java
new file mode 100755
//Synthetic comment -- index 0000000..6f6896e

//Synthetic comment -- @@ -0,0 +1,125 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class DebugKeyProviderTest extends TestCase {

    private File mTmpFile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // We want to allocate a new tmp file but not have it actually exist
        mTmpFile = File.createTempFile(this.getClass().getSimpleName(), ".keystore");
        assertTrue(mTmpFile.delete());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (mTmpFile != null) {
            if (!mTmpFile.delete()) {
                mTmpFile.deleteOnExit();
            }
            mTmpFile = null;
        }
    }

    public void testCreateAndCheckKey() throws Exception {
        String osPath = mTmpFile.getAbsolutePath();

        KeygenOutput keygenOutput = new KeygenOutput();

        // "now" is just slightly before the key was created
        long now = System.currentTimeMillis();

        DebugKeyProvider provider;
        try {
            provider = new DebugKeyProvider(osPath,  null /*storeType*/, keygenOutput);
        } catch (Throwable t) {
            // In case we get any kind of exception, rewrap it to make sure we output
            // the path used.
            String msg = String.format("%1$s in %2$s\n%3$s",
                    t.getClass().getSimpleName(), osPath, t.toString());
            throw new Exception(msg, t);
        }
        assertNotNull(provider);

        assertEquals("", keygenOutput.getOut());
        assertEquals("", keygenOutput.getErr());

        PrivateKey key = provider.getDebugKey();
        assertNotNull(key);

        X509Certificate certificate = (X509Certificate) provider.getCertificate();
        assertNotNull(certificate);

        // The "not-after" (a.k.a. expiration) date should be after "now"
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        assertTrue(certificate.getNotAfter().compareTo(c.getTime()) > 0);

        // It should be valid after 1 year from now (adjust by a second since the 'now' time
        // doesn't exactly match the creation time... 1 second should be enough.)
        c.add(Calendar.DAY_OF_YEAR, 365);
        c.add(Calendar.SECOND, -1);
        assertTrue("1 year expiration failed",
                certificate.getNotAfter().compareTo(c.getTime()) > 0);

        // and 30 years from now
        c.add(Calendar.DAY_OF_YEAR, 29 * 365);
        assertTrue("30 year expiration failed",
                certificate.getNotAfter().compareTo(c.getTime()) > 0);

        // however expiration date should be passed in 30 years + 1 hour
        c.add(Calendar.HOUR, 1);
        assertFalse("30 year and 1 hour expiration failed",
                certificate.getNotAfter().compareTo(c.getTime()) > 0);
    }

    private static class KeygenOutput implements IKeyGenOutput {
        private String mOut = "";               //$NON-NLS-1$
        private String mErr = "";               //$NON-NLS-1$

        public void out(String message) {
            mOut += message + "\n";             //$NON-NLS-1$
        }

        public void err(String message) {
            mErr += message + "\n";             //$NON-NLS-1$
        }

        public String getOut() {
            return mOut;
        }

        public String getErr() {
            return mErr;
        }
    }
}







