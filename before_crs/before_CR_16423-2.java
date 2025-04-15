/*AtCommandHandler.java:
Fixed comment typos.

BluetoothHeadset.java:
Added ACTION_VENDOR_SPECIFIC_HEADSET_EVENT,
EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD,  EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS.

HeadsetBase.java:
Reformatted some code.

Change-Id:I34d6f248166305d72be66632779fc963b894379c*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/AtCommandHandler.java b/core/java/android/bluetooth/AtCommandHandler.java
//Synthetic comment -- index 8de2133..6deab34 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
*             least one element in this array.
* @return     The result of this command.
*/
    // Typically used to set this paramter
public AtCommandResult handleSetCommand(Object[] args) {
return new AtCommandResult(AtCommandResult.ERROR);
}
//Synthetic comment -- @@ -83,11 +83,12 @@
* Test commands are part of the Extended command syntax, and are typically
* used to request an indication of the range of legal values that "FOO"
* can take.<p>
     * By defualt we return an OK result, to indicate that this command is at
* least recognized.<p>
* @return The result of this command.
*/
public AtCommandResult handleTestCommand() {
return new AtCommandResult(AtCommandResult.OK);
}
}








//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothHeadset.java b/core/java/android/bluetooth/BluetoothHeadset.java
//Synthetic comment -- index 95e61b6..da9c93a 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
* This BluetoothHeadset object is not immediately bound to the
* BluetoothHeadset service. Use the ServiceListener interface to obtain a
* notification when it is bound, this is especially important if you wish to
 * immediately call methods on BluetootHeadset after construction.
*
* Android only supports one connected Bluetooth Headset at a time.
*
//Synthetic comment -- @@ -85,6 +85,33 @@
"android.bluetooth.headset.extra.DISCONNECT_INITIATOR";

/**
* TODO(API release): Consider incorporating as new state in
* HEADSET_STATE_CHANGED
*/








//Synthetic comment -- diff --git a/core/java/android/bluetooth/HeadsetBase.java b/core/java/android/bluetooth/HeadsetBase.java
//Synthetic comment -- index e2935c9..9ef2eb5 100644

//Synthetic comment -- @@ -74,8 +74,8 @@

private native void cleanupNativeDataNative();

    public HeadsetBase(PowerManager pm, BluetoothAdapter adapter, BluetoothDevice device,
            int rfcommChannel) {
mDirection = DIRECTION_OUTGOING;
mConnectTimestamp = System.currentTimeMillis();
mAdapter = adapter;
//Synthetic comment -- @@ -89,9 +89,10 @@
initializeNativeDataNative(-1);
}

    /* Create from an already exisiting rfcomm connection */
    public HeadsetBase(PowerManager pm, BluetoothAdapter adapter, BluetoothDevice device,
            int socketFd, int rfcommChannel, Handler handler) {
mDirection = DIRECTION_INCOMING;
mConnectTimestamp = System.currentTimeMillis();
mAdapter = adapter;
//Synthetic comment -- @@ -128,7 +129,7 @@
(System.currentTimeMillis() - timestamp) + " ms");

if (result.getResultCode() == AtCommandResult.ERROR) {
            Log.i(TAG, "Error pocessing <" + input + ">");
}

sendURC(result.toString());
//Synthetic comment -- @@ -142,8 +143,9 @@
*/
protected void initializeAtParser() {
mAtParser = new AtParser();
        //TODO(): Get rid of this as there are no parsers registered. But because of dependencies,
        //it needs to be done as part of refactoring HeadsetBase and BluetoothHandsfree
}

public AtParser getAtParser() {
//Synthetic comment -- @@ -159,8 +161,7 @@
String input = readNative(500);
if (input != null) {
handleInput(input);
                        }
                        else {
last_read_error = getLastReadStatusNative();
if (last_read_error != 0) {
Log.i(TAG, "headset read error " + last_read_error);
//Synthetic comment -- @@ -179,8 +180,6 @@
mEventThread.start();
}



private native String readNative(int timeout_ms);
private native int getLastReadStatusNative();








