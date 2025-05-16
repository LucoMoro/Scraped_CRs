//<Beginning of snippet n. 0>

if (priority <= BluetoothHeadset.PRIORITY_OFF) {
    Log.i(TAG, "Rejecting incoming connection because priority = " + priority);

    headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
            info.mSocketFd, info.mRfcommChan, null);
    if (headset != null && headset.isConnected()) {
        headset.disconnect();
    }
    return;
}

mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
headset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
Log.i(TAG, "Already attempting connect to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);

if (headset != null && headset.isConnected()) {
    headset.disconnect();
}

// If we are here, we are in danger of a race condition
mHeadset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice,
        info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);

if (headset != null && headset.isConnected()) {
    headset.disconnect();
}

long timestamp = System.currentTimeMillis();
HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter, device, channel);
int result = waitForConnect(headset);


//<End of snippet n. 0>