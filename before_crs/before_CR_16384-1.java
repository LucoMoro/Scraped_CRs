/*Moved parsing and broadcasting code to BluetoothHandsfree.java from
HeadsetBase.java.  Adjusted HeadsetBase constructor calls to remove Context.

Change-Id:I969533bfe652fbfe0a07486547c181c3ae9816b5*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..4ed35e1 100644

//Synthetic comment -- @@ -1031,8 +1031,24 @@
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

void updateBtHandsfreeAfterRadioTechnologyChange() {
        if(VDBG) Log.d(TAG, "updateBtHandsfreeAfterRadioTechnologyChange...");

//Get the Call references from the new active phone again
mRingingCall = mPhone.getRingingCall();
//Synthetic comment -- @@ -1448,13 +1464,33 @@
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
//Synthetic comment -- @@ -1499,6 +1535,9 @@
return headsetButtonPress();
}
});
}

/**
//Synthetic comment -- @@ -2131,6 +2170,10 @@
return new AtCommandResult("+CPAS: " + status);
}
});
mPhonebook.register(parser);
}

//Synthetic comment -- @@ -2205,6 +2248,26 @@
return true;
}

private boolean inDebug() {
return DBG && SystemProperties.getBoolean(DebugThread.DEBUG_HANDSFREE, false);
}








//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
//Synthetic comment -- index 54c91ab..94ab632 100755

//Synthetic comment -- @@ -160,7 +160,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
info.mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                          null, BluetoothHeadsetService.this);
headset.disconnect();
return;
}
//Synthetic comment -- @@ -172,8 +172,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
mRemoteDevice, info.mSocketFd,
info.mRfcommChan,
                                          mConnectedStatusHandler,
                                          BluetoothHeadsetService.this);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
//Synthetic comment -- @@ -188,8 +187,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
info.mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                              null, 
                                              BluetoothHeadsetService.this);
headset.disconnect();
}
// If we are here, we are in danger of a race condition
//Synthetic comment -- @@ -207,8 +205,7 @@
mHeadset = new HeadsetBase(mPowerManager, mAdapter,
mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                           mConnectedStatusHandler,
                                           BluetoothHeadsetService.this);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
//Synthetic comment -- @@ -223,7 +220,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
info.mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                          null,  BluetoothHeadsetService.this);
headset.disconnect();
break;
}
//Synthetic comment -- @@ -370,8 +367,7 @@

timestamp = System.currentTimeMillis();
HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter,
                                                  device, channel,
                                                  BluetoothHeadsetService.this);

int result = waitForConnect(headset);








