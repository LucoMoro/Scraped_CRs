
//<Beginning of snippet n. 0>


private BluetoothClass mBtClass;
private HashMap<LocalBluetoothProfile, Integer> mProfileConnectionState;

private final List<LocalBluetoothProfile> mProfiles =
new ArrayList<LocalBluetoothProfile>();

LocalBluetoothAdapter adapter,
LocalBluetoothProfileManager profileManager,
BluetoothDevice device) {
mContext = context;
mLocalAdapter = adapter;
mProfileManager = profileManager;

private boolean ensurePaired() {
if (getBondState() == BluetoothDevice.BOND_NONE) {
startPairing();
return false;
} else {
}

void unpair() {
disconnect();

int state = getBondState();
}

boolean isBusy() {
for (LocalBluetoothProfile profile : mProfiles) {
int status = getProfileConnectionState(profile);
if (status == BluetoothProfile.STATE_CONNECTING

void onBondingStateChanged(int bondState) {
if (bondState == BluetoothDevice.BOND_NONE) {
mProfiles.clear();
mConnectAfterPairing = false;  // cancel auto-connect
setPhonebookPermissionChoice(PHONEBOOK_ACCESS_UNKNOWN);

//<End of snippet n. 0>








