/*Phone: enhancement on Use2GOnlyCheckBox

We think there is some enhancement on Phone/Use2GOnlyCheckBoxPreference.java
we may refer to Phone/Settings.java as the similar solution.

1. handleGetPreferredNetworkTypeResponse
Only if NetworkMode of Settings.Secure is not equal to current, putInt curre
NetworkMode to Settings.Secure.

2. handleSetPreferredNetworkTypeResponse
after confirmation from modem, putInt the NetworkMode to Settings.Secure.

3. onClick
Don't need putInt Settings.Secure here.

Change-Id:I905251d4607133da899d9f743d204a41efcee581Author: Xiao Jin <jin.xiao@intel.com>
Signed-off-by: Xiao Jin <jin.xiao@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 39591*/
//Synthetic comment -- diff --git a/src/com/android/phone/Use2GOnlyCheckBoxPreference.java b/src/com/android/phone/Use2GOnlyCheckBoxPreference.java
//Synthetic comment -- index 4da238b..9192523 100644

//Synthetic comment -- @@ -28,6 +28,7 @@

public class Use2GOnlyCheckBoxPreference extends CheckBoxPreference {
private static final String LOG_TAG = "Use2GOnlyCheckBoxPreference";

private Phone mPhone;
private MyHandler mHandler;
//Synthetic comment -- @@ -82,15 +83,33 @@
AsyncResult ar = (AsyncResult) msg.obj;

if (ar.exception == null) {
                int type = ((int[])ar.result)[0];
                if (type != Phone.NT_MODE_GSM_ONLY) {
// Allow only NT_MODE_GSM_ONLY or NT_MODE_WCDMA_PREF
                    type = Phone.NT_MODE_WCDMA_PREF;
}
                Log.i(LOG_TAG, "get preferred network type="+type);
                setChecked(type == Phone.NT_MODE_GSM_ONLY);
                android.provider.Settings.Global.putInt(mPhone.getContext().getContentResolver(),
                        android.provider.Settings.Global.PREFERRED_NETWORK_MODE, type);
} else {
// Weird state, disable the setting
Log.i(LOG_TAG, "get preferred network type, exception="+ar.exception);
//Synthetic comment -- @@ -109,6 +128,10 @@
mPhone.getPreferredNetworkType(obtainMessage(MESSAGE_GET_PREFERRED_NETWORK_TYPE));
} else {
Log.i(LOG_TAG, "set preferred network type done");
}
}
}







