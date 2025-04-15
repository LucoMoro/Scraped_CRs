/*Contacts: SIM Security support for DSDS.

Handle PIN/PUK MMI codes based on the MMI subscription
selected by user.

Change-Id:I7a3f802753b514eed0d38daf7b2f7a4fdba56d51*/




//Synthetic comment -- diff --git a/src/com/android/contacts/SpecialCharSequenceMgr.java b/src/com/android/contacts/SpecialCharSequenceMgr.java
//Synthetic comment -- index 140e7d4..5bb07ab 100644

//Synthetic comment -- @@ -171,8 +171,10 @@
static boolean handlePinEntry(Context context, String input) {
if ((input.startsWith("**04") || input.startsWith("**05")) && input.endsWith("#")) {
try {
                // Use Voice Subscription for both change PIN & unblock PIN using PUK.
                int subscription = TelephonyManager.getPreferredVoiceSubscription();
return ITelephony.Stub.asInterface(ServiceManager.getService("phone"))
                        .handlePinMmiOnSubscription(input, subscription);
} catch (RemoteException e) {
Log.e(TAG, "Failed to handlePinMmi due to remote exception");
return false;







