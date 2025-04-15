/*Revert "Optional provisioning"

This reverts commit 0180eded20c119b0e3d4647e72edab953d31f274.*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 33a3ece..2673220 100644

//Synthetic comment -- @@ -184,14 +184,8 @@
PhoneApp app = PhoneApp.getInstance();

// Incoming calls are totally ignored if the device isn't provisioned yet
        boolean provisioned = Settings.Secure.getInt(mPhone.getContext().getContentResolver(),
            Settings.Secure.DEVICE_PROVISIONED, 0) != 0;
if (!provisioned) {
Log.i(TAG, "CallNotifier: rejecting incoming call because device isn't provisioned");
// Send the caller straight to voicemail, just like







