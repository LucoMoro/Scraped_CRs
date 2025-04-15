/*Move DebugKeyProvider into the Builder library.

Change-Id:I815dc7caff9056af4a7462bf976ddfd7824ef772*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/packaging/Packager.java b/builder/src/main/java/com/android/builder/internal/packaging/Packager.java
//Synthetic comment -- index 5860f20..06ddb1e 100644

//Synthetic comment -- @@ -19,13 +19,13 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.builder.internal.packaging.JavaResourceProcessor.IArchiveBuilder;
import com.android.builder.internal.signing.DebugKeyProvider;
import com.android.builder.internal.signing.SignedJarBuilder;
import com.android.builder.internal.signing.SignedJarBuilder.IZipEntryFilter;
import com.android.builder.internal.signing.SigningInfo;
import com.android.builder.packaging.DuplicateFileException;
import com.android.builder.packaging.PackagerException;
import com.android.builder.packaging.SealedPackageException;
import com.android.utils.ILogger;

import java.io.File;








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/signing/DebugKeyProvider.java b/builder/src/main/java/com/android/builder/internal/signing/DebugKeyProvider.java
new file mode 100644
//Synthetic comment -- index 0000000..8b232dc

//Synthetic comment -- @@ -0,0 +1,173 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.builder.internal.signing;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.utils.ILogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * A provider of a dummy key to sign Android application for debugging purpose.
 * <p/>This provider uses a custom keystore to create and store a key with a known password.
 */
public class DebugKeyProvider {

    private static final String PASSWORD_STRING = "android";
    private static final char[] PASSWORD_CHAR = PASSWORD_STRING.toCharArray();
    private static final String DEBUG_ALIAS = "AndroidDebugKey";

    // Certificate CN value. This is a hard-coded value for the debug key.
    // Android Market checks against this value in order to refuse applications signed with
    // debug keys.
    private static final String CERTIFICATE_DESC = "CN=Android Debug,O=Android,C=US";

    private KeyStore.PrivateKeyEntry mEntry;

    /**
     * Creates a provider using a keystore at the given location.
     * <p/>The keystore, and a new random android debug key are created if they do not yet exist.
     * <p/>Password for the store/key is <code>android</code>, and the key alias is
     * <code>AndroidDebugKey</code>.
     * @param osKeyStorePath the OS path to the keystore, or <code>null</code> if the default one
     * is to be used.
     * @param storeType an optional keystore type, or <code>null</code> if the default is to
     * be used.
     * @param logger the logger
     * @throws KeytoolException If the creation of the debug key failed.
     * @throws AndroidLocationException
     */
    public DebugKeyProvider(String osKeyStorePath, String storeType, ILogger logger)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableEntryException, IOException, KeytoolException, AndroidLocationException {

        if (osKeyStorePath == null) {
            osKeyStorePath = getDefaultKeyStoreOsPath();
        }

        if (!loadKeyEntry(osKeyStorePath, storeType)) {
            // create the store with the key
            createNewStore(osKeyStorePath, storeType, logger);
        }
    }

    /**
     * Returns the OS path to the default debug keystore.
     *
     * @return The OS path to the default debug keystore.
     * @throws AndroidLocationException
     */
    public static String getDefaultKeyStoreOsPath() throws AndroidLocationException {
        return AndroidLocation.getFolder() + "debug.keystore";
    }

    /**
     * Returns the debug {@link PrivateKey} to use to sign applications for debug purpose.
     * @return the private key or <code>null</code> if its creation failed.
     */
    @SuppressWarnings("unused") // the thrown Exceptions are not actually thrown
    public PrivateKey getDebugKey() throws KeyStoreException, NoSuchAlgorithmException,
            UnrecoverableKeyException, UnrecoverableEntryException {
        if (mEntry != null) {
            return mEntry.getPrivateKey();
        }

        return null;
    }

    /**
     * Returns the debug {@link Certificate} to use to sign applications for debug purpose.
     * @return the certificate or <code>null</code> if its creation failed.
     */
    @SuppressWarnings("unused") // the thrown Exceptions are not actually thrown
    public Certificate getCertificate() throws KeyStoreException, NoSuchAlgorithmException,
            UnrecoverableKeyException, UnrecoverableEntryException {
        if (mEntry != null) {
            return mEntry.getCertificate();
        }

        return null;
    }

    /**
     * Loads the debug key from the keystore.
     * @param osKeyStorePath the OS path to the keystore.
     * @param storeType an optional keystore type, or <code>null</code> if the default is to
     * be used.
     * @return <code>true</code> if success, <code>false</code> if the keystore does not exist.
     */
    private boolean loadKeyEntry(String osKeyStorePath, String storeType) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException,
            UnrecoverableEntryException {
        FileInputStream fis = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(
                    storeType != null ? storeType : KeyStore.getDefaultType());
            fis = new FileInputStream(osKeyStorePath);
            keyStore.load(fis, PASSWORD_CHAR);
            mEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(
                    DEBUG_ALIAS, new KeyStore.PasswordProtection(PASSWORD_CHAR));
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // pass
                }
            }
        }

        return true;
    }

    /**
     * Creates a new store
     * @param osKeyStorePath the location of the store
     * @param storeType an optional keystore type, or <code>null</code> if the default is to
     * be used.
     * @param logger an optional {@link ILogger} object to get the output of the keytool
     *               process call.
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws UnrecoverableEntryException
     * @throws IOException
     * @throws KeytoolException
     */
    private void createNewStore(String osKeyStorePath, String storeType, ILogger logger)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableEntryException, IOException, KeytoolException {

        if (KeystoreHelper.createNewStore(osKeyStorePath, storeType, PASSWORD_STRING, DEBUG_ALIAS,
                PASSWORD_STRING, CERTIFICATE_DESC, 30 /* validity*/, logger)) {
            loadKeyEntry(osKeyStorePath, storeType);
        }
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/signing/KeystoreHelper.java b/builder/src/main/java/com/android/builder/internal/signing/KeystoreHelper.java
//Synthetic comment -- index ad687b0..3c9b040 100644

//Synthetic comment -- @@ -112,18 +112,14 @@
@Override
public void out(@Nullable String line) {
if (line != null) {
                                logger.info(line);
}
}

@Override
public void err(@Nullable String line) {
if (line != null) {
                                logger.error(null /*throwable*/, line);
}
}
});








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/internal/signing/DebugKeyProviderTest.java b/builder/src/test/java/com/android/builder/internal/signing/DebugKeyProviderTest.java
new file mode 100755
//Synthetic comment -- index 0000000..5d85a2c

//Synthetic comment -- @@ -0,0 +1,143 @@
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

package com.android.builder.internal.signing;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.utils.ILogger;
import junit.framework.TestCase;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;

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

        FakeLogger fakeLogger = new FakeLogger();

        // "now" is just slightly before the key was created
        long now = System.currentTimeMillis();

        DebugKeyProvider provider;
        try {
            provider = new DebugKeyProvider(osPath,  null /*storeType*/, fakeLogger);
        } catch (Throwable t) {
            // In case we get any kind of exception, rewrap it to make sure we output
            // the path used.
            String msg = String.format("%1$s in %2$s\n%3$s",
                    t.getClass().getSimpleName(), osPath, t.toString());
            throw new Exception(msg, t);
        }
        assertNotNull(provider);

        assertEquals("", fakeLogger.getOut());
        assertEquals("", fakeLogger.getErr());

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

    private static class FakeLogger implements ILogger {
        private String mOut = "";
        private String mErr = "";

        public String getOut() {
            return mOut;
        }

        public String getErr() {
            return mErr;
        }

        @Override
        public void error(@Nullable Throwable t, @Nullable String msgFormat, Object... args) {
            String message = msgFormat != null ?
                    String.format(msgFormat, args) :
                    t != null ? t.getClass().getCanonicalName() : "ERROR!";
            mErr += message + "\n";
        }

        @Override
        public void warning(@NonNull String msgFormat, Object... args) {
            String message = String.format(msgFormat, args);
            mOut += message + "\n";
        }

        @Override
        public void info(@NonNull String msgFormat, Object... args) {
            String message = String.format(msgFormat, args);
            mOut += message + "\n";
        }

        @Override
        public void verbose(@NonNull String msgFormat, Object... args) {
            String message = String.format(msgFormat, args);
            mOut += message + "\n";
        }
    }
}








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/NoteUtilsTest.java b/builder/src/test/java/com/android/builder/resources/NodeUtilsTest.java
similarity index 98%
rename from builder/src/test/java/com/android/builder/resources/NoteUtilsTest.java
rename to builder/src/test/java/com/android/builder/resources/NodeUtilsTest.java
//Synthetic comment -- index 1bcd083..70d25e7 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NodeUtilsTest extends TestCase {

public void testBasicAttributes() throws Exception {
Document document = createDocument();







