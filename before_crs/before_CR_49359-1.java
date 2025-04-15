/*Telephony: Restrict setMute based on call/mute state

Currently, setMute is triggered upon call state changes and
so setMute issued when there is no active callresulting in
error.

Restrict the setMute based on the current mute state,
requested mute state and the current call state.

Change-Id:I5fb5f87548abb6da118be37246580ec997ff297fAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 14806*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 2080976..4dd9115 100644

//Synthetic comment -- @@ -719,8 +719,10 @@

/*package*/ void
setMute(boolean mute) {
        desiredMute = mute;
        cm.setMute(desiredMute, null);
}

/*package*/ boolean







