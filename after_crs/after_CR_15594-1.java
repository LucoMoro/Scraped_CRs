/*Removed debug logs and unused code

Change-Id:I1c0a38e7f48e5cc20ffa0018f9b1a754712986f2*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 7dd4381..aa9f45d 100644

//Synthetic comment -- @@ -57,9 +57,9 @@
*/
public class BluetoothHandsfree {
private static final String TAG = "BT HS/HF";
    private static final boolean DBG = (PhoneApp.DBG_LEVEL >= 1)
            && (SystemProperties.getInt("ro.debuggable", 0) == 1);
    private static final boolean VDBG = (PhoneApp.DBG_LEVEL >= 2);  // even more logging

public static final int TYPE_UNKNOWN           = 0;
public static final int TYPE_HEADSET           = 1;
//Synthetic comment -- @@ -156,6 +156,7 @@

// VirtualCall support
// VirtualCall can be enabled/disabled via this property
    // The property is read only once when the BluetoothHandsfree is initialized
private static final String PROPERTY_VIRTUALCALL_ENABLE = "service.bt.virtualcall";
	// Default VirtualCall is enabled unless disabled otherwise via the above property
private static boolean mVirtualCallEnabled = true;
//Synthetic comment -- @@ -417,7 +418,6 @@
filter.addAction(BluetoothA2dp.ACTION_SINK_STATE_CHANGED);
mContext.registerReceiver(mStateReceiver, filter);

// reset value of VirtualCallSCo from the property
setVirtualCallStatusFromProperty();
}
//Synthetic comment -- @@ -1040,7 +1040,6 @@
((ScoSocket)msg.obj).close();
mOutgoingSco.close();
mHFScoState = HF_STATE_SCO_IDLE;
if (DBG) log("mHandler: Updated mHFScoState:"+ mHFScoState);
}
mOutgoingSco = null;
//Synthetic comment -- @@ -2383,42 +2382,6 @@
return mVirtualCallEnabled;
}

private void broadcastVirtualCallStateIntent(int state) {
	   if (VDBG) log("broadcastVirtualCallStateIntent(" + state + ")");
	   Intent intent = new Intent(BluetoothHeadset.ACTION_VIRTUALCALL_STATE_CHANGED);
//Synthetic comment -- @@ -2467,10 +2430,6 @@

mAudioPossible = true;

// Done
if (DBG) log("initiateVirtualVoiceCall: Done");
return true;
//Synthetic comment -- @@ -2513,9 +2472,6 @@
}
mAudioPossible = false;

// Done
if (DBG) log("terminateVirtualVoiceCall: Done");
return true;








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 813ef39..402dec0 100755

//Synthetic comment -- @@ -67,7 +67,7 @@
*/
public class PhoneUtils {
private static final String LOG_TAG = "PhoneUtils";
    private static final boolean DBG = (PhoneApp.DBG_LEVEL >= 2);

/** Control stack trace for Audio Mode settings */
private static final boolean DBG_SETAUDIOMODE_STACK = false;







