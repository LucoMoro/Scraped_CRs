/*KeyStore: stop using state()

Change-Id:I22daeb0f9873854af789a74ac3c7da2a32e34475*/
//Synthetic comment -- diff --git a/src/com/android/certinstaller/CertInstaller.java b/src/com/android/certinstaller/CertInstaller.java
//Synthetic comment -- index c953416..a95dcac 100644

//Synthetic comment -- @@ -132,7 +132,7 @@

private boolean needsKeyStoreAccess() {
return ((mCredentials.hasKeyPair() || mCredentials.hasUserCertificate())
                && (mKeyStore.state() != KeyStore.State.UNLOCKED));
}

@Override







