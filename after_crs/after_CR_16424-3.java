/*Event broadcasts should include the Bluetooth company ID.

Change-Id:I969bbb4d963c4f257f5d122436a55add3de02c89*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 3b3c485..1f02852 100644

//Synthetic comment -- @@ -1036,15 +1036,19 @@
}

/*
     * Put the AT command, company ID, arguments, and device in an Intent and broadcast it.
*/
private void broadcastVendorSpecificEventIntent(String command,
                                                    int companyID,
Object[] arguments,
BluetoothDevice device) {
if (VDBG) log("broadcastVendorSpecificEventIntent(" + command + ")");
Intent intent =
new Intent(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);
intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD, command);
        intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID,
                        companyID);

// assert: all elements of args are Serializable
intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS, arguments);
intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
//Synthetic comment -- @@ -1473,12 +1477,23 @@
* @param commandName the name of the command.  For example, if the expected
* incoming command is <code>AT+FOO=bar,baz</code>, the value of this should be
* <code>"+FOO"</code>.
     * @param companyID the Bluetooth SIG Company Identifier
* @param parser the AtParser on which to register the command
*/
    private void registerVendorSpecificCommand(String commandName,
                                               int companyID,
                                               AtParser parser) {
        parser.register(commandName,
                        new VendorSpecificCommandHandler(commandName, companyID));
}

    // Bluetooth SIG Company IDs for vendor-specific events

    /*
     * The Plantronics company ID.
     */
    private static final int PLANTRONICS_ID = 0x0055;

/*
* Register all vendor-specific commands here.
*/
//Synthetic comment -- @@ -1486,7 +1501,7 @@
AtParser parser = mHeadset.getAtParser();

// Plantronics-specific headset events go here
        registerVendorSpecificCommand("+XEVENT", PLANTRONICS_ID, parser);
}

/**
//Synthetic comment -- @@ -2254,13 +2269,17 @@

private String mCommandName;

        private int mCompanyID;

        private VendorSpecificCommandHandler(String commandName, int companyID) {
mCommandName = commandName;
            mCompanyID = companyID;
}

@Override
public AtCommandResult handleSetCommand(Object[] arguments) {
broadcastVendorSpecificEventIntent(mCommandName,
                                               mCompanyID,
arguments,
mHeadset.getRemoteDevice());
return new AtCommandResult(AtCommandResult.OK);







