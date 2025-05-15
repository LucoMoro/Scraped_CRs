
//<Beginning of snippet n. 0>


private BluetoothClass mBtClass;
private HashMap<LocalBluetoothProfile, Integer> mProfileConnectionState;

    private boolean mUnBonding = false;

private final List<LocalBluetoothProfile> mProfiles =
new ArrayList<LocalBluetoothProfile>();

LocalBluetoothAdapter adapter,
LocalBluetoothProfileManager profileManager,
BluetoothDevice device) {
        mUnBonding = false;
mContext = context;
mLocalAdapter = adapter;
mProfileManager = profileManager;

private boolean ensurePaired() {
if (getBondState() == BluetoothDevice.BOND_NONE) {
            mUnBonding = false;
startPairing();
return false;
} else {
}

void unpair() {
        mUnBonding = true;

disconnect();

int state = getBondState();
}

boolean isBusy() {
        if(mUnBonding)
            return true;

for (LocalBluetoothProfile profile : mProfiles) {
int status = getProfileConnectionState(profile);
if (status == BluetoothProfile.STATE_CONNECTING

void onBondingStateChanged(int bondState) {
if (bondState == BluetoothDevice.BOND_NONE) {
            mUnBonding = false;
mProfiles.clear();
mConnectAfterPairing = false;  // cancel auto-connect
setPhonebookPermissionChoice(PHONEBOOK_ACCESS_UNKNOWN);

//<End of snippet n. 0>








