/*Moved parsing and broadcasting code to BluetoothHandsfree.java from
HeadsetBase.java.  Adjusted HeadsetBase constructor calls to remove Context.

Change-Id:I969533bfe652fbfe0a07486547c181c3ae9816b5*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..4ed35e1 100644

//Synthetic comment -- @@ -1031,8 +1031,24 @@
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

    /*
     * Put the AT command, arguments, and device in an Intent and broadcast it.
     */
    private void broadcastVendorSpecificEventIntent(String command,
                                                    Object[] arguments,
                                                    BluetoothDevice device) {
        if (VDBG) log("broadcastVendorSpecificEventIntent(" + command + ")");
        Intent intent =
                new Intent(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);
        intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD, command);
        // assert: all elements of args are Serializable
        intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS, arguments);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
    }

void updateBtHandsfreeAfterRadioTechnologyChange() {
        if (VDBG) Log.d(TAG, "updateBtHandsfreeAfterRadioTechnologyChange...");

//Get the Call references from the new active phone again
mRingingCall = mPhone.getRingingCall();
//Synthetic comment -- @@ -1448,13 +1464,33 @@
return result;
}

    /*
     * Register a vendor-specific command.
     * @param commandName the name of the command.  For example, if the expected
     * incoming command is <code>AT+FOO=bar,baz</code>, the value of this should be
     * <code>"+FOO"</code>.
     * @param parser the AtParser on which to register the command
     */
    private void registerVendorSpecificCommand(String commandName, AtParser parser) {
        parser.register(commandName, new VendorSpecificCommandHandler(commandName));
    }

    /*
     * Register all vendor-specific commands here.
     */
    private void registerAllVendorSpecificCommands(AtParser parser) {

        // Plantronics-specific headset events go here
        registerVendorSpecificCommand("+XEVENT", parser);
    }

/**
* Register AT Command handlers to implement the Headset profile
*/
private void initializeHeadsetAtParser() {
if (VDBG) log("Registering Headset AT commands");
AtParser parser = mHeadset.getAtParser();
        // Headsets usually only have one button, which is meant to cause the
// HS to send us AT+CKPD=200 or AT+CKPD.
parser.register("+CKPD", new AtCommandHandler() {
private AtCommandResult headsetButtonPress() {
//Synthetic comment -- @@ -1499,6 +1535,9 @@
return headsetButtonPress();
}
});

        // Headset vendor-specific commands
        registerAllVendorSpecificCommands(parser); 
}

/**
//Synthetic comment -- @@ -2131,6 +2170,10 @@
return new AtCommandResult("+CPAS: " + status);
}
});

        // Headset vendor-specific commands
        registerAllVendorSpecificCommands(parser); 

mPhonebook.register(parser);
}

//Synthetic comment -- @@ -2205,6 +2248,26 @@
return true;
}

    /*
     * This class broadcasts vendor-specific commands + arguments to interested receivers.
     */
    private class VendorSpecificCommandHandler extends AtCommandHandler {

        private String mCommandName;

        private VendorSpecificCommandHandler(String commandName) {
            mCommandName = commandName;
        }

        @Override
        public AtCommandResult handleSetCommand(Object[] arguments) {
            broadcastVendorSpecificEventIntent(mCommandName,
                                               arguments,
                                               mHeadset.getRemoteDevice());
            return new AtCommandResult(AtCommandResult.OK);
        }
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
                                          null);
headset.disconnect();
return;
}
//Synthetic comment -- @@ -172,8 +172,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
mRemoteDevice, info.mSocketFd,
info.mRfcommChan,
                                          mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
//Synthetic comment -- @@ -188,8 +187,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
info.mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                              null);
headset.disconnect();
}
// If we are here, we are in danger of a race condition
//Synthetic comment -- @@ -207,8 +205,7 @@
mHeadset = new HeadsetBase(mPowerManager, mAdapter,
mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                           mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
//Synthetic comment -- @@ -223,7 +220,7 @@
headset = new HeadsetBase(mPowerManager, mAdapter,
info.mRemoteDevice,
info.mSocketFd, info.mRfcommChan,
                                          null);
headset.disconnect();
break;
}
//Synthetic comment -- @@ -370,8 +367,7 @@

timestamp = System.currentTimeMillis();
HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter,
                                                  device, channel);

int result = waitForConnect(headset);








