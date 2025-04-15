/*Stop using KeyStore.state()

Change-Id:Iae821ae96ae76b14f10625f93e8011225d85e35e*/
//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index 3635c4d..9000668 100644

//Synthetic comment -- @@ -74,6 +74,10 @@
}
}

public byte[] get(String key) {
try {
return mBinder.get(key);








//Synthetic comment -- diff --git a/keystore/tests/src/android/security/AndroidKeyPairGeneratorTest.java b/keystore/tests/src/android/security/AndroidKeyPairGeneratorTest.java
//Synthetic comment -- index d108caaa..1a237f5 100644

//Synthetic comment -- @@ -62,11 +62,7 @@

assertTrue(mAndroidKeyStore.reset());

        assertEquals(android.security.KeyStore.State.UNINITIALIZED, mAndroidKeyStore.state());

        assertTrue(mAndroidKeyStore.password("1111"));

        assertEquals(android.security.KeyStore.State.UNLOCKED, mAndroidKeyStore.state());

assertEquals(0, mAndroidKeyStore.saw("").length);









//Synthetic comment -- diff --git a/keystore/tests/src/android/security/AndroidKeyStoreTest.java b/keystore/tests/src/android/security/AndroidKeyStoreTest.java
//Synthetic comment -- index c376f3d..63cae8c 100644

//Synthetic comment -- @@ -468,11 +468,9 @@

assertTrue(mAndroidKeyStore.reset());

        assertEquals(android.security.KeyStore.State.UNINITIALIZED, mAndroidKeyStore.state());

assertTrue(mAndroidKeyStore.password("1111"));

        assertEquals(android.security.KeyStore.State.UNLOCKED, mAndroidKeyStore.state());

assertEquals(0, mAndroidKeyStore.saw("").length);









//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index cccaf1c..e5cfdf6 100644

//Synthetic comment -- @@ -3384,7 +3384,7 @@
// Tear down existing lockdown if profile was removed
mLockdownEnabled = LockdownVpnTracker.isEnabled();
if (mLockdownEnabled) {
            if (mKeyStore.state() != KeyStore.State.UNLOCKED) {
Slog.w(TAG, "KeyStore locked; unable to create LockdownTracker");
return false;
}








//Synthetic comment -- diff --git a/services/java/com/android/server/connectivity/Vpn.java b/services/java/com/android/server/connectivity/Vpn.java
//Synthetic comment -- index bb19cc7..bb7334a 100644

//Synthetic comment -- @@ -462,7 +462,7 @@
* secondary thread to perform connection work, returning quickly.
*/
public void startLegacyVpn(VpnProfile profile, KeyStore keyStore, LinkProperties egress) {
        if (keyStore.state() != KeyStore.State.UNLOCKED) {
throw new IllegalStateException("KeyStore isn't unlocked");
}








