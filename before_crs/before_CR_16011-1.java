/*Change HeadsetBase to broadcast AT+XEVENT messages from the headset to
interested observers.

- core/java/android/bluetooth/AtCommandHandler.java:
- core/java/android/bluetooth/BluetoothHeadset.java:

Fixed minor typos in comments.

- core/java/android/bluetooth/BluetoothDevice.java:

Added Broadcast Intent Action ACTION_HEADSET_XEVENT.  This action is
used to notify interested observers that the current Bluetooth headset
has sent an "AT+XEVENT=x,y,z,..." command to the phone.  The arguments
x,y,z,... are sent as an ArrayList<String> in the EXTRA_XEVENT_ARGS
extra, which is also new.  The broadcast action also includes the headset's
BluetoothDevice object in the EXTRA_DEVICE extra.

- core/java/android/bluetooth/EventAtCommandHandler.java:

(New file.) Handles the "set" form of the AT+XEVENT command -- that
is, "AT+XEVENT=x,y,z,..." -- by broadcasting the action
BluetoothDevice.ACTION_HEADSET_XEVENT with extras
BluetoothDevice.EXTRA_XEVENT_ARGS (new) and
BluetoothDevice.EXTRA_DEVICE.

- core/java/android/bluetooth/HeadsetBase.java:

Changed the HeadsetBase constructor to accept the Context in which it
is running.  AT command handlers (such as EventAtCommandHandler)
invoked by the HeadsetBase may need the Context in order to broadcast
events, etc.

Also fixed a minor typo in a Log message.
Removed comment about removing the AtParser call.
Register EventAtCommandHandler as responding to the AT+XEVENT command.

Change-Id:I2cbd73d6bd95aa54d03b1b2fab10faf15e9432e1*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/AtCommandHandler.java b/core/java/android/bluetooth/AtCommandHandler.java
old mode 100644
new mode 100755
//Synthetic comment -- index 8de2133..6deab34

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








//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothDevice.java b/core/java/android/bluetooth/BluetoothDevice.java
old mode 100644
new mode 100755
//Synthetic comment -- index e77e76f..e91657a

//Synthetic comment -- @@ -167,6 +167,18 @@
"android.bluetooth.device.action.BOND_STATE_CHANGED";

/**
* Used as a Parcelable {@link BluetoothDevice} extra field in every intent
* broadcast by this class. It contains the {@link BluetoothDevice} that
* the intent applies to.
//Synthetic comment -- @@ -193,6 +205,13 @@
public static final String EXTRA_CLASS = "android.bluetooth.device.extra.CLASS";

/**
* Used as an int extra field in {@link #ACTION_BOND_STATE_CHANGED} intents.
* Contains the bond state of the remote device.
* <p>Possible values are:








//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothHeadset.java b/core/java/android/bluetooth/BluetoothHeadset.java
old mode 100644
new mode 100755
//Synthetic comment -- index 95e61b6..5b2de22

//Synthetic comment -- @@ -45,7 +45,7 @@
* This BluetoothHeadset object is not immediately bound to the
* BluetoothHeadset service. Use the ServiceListener interface to obtain a
* notification when it is bound, this is especially important if you wish to
 * immediately call methods on BluetootHeadset after construction.
*
* Android only supports one connected Bluetooth Headset at a time.
*








//Synthetic comment -- diff --git a/core/java/android/bluetooth/EventAtCommandHandler.java b/core/java/android/bluetooth/EventAtCommandHandler.java
new file mode 100755
//Synthetic comment -- index 0000000..0fa62b8

//Synthetic comment -- @@ -0,0 +1,76 @@








//Synthetic comment -- diff --git a/core/java/android/bluetooth/HeadsetBase.java b/core/java/android/bluetooth/HeadsetBase.java
old mode 100644
new mode 100755
//Synthetic comment -- index e2935c9..a63d5bc

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.bluetooth;

import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
//Synthetic comment -- @@ -42,6 +44,8 @@

private static int sAtInputCount = 0;  /* TODO: Consider not using a static variable */

private final BluetoothAdapter mAdapter;
private final BluetoothDevice mRemoteDevice;
private final String mAddress;  // for native code
//Synthetic comment -- @@ -74,14 +78,16 @@

private native void cleanupNativeDataNative();

    public HeadsetBase(PowerManager pm, BluetoothAdapter adapter, BluetoothDevice device,
            int rfcommChannel) {
mDirection = DIRECTION_OUTGOING;
mConnectTimestamp = System.currentTimeMillis();
mAdapter = adapter;
mRemoteDevice = device;
mAddress = device.getAddress();
mRfcommChannel = rfcommChannel;
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HeadsetBase");
mWakeLock.setReferenceCounted(false);
initializeAtParser();
//Synthetic comment -- @@ -89,9 +95,11 @@
initializeNativeDataNative(-1);
}

    /* Create from an already exisiting rfcomm connection */
    public HeadsetBase(PowerManager pm, BluetoothAdapter adapter, BluetoothDevice device,
            int socketFd, int rfcommChannel, Handler handler) {
mDirection = DIRECTION_INCOMING;
mConnectTimestamp = System.currentTimeMillis();
mAdapter = adapter;
//Synthetic comment -- @@ -99,6 +107,7 @@
mAddress = device.getAddress();
mRfcommChannel = rfcommChannel;
mEventThreadHandler = handler;
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HeadsetBase");
mWakeLock.setReferenceCounted(false);
initializeAtParser();
//Synthetic comment -- @@ -128,7 +137,7 @@
(System.currentTimeMillis() - timestamp) + " ms");

if (result.getResultCode() == AtCommandResult.ERROR) {
            Log.i(TAG, "Error pocessing <" + input + ">");
}

sendURC(result.toString());
//Synthetic comment -- @@ -142,8 +151,10 @@
*/
protected void initializeAtParser() {
mAtParser = new AtParser();
        //TODO(): Get rid of this as there are no parsers registered. But because of dependencies,
        //it needs to be done as part of refactoring HeadsetBase and BluetoothHandsfree
}

public AtParser getAtParser() {
//Synthetic comment -- @@ -159,8 +170,7 @@
String input = readNative(500);
if (input != null) {
handleInput(input);
                        }
                        else {
last_read_error = getLastReadStatusNative();
if (last_read_error != 0) {
Log.i(TAG, "headset read error " + last_read_error);
//Synthetic comment -- @@ -179,8 +189,6 @@
mEventThread.start();
}



private native String readNative(int timeout_ms);
private native int getLastReadStatusNative();








