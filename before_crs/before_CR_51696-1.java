/*KeyStore: stop using state()

Change-Id:Ib9a9af88a280a5442989a8199218a7ba82ce9e25*/
//Synthetic comment -- diff --git a/src/com/android/keychain/KeyChainActivity.java b/src/com/android/keychain/KeyChainActivity.java
//Synthetic comment -- index 1811fbf..e7b161b 100644

//Synthetic comment -- @@ -74,12 +74,6 @@
// be done on the UI thread.
private KeyStore mKeyStore = KeyStore.getInstance();

    // the KeyStore.state operation is safe to do on the UI thread, it
    // does not do a file operation.
    private boolean isKeyStoreUnlocked() {
        return mKeyStore.state() == KeyStore.State.UNLOCKED;
    }

@Override public void onCreate(Bundle savedState) {
super.onCreate(savedState);
if (savedState == null) {
//Synthetic comment -- @@ -114,7 +108,7 @@
// see if KeyStore has been unlocked, if not start activity to do so
switch (mState) {
case INITIAL:
                if (!isKeyStoreUnlocked()) {
mState = State.UNLOCK_REQUESTED;
this.startActivityForResult(new Intent(Credentials.UNLOCK_ACTION),
REQUEST_UNLOCK);
//Synthetic comment -- @@ -362,7 +356,7 @@
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
switch (requestCode) {
case REQUEST_UNLOCK:
                if (isKeyStoreUnlocked()) {
showCertChooserDialog();
} else {
// user must have canceled unlock, give up








//Synthetic comment -- diff --git a/src/com/android/keychain/KeyChainService.java b/src/com/android/keychain/KeyChainService.java
//Synthetic comment -- index 1c41957..a2b44e8 100644

//Synthetic comment -- @@ -111,7 +111,7 @@
if (alias == null) {
throw new NullPointerException("alias == null");
}
            if (!isKeyStoreUnlocked()) {
throw new IllegalStateException("keystore is "
+ mKeyStore.state().toString());
}
//Synthetic comment -- @@ -123,10 +123,6 @@
}
}

        private boolean isKeyStoreUnlocked() {
            return (mKeyStore.state() == KeyStore.State.UNLOCKED);
        }

@Override public void installCaCertificate(byte[] caCertificate) {
checkCertInstallerOrSystemCaller();
try {







