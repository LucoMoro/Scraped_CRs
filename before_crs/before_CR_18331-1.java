/*Telephony: Ignore intents received on inactive phone object.

Change-Id:I3f3624ffd922261a22c591f49104419afa118473*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..f385a51 100644

//Synthetic comment -- @@ -169,6 +169,12 @@
private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
// update emergency string whenever locale changed
updateSpnDisplay();







