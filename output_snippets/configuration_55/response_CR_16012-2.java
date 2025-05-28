//<Beginning of snippet n. 0>
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

// If we are here, we are in danger of a race condition
synchronized (this) {
    if (headset != null) {
        headset.disconnect();
    }
    mHeadset = new HeadsetBase(mContext, mPowerManager, mAdapter, mRemoteDevice,
            info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
    mHeadsetType = type;

    setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
    Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);
}

long timestamp = System.currentTimeMillis();
if (device == null) {
    Log.e(TAG, "Device is null, cannot instantiate headset");
    return;
}
HeadsetBase headset = new HeadsetBase(mContext, mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);
//<End of snippet n. 0>