/*Removed debug logs and unused code

Change-Id:I1c0a38e7f48e5cc20ffa0018f9b1a754712986f2*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 7dd4381..aa9f45d 100644

//Synthetic comment -- @@ -57,9 +57,9 @@
*/
public class BluetoothHandsfree {
private static final String TAG = "BT HS/HF";
    private static final boolean DBG = true; //(PhoneApp.DBG_LEVEL >= 1)
            //&& (SystemProperties.getInt("ro.debuggable", 0) == 1);
    private static final boolean VDBG = true; //(PhoneApp.DBG_LEVEL >= 2);  // even more logging

public static final int TYPE_UNKNOWN           = 0;
public static final int TYPE_HEADSET           = 1;
//Synthetic comment -- @@ -156,6 +156,7 @@

// VirtualCall support
// VirtualCall can be enabled/disabled via this property
private static final String PROPERTY_VIRTUALCALL_ENABLE = "service.bt.virtualcall";
	// Default VirtualCall is enabled unless disabled otherwise via the above property
private static boolean mVirtualCallEnabled = true;
//Synthetic comment -- @@ -417,7 +418,6 @@
filter.addAction(BluetoothA2dp.ACTION_SINK_STATE_CHANGED);
mContext.registerReceiver(mStateReceiver, filter);

            // Intent Filter action for VirtualCall SCO
// reset value of VirtualCallSCo from the property
setVirtualCallStatusFromProperty();
}
//Synthetic comment -- @@ -1040,7 +1040,6 @@
((ScoSocket)msg.obj).close();
mOutgoingSco.close();
mHFScoState = HF_STATE_SCO_IDLE;
                        // TODO: VC_AOSP Take a look at this            
if (DBG) log("mHandler: Updated mHFScoState:"+ mHFScoState);
}
mOutgoingSco = null;
//Synthetic comment -- @@ -2383,42 +2382,6 @@
return mVirtualCallEnabled;
}

/* VC_AOSP
      private void updateAndBroadcastHFScoState(int state, int reason) {
        mHFScoState = state;
        broadcastHFScoStateIntent(reason);
    }
    private void broadcastHFScoStateIntent(int reason) {
	  if (isVirtualCallScoEnabled() == false) return;
      if (VDBG) log("broadcastHFScoStateIntent(" + mHFScoState + ")");
      String action;
      Intent intent;	  
	  action = BluetoothHeadset.VIRTUALCALL_STATE_CHANGED;
	  intent = new Intent(action);
      switch (mHFScoState) {
        case HF_STATE_SCO_IDLE:
			intent.putExtra(BluetoothHeadset.VIRTUALCALL_STATE,
				            BluetoothHeadset.VIRTUALCALL_STATE_DISCONNECTED);
			break;
        case HF_STATE_SCO_CALL_TRANSFERRED:
        case HF_STATE_SCO_VIRTUALCALL_TRANSFERRED:
            intent.putExtra(ScoIntent.SCO_CLOSED_REASON, reason);
            break;
        case HF_STATE_SCO_CALL_ACTIVE:
            action = ScoIntent.SCO_CALL_ACTIVE;
            intent = new Intent(action);
            break;
        case HF_STATE_SCO_VIRTUALCALL_ACTIVE:
            action = ScoIntent.SCO_VIRTUALCALL_ACTIVE;
            intent = new Intent(action);
            break;                  
        default:
            return;
      }
      mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
      if (DBG) Log.d(TAG, "broadcasHFScoStateIntent: Sent intent:" + action);     
    }
 */
private void broadcastVirtualCallStateIntent(int state) {
	   if (VDBG) log("broadcastVirtualCallStateIntent(" + state + ")");
	   Intent intent = new Intent(BluetoothHeadset.ACTION_VIRTUALCALL_STATE_CHANGED);
//Synthetic comment -- @@ -2467,10 +2430,6 @@

mAudioPossible = true;

        //VC_AOSP This is done from ScoConnected mHandler
        // 5. Broadcast the intent  
        //broadcastHFScoStateIntent(0);

// Done
if (DBG) log("initiateVirtualVoiceCall: Done");
return true;
//Synthetic comment -- @@ -2513,9 +2472,6 @@
}
mAudioPossible = false;

        // 6. Broadcast the intent
        // VC_AOSP This is done from audioOff
        //broadcastHFScoStateIntent(ScoIntent.SCO_CLOSED_REASON_CALL_ENDED);
// Done
if (DBG) log("terminateVirtualVoiceCall: Done");
return true;








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 813ef39..402dec0 100755

//Synthetic comment -- @@ -67,7 +67,7 @@
*/
public class PhoneUtils {
private static final String LOG_TAG = "PhoneUtils";
    private static final boolean DBG = true; //(PhoneApp.DBG_LEVEL >= 2);

/** Control stack trace for Audio Mode settings */
private static final boolean DBG_SETAUDIOMODE_STACK = false;







