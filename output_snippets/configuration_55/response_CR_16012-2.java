//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

if (priority <= BluetoothHeadset.PRIORITY_OFF) {
    Log.i(TAG, "Rejecting incoming connection because priority = " + priority);
    return;
}

// headset connecting us, lets join
mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);

if (headset != null) {
    headset.disconnect();
}

headset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
Log.i(TAG, "Already attempting connect to " + mRemoteDevice +
        ", disconnecting " + info.mRemoteDevice);

// Now continue with new connection, including calling callback
if (headset != null) {
    headset.disconnect();
}

mHeadset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice,
        info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " +
        info.mRemoteDevice);

long timestamp;

timestamp = System.currentTimeMillis();
headset = new HeadsetBase(mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);
if (result != SUCCESS) {
    Log.e(TAG, "Connection failed for " + mRemoteDevice);
}

//<End of snippet n. 0>