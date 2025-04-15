/*Phone: Fix the headset plug/unplug detection

Previously, the PhoneApp detect the headset plug/unplug state just
by the value of intent state, it may cause error if the headset plug
is a kind of headset/headphone two in one plug. As the definition
from Google, we need to detect it according to the value of both
"state" and "microphone" element of the intent

Change-Id:Ic1a045599e26413c1996d5b6bccf88bd9d8e8967Author: Leo Yan <leo.yan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 40239*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index 10099f8..37b1346 100644

//Synthetic comment -- @@ -110,6 +110,9 @@
private static final int EVENT_TTY_MODE_SET = 16;
private static final int EVENT_START_SIP_SERVICE = 17;

// The MMI codes are also used by the InCallScreen.
public static final int MMI_INITIATE = 51;
public static final int MMI_COMPLETE = 52;
//Synthetic comment -- @@ -245,6 +248,10 @@

private UpdateLock mUpdateLock;

// Broadcast receiver for various intent broadcasts (see onCreate())
private final BroadcastReceiver mReceiver = new PhoneAppBroadcastReceiver();

//Synthetic comment -- @@ -1636,7 +1643,20 @@
if (VDBG) Log.d(LOG_TAG, "mReceiver: ACTION_HEADSET_PLUG");
if (VDBG) Log.d(LOG_TAG, "    state: " + intent.getIntExtra("state", 0));
if (VDBG) Log.d(LOG_TAG, "    name: " + intent.getStringExtra("name"));
                mIsHeadsetPlugged = (intent.getIntExtra("state", 0) == 1);
mHandler.sendMessage(mHandler.obtainMessage(EVENT_WIRED_HEADSET_PLUG, 0));
} else if ((action.equals(TelephonyIntents.ACTION_SIM_STATE_CHANGED)) &&
(mPUKEntryActivity != null)) {







