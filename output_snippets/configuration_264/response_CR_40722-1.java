//<Beginning of snippet n. 0>
private BluetoothClass mBtClass;
private HashMap<LocalBluetoothProfile, Integer> mProfileConnectionState;
private final List<LocalBluetoothProfile> mProfiles = new ArrayList<LocalBluetoothProfile>();

LocalBluetoothAdapter adapter,
LocalBluetoothProfileManager profileManager,
BluetoothDevice device) {
    mContext = context;
    mLocalAdapter = adapter;
    mProfileManager = profileManager;
}

private boolean ensurePaired() {
    if (getBondState() == BluetoothDevice.BOND_NONE) {
        startPairing();
        return false;
    } else {
        return true;
    }
}

void unpair() {
    if (isBusy() || getBondState() != BluetoothDevice.BOND_BONDED) {
        return;
    }
    try {
        for (LocalBluetoothProfile profile : mProfiles) {
            if (getProfileConnectionState(profile) == BluetoothProfile.STATE_CONNECTED) {
                disconnect(); // Ensures disconnection before removal
                break; // Exit after disconnection
            }
        }
        mProfiles.removeIf(profile -> getProfileConnectionState(profile) == BluetoothProfile.STATE_CONNECTED);
    } catch (Exception e) {
        // Handle the error appropriately, potentially logging it or notifying the user
    }
}

boolean isBusy() {
    for (LocalBluetoothProfile profile : mProfiles) {
        int status = getProfileConnectionState(profile);
        if (status == BluetoothProfile.STATE_CONNECTING || status == BluetoothProfile.STATE_CONNECTED) {
            return true;
        }
    }
    return false;
}

void onBondingStateChanged(int bondState) {
    synchronized (mProfiles) {
        if (bondState == BluetoothDevice.BOND_NONE) {
            mProfiles.clear();
            mConnectAfterPairing = false;
            setPhonebookPermissionChoice(PHONEBOOK_ACCESS_UNKNOWN);
        }
    }
}
//<End of snippet n. 0>