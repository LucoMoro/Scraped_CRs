/*BT: fixes crash caused by unpair/pair in a short time

Disabling the device in the paired list to avoid
having the possibility to reconnect the device
during the unpairing process

Change-Id:I2fa87f921df576926379d931860c7b1ced051472Author: Christophe Bransiec <christophex.bransiec@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 43678*/




//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/CachedBluetoothDevice.java b/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
//Synthetic comment -- index 01fd1b2..e82bfff 100644

//Synthetic comment -- @@ -51,6 +51,8 @@
private BluetoothClass mBtClass;
private HashMap<LocalBluetoothProfile, Integer> mProfileConnectionState;

    private boolean mUnBonding = false;

private final List<LocalBluetoothProfile> mProfiles =
new ArrayList<LocalBluetoothProfile>();

//Synthetic comment -- @@ -144,6 +146,7 @@
LocalBluetoothAdapter adapter,
LocalBluetoothProfileManager profileManager,
BluetoothDevice device) {
        mUnBonding = false;
mContext = context;
mLocalAdapter = adapter;
mProfileManager = profileManager;
//Synthetic comment -- @@ -253,6 +256,7 @@

private boolean ensurePaired() {
if (getBondState() == BluetoothDevice.BOND_NONE) {
            mUnBonding = false;
startPairing();
return false;
} else {
//Synthetic comment -- @@ -283,6 +287,8 @@
}

void unpair() {
        mUnBonding = true;

disconnect();

int state = getBondState();
//Synthetic comment -- @@ -412,6 +418,9 @@
}

boolean isBusy() {
        if(mUnBonding)
            return true;

for (LocalBluetoothProfile profile : mProfiles) {
int status = getProfileConnectionState(profile);
if (status == BluetoothProfile.STATE_CONNECTING
//Synthetic comment -- @@ -485,6 +494,7 @@

void onBondingStateChanged(int bondState) {
if (bondState == BluetoothDevice.BOND_NONE) {
            mUnBonding = false;
mProfiles.clear();
mConnectAfterPairing = false;  // cancel auto-connect
setPhonebookPermissionChoice(PHONEBOOK_ACCESS_UNKNOWN);







