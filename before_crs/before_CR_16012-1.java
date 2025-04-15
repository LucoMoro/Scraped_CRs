/*Adjusted calls to the HeadsetBase constructor to pass in the current
Context.

Change-Id:I938b13e10e1cacecf0d8a5f41f41e88a77df9175*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 513caad..abf9468

//Synthetic comment -- @@ -157,8 +157,10 @@
if (priority <= BluetoothHeadset.PRIORITY_OFF) {
Log.i(TAG, "Rejecting incoming connection because priority = " + priority);

                headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
                        info.mSocketFd, info.mRfcommChan, null);
headset.disconnect();
return;
}
//Synthetic comment -- @@ -167,8 +169,11 @@
// headset connecting us, lets join
mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
                headset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
                        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
//Synthetic comment -- @@ -180,8 +185,11 @@
Log.i(TAG, "Already attempting connect to " + mRemoteDevice +
", disconnecting " + info.mRemoteDevice);

                    headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
                            info.mSocketFd, info.mRfcommChan, null);
headset.disconnect();
}
// If we are here, we are in danger of a race condition
//Synthetic comment -- @@ -196,8 +204,11 @@
}

// Now continue with new connection, including calling callback
                mHeadset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice,
                        info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
//Synthetic comment -- @@ -209,8 +220,10 @@
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " +
info.mRemoteDevice);

                headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
                        info.mSocketFd, info.mRfcommChan, null);
headset.disconnect();
break;
}
//Synthetic comment -- @@ -356,7 +369,9 @@
long timestamp;

timestamp = System.currentTimeMillis();
            HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);








