//<Beginning of snippet n. 0>

if (priority <= BluetoothHeadset.PRIORITY_OFF) {
    Log.i(TAG, "Rejecting incoming connection because priority = " + priority);
    headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
            info.mSocketFd, info.mRfcommChan, null);
    headset.disconnect();
    return;
}

// headset connecting us, lets join
mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
headset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
Log.i(TAG, "Already attempting connect to " + mRemoteDevice +
        ", disconnecting " + info.mRemoteDevice);

headset.disconnect();

// If we are here, we are in danger of a race condition
}

// Now continue with new connection, including calling callback
mHeadset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice,
        info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " +
        info.mRemoteDevice);

headset.disconnect();
break;

long timestamp;
timestamp = System.currentTimeMillis();
HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter, device, channel);
int result = waitForConnect(headset);

//<End of snippet n. 0>