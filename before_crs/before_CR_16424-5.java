/*BluetoothHandsfree.java:
Added mechanism to broadcast vendor-specific headset events.

BluetoothHeadsetService.java:
Reformatted code.

Change-Id:I6c613d185f0ab96f9f040f54e8b8d7de9df4735bEvent broadcasts should include the Bluetooth company ID.

Change-Id:I969bbb4d963c4f257f5d122436a55add3de02c89*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..5078c69 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import android.bluetooth.AtCommandResult;
import android.bluetooth.AtParser;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
//Synthetic comment -- @@ -249,6 +250,10 @@
} else {
initializeHandsfreeAtParser();
}
headset.startEventThread();
configAudioParameters();

//Synthetic comment -- @@ -1031,8 +1036,28 @@
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

void updateBtHandsfreeAfterRadioTechnologyChange() {
        if(VDBG) Log.d(TAG, "updateBtHandsfreeAfterRadioTechnologyChange...");

//Get the Call references from the new active phone again
mRingingCall = mPhone.getRingingCall();
//Synthetic comment -- @@ -1448,13 +1473,40 @@
return result;
}

/**
* Register AT Command handlers to implement the Headset profile
*/
private void initializeHeadsetAtParser() {
if (VDBG) log("Registering Headset AT commands");
AtParser parser = mHeadset.getAtParser();
        // Headset's usually only have one button, which is meant to cause the
// HS to send us AT+CKPD=200 or AT+CKPD.
parser.register("+CKPD", new AtCommandHandler() {
private AtCommandResult headsetButtonPress() {
//Synthetic comment -- @@ -2131,6 +2183,7 @@
return new AtCommandResult("+CPAS: " + status);
}
});
mPhonebook.register(parser);
}

//Synthetic comment -- @@ -2205,6 +2258,30 @@
return true;
}

private boolean inDebug() {
return DBG && SystemProperties.getBoolean(DebugThread.DEBUG_HANDSFREE, false);
}








//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 513caad..94ab632

//Synthetic comment -- @@ -157,8 +157,10 @@
if (priority <= BluetoothHeadset.PRIORITY_OFF) {
Log.i(TAG, "Rejecting incoming connection because priority = " + priority);

                headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
                        info.mSocketFd, info.mRfcommChan, null);
headset.disconnect();
return;
}
//Synthetic comment -- @@ -167,8 +169,10 @@
// headset connecting us, lets join
mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
                headset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice, info.mSocketFd,
                        info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
//Synthetic comment -- @@ -180,8 +184,10 @@
Log.i(TAG, "Already attempting connect to " + mRemoteDevice +
", disconnecting " + info.mRemoteDevice);

                    headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
                            info.mSocketFd, info.mRfcommChan, null);
headset.disconnect();
}
// If we are here, we are in danger of a race condition
//Synthetic comment -- @@ -196,8 +202,10 @@
}

// Now continue with new connection, including calling callback
                mHeadset = new HeadsetBase(mPowerManager, mAdapter, mRemoteDevice,
                        info.mSocketFd, info.mRfcommChan, mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
//Synthetic comment -- @@ -209,8 +217,10 @@
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " +
info.mRemoteDevice);

                headset = new HeadsetBase(mPowerManager, mAdapter, info.mRemoteDevice,
                        info.mSocketFd, info.mRfcommChan, null);
headset.disconnect();
break;
}
//Synthetic comment -- @@ -356,7 +366,8 @@
long timestamp;

timestamp = System.currentTimeMillis();
            HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter, device, channel);

int result = waitForConnect(headset);








