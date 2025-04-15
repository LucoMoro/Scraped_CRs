/*Remove obsolete stuff.

Change-Id:Ib3a4f7c0c479b4c8c7f2e27d47ba756969d4f0b4*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 3b2afc8..c4e71b3 100644

//Synthetic comment -- @@ -24,17 +24,16 @@
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.build.MakeIdentity;
import com.android.sdklib.internal.project.ProjectCreator;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
//Synthetic comment -- @@ -45,8 +44,8 @@
import com.android.sdkuilib.internal.repository.SdkUpdaterNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.AvdManagerWindow;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;
import com.android.utils.ILogger;
import com.android.utils.Pair;
//Synthetic comment -- @@ -276,9 +275,6 @@
} else if (SdkCommandLine.OBJECT_LIB_PROJECT.equals(directObject)) {
createProject(true /*library*/);

            } else if (SdkCommandLine.OBJECT_IDENTITY.equals(directObject)) {
                createIdentity();

}
} else if (SdkCommandLine.VERB_UPDATE.equals(verb)) {
if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
//Synthetic comment -- @@ -1307,50 +1303,6 @@
}


    private void createIdentity() {
        try {
            String account = (String) mSdkCommandLine.getValue(
                    SdkCommandLine.VERB_CREATE,
                    SdkCommandLine.OBJECT_IDENTITY,
                    SdkCommandLine.KEY_ACCOUNT);

            String keystorePath = (String) mSdkCommandLine.getValue(
                    SdkCommandLine.VERB_CREATE,
                    SdkCommandLine.OBJECT_IDENTITY,
                    SdkCommandLine.KEY_KEYSTORE);

            String aliasName = (String) mSdkCommandLine.getValue(
                    SdkCommandLine.VERB_CREATE,
                    SdkCommandLine.OBJECT_IDENTITY,
                    SdkCommandLine.KEY_ALIAS);

            String keystorePass = (String) mSdkCommandLine.getValue(
                    SdkCommandLine.VERB_CREATE,
                    SdkCommandLine.OBJECT_IDENTITY,
                    SdkCommandLine.KEY_STOREPASS);

            if (keystorePass == null) {
                keystorePass = promptPassword("Keystore Password:  ").trim();
            }

            String aliasPass = (String) mSdkCommandLine.getValue(
                    SdkCommandLine.VERB_CREATE,
                    SdkCommandLine.OBJECT_IDENTITY,
                    SdkCommandLine.KEY_KEYPASS);

            if (aliasPass == null) {
                aliasPass = promptPassword("Alias Password:  ").trim();
            }

            MakeIdentity mi = new MakeIdentity(account, keystorePath, keystorePass,
                    aliasName, aliasPass);

            mi.make(System.out, mSdkLog);
        } catch (Exception e) {
            errorAndExit("Unexpected error: %s", e.getMessage());
        }
    }


/**
* Prompts the user to setup a hardware config for a Platform-based AVD.








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index a487881..68b1943 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
public static final String OBJECT_TEST_PROJECT   = "test-project";              //$NON-NLS-1$
public static final String OBJECT_LIB_PROJECT    = "lib-project";               //$NON-NLS-1$
public static final String OBJECT_ADB            = "adb";                       //$NON-NLS-1$
    public static final String OBJECT_IDENTITY       = "identity";                  //$NON-NLS-1$

public static final String ARG_ALIAS        = "alias";                          //$NON-NLS-1$
public static final String ARG_ACTIVITY     = "activity";                       //$NON-NLS-1$
//Synthetic comment -- @@ -156,9 +155,6 @@

{ VERB_UPDATE, OBJECT_SDK,
"Updates the SDK by suggesting new platforms to install if available." },

            { VERB_CREATE, OBJECT_IDENTITY,
                "Creates an identity file." },
};

public SdkCommandLine(ILogger logger) {
//Synthetic comment -- @@ -419,23 +415,6 @@
VERB_UPDATE, OBJECT_LIB_PROJECT, "t", KEY_TARGET_ID,                //$NON-NLS-1$
"Target ID to set for the project.", null);

        // --- create identity file ---

        define(Mode.STRING, true,
                VERB_CREATE, OBJECT_IDENTITY, "a", KEY_ACCOUNT,                      //$NON-NLS-1$
                "The publisher account.", null);
        define(Mode.STRING, true,
                VERB_CREATE, OBJECT_IDENTITY, "s", KEY_KEYSTORE,                     //$NON-NLS-1$
                "The keystore path.", null);
        define(Mode.STRING, true,
                VERB_CREATE, OBJECT_IDENTITY, "k", KEY_ALIAS,                        //$NON-NLS-1$
                "The key alias.", null);
        define(Mode.STRING, false,
                VERB_CREATE, OBJECT_IDENTITY, "p", KEY_STOREPASS,                    //$NON-NLS-1$
                "The keystore password. Default is to prompt.", null);
        define(Mode.STRING, false,
                VERB_CREATE, OBJECT_IDENTITY, "w", KEY_KEYPASS,                      //$NON-NLS-1$
                "The alias password. Default is to prompt.", null);
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/MakeIdentity.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/MakeIdentity.java
deleted file mode 100644
//Synthetic comment -- index 955a81c..0000000

//Synthetic comment -- @@ -1,105 +0,0 @@
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

import com.android.appauth.Certificate;
import com.android.utils.ILogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.CertificateException;

public class MakeIdentity {

    private final String mAccount;
    private final String mKeystorePath;
    private final String mKeystorePass;
    private final String mAliasName;
    private final String mAliasPass;

    /**
     * Create a {@link MakeIdentity} object.
     * @param account the google account
     * @param keystorePath the path to the keystore
     * @param keystorePass the password of the keystore
     * @param aliasName the key alias name
     * @param aliasPass the key alias password
     */
    public MakeIdentity(String account, String keystorePath, String keystorePass,
            String aliasName, String aliasPass) {
        mAccount = account;
        mKeystorePath = keystorePath;
        mKeystorePass = keystorePass;
        mAliasName = aliasName;
        mAliasPass = aliasPass;
    }

    /**
     * Write the identity file to the given {@link PrintStream} object.
     * @param ps the printstream object to write the identity file to.
     * @return true if success.
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableEntryException
     */
    public boolean make(PrintStream ps, ILogger log)
            throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException, UnrecoverableEntryException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = new FileInputStream(mKeystorePath);
        keyStore.load(fis, mKeystorePass.toCharArray());
        fis.close();
        PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(
                mAliasName, new KeyStore.PasswordProtection(mAliasPass.toCharArray()));

        if (entry == null) {
            return false;
        }

        Certificate c = new Certificate();
        c.setVersion(Certificate.VERSION);
        c.setType(Certificate.TYPE_IDENTITY);
        c.setHashAlgo(Certificate.DIGEST_TYPE);
        c.setPublicKey(entry.getCertificate().getPublicKey());
        c.setEntityName(mAccount);
        c.signWith(c, entry.getPrivateKey());

        /* sanity check */
        if (!c.isSignedBy(c)) {
            System.err.println("signature failed?!");
            return false;
        }

        /* write to the printstream object */
        try {
            c.writeTo(ps);
        } catch (Exception e) {
            log.error(e, null);
        }

        return true;
    }
}







