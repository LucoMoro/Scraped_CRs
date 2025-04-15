/*code cleanup : unused import statement, local vars and static finals.

Change-Id:I49b96ce37385989fb2208cecbf4cddcdd0e0d240*/




//Synthetic comment -- diff --git a/src/com/android/certinstaller/CertFile.java b/src/com/android/certinstaller/CertFile.java
//Synthetic comment -- index 8e63794..fb7e13e 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.certinstaller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
//Synthetic comment -- @@ -27,7 +26,6 @@

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;








//Synthetic comment -- diff --git a/src/com/android/certinstaller/CertFileList.java b/src/com/android/certinstaller/CertFileList.java
//Synthetic comment -- index bd7767f..601bee4 100644

//Synthetic comment -- @@ -37,8 +37,6 @@
private static final String TAG = "CertFileList";

private static final String DOWNLOAD_DIR = "download";

private SdCardMonitor mSdCardMonitor;









//Synthetic comment -- diff --git a/src/com/android/certinstaller/CredentialHelper.java b/src/com/android/certinstaller/CredentialHelper.java
//Synthetic comment -- index 15f31ce..eeb2bdb 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
//Synthetic comment -- @@ -48,7 +47,6 @@
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
* A helper class for accessing the raw data in the intent extra and handling








//Synthetic comment -- diff --git a/src/com/android/certinstaller/Util.java b/src/com/android/certinstaller/Util.java
//Synthetic comment -- index 61b4a19..00c14c4 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
//Synthetic comment -- @@ -39,9 +38,8 @@
os.close();
} catch (Exception e) {
Log.w(TAG, "toBytes(): " + e + ": " + object);
}
        return baos.toByteArray();
}

static <T> T fromBytes(byte[] bytes) {







