//<Beginning of snippet n. 0>

if (priority <= BluetoothHeadset.PRIORITY_OFF) {
    Log.i(TAG, "Rejecting incoming connection because priority = " + priority);
    if (info.mRemoteDevice != null && info.mSocketFd >= 0 && info.mRfcommChan >= 0) {
        disconnectHeadset(info);
    }
    return;
}

mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
if (mRemoteDevice != null && info.mSocketFd >= 0 && info.mRfcommChan >= 0) {
    headset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
    mHeadsetType = type;
}

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
Log.i(TAG, "Already attempting connect to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);
disconnectHeadset(info);

mHeadset = headset;
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " + info.mRemoteDevice);
disconnectHeadset(info);

long timestamp = System.currentTimeMillis();
HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);

private void disconnectHeadset(ConnectionInfo info) {
    if (info.mRemoteDevice != null && info.mSocketFd >= 0 && info.mRfcommChan >= 0) {
        headset.disconnect();
    }
}

//<End of snippet n. 0>