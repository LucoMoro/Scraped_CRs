/*AndroidKeyStore: return error code on error

Instead of blindly multiplying return value by 1000 to convert to
milliseconds, check to see if it's an error condition first.

Change-Id:I8eab1e7a86d78c13458fcbbc79d590e452fc9791*/
//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index 444dc1d..ceaff37 100644

//Synthetic comment -- @@ -243,7 +243,12 @@
*/
public long getmtime(String key) {
try {
            return mBinder.getmtime(key) * 1000L;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return -1L;







