/*Send correct screen state when radio available.

Change-Id:I098ffd06916d9d37272ee3c8c57a4d7e21e564c6Signed-off-by: Bin Li <libin@marvell.com>*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..dbe7690 100644

//Synthetic comment -- @@ -2054,9 +2054,8 @@
// In case screen state was lost (due to process crash),
// this ensures that the RIL knows the correct screen state.

        PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        sendScreenState(pm.isScreenOn());
}

private RadioState getRadioStateFromInt(int stateInt) {







