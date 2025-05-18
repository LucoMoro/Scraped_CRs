//<Beginning of snippet n. 0>

if (priority <= BluetoothHeadset.PRIORITY_OFF) {
    Log.i(TAG, "Rejecting incoming connection because priority = " + priority);
    return;
}

mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);

if (headset != null) {
    synchronized (headset) {
        headset.disconnect();
    }
}

headset = new HeadsetBase(context, mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
Log.i(TAG, "Already attempting connect to " + mRemoteDevice +
        ", disconnecting " + info.mRemoteDevice);

if (headset != null) {
    synchronized (headset) {
        headset.disconnect();
    }
}

mHeadset = new HeadsetBase(context, mPowerManager, mAdapter, mRemoteDevice,
        info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " +
        info.mRemoteDevice);

long timestamp;

timestamp = System.currentTimeMillis();
headset = new HeadsetBase(context, mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);
if (result != SUCCESS) {
    Log.e(TAG, "Connection failed for " + mRemoteDevice);
    // Add retry mechanism or user notification here
}

//<End of snippet n. 0>