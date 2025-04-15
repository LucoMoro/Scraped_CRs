/*Telephony: Don't start RilReceiver when not needed

Basebandless targets don't need RilReceiver.

Change-Id:I510cc6f0100219af9479738fc4b48c7ab85ea666CRs-Fixed: 367918*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..68b4a1c 100644

//Synthetic comment -- @@ -640,6 +640,8 @@
Context.CONNECTIVITY_SERVICE);
if (cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE) == false) {
riljLog("Not starting RILReceiver: wifi-only");
} else {
riljLog("Starting RILReceiver");
mReceiver = new RILReceiver();







