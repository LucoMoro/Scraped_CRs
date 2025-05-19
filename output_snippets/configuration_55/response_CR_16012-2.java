//<Beginning of snippet n. 0>
if (mContext == null) {
    Log.e(TAG, "Context is null, cannot initialize headset");
    return;
}

if (priority <= BluetoothHeadset.PRIORITY_OFF) {
    Log.i(TAG, "Rejecting incoming connection because priority = " + priority);

    if (headset != null) {
        headset.disconnect();
    }
    headset = new HeadsetBase(mContext, mPowerManager, mAdapter, info.mRemoteDevice,
            info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
    return;
}

if (info.mRemoteDevice == null) {
    Log.e(TAG, "Remote device is null, cannot proceed with connection");
    return;
}

mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
if (headset != null) {
    headset.disconnect();
}

headset = new HeadsetBase(mContext, mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
Log.i(TAG, "Already attempting connect to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);

// Improved synchronization to prevent race conditions
synchronized (this) {
    if (headset != null) {
        headset.disconnect();
    }
    mHeadset = headset; // Reusing the previously created headset instance
    mHeadsetType = type;

    setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
    Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);
}

if (device == null) {
    Log.e(TAG, "Device is null, cannot instantiate headset");
    return;
}
HeadsetBase headset = new HeadsetBase(mContext, mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);
//<End of snippet n. 0>