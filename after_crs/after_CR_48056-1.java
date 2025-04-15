/*Telephony: remove setCurrentPreferredNetworkType done on SIM ready

Upon SIM ready, RIL_REQUEST_SET_PREFERRED_NETWORK_TYPE
is sent to make sure the network type is set for modem
which does not restore the type. setPreferredNetworkType
is already issued upon RIL_UNSOL_RIL_CONNECTED message.
Doing it again in SIM ready is redundant.

Also setting preferred network type on SIM ready,
will result in emergency call being dropped if the
user unlocks the phone when the emergency call is connected.

This patch removes the setCurrentPreferredNetworkType done
on SIM ready.

Change-Id:I33a79d85096fcaae9e58e5c165bac9ed2db7bc8cAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 45846*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bd13374..8f7e5e6 100644

//Synthetic comment -- @@ -284,8 +284,6 @@
break;

case EVENT_SIM_READY:

boolean skipRestoringSelection = phone.getContext().getResources().getBoolean(
com.android.internal.R.bool.skip_restoring_network_selection);







