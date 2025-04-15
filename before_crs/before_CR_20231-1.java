/*Phone: SIM security support for DSDS

Handle PUK MMI code for the subscription
that is PUK-Locked.

Change-Id:Iaba46dc5b292c79196695f75f3d5f0a7e4bb4b61*/
//Synthetic comment -- diff --git a/src/com/android/phone/SpecialCharSequenceMgr.java b/src/com/android/phone/SpecialCharSequenceMgr.java
//Synthetic comment -- index 727f674..fc281f2 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.WindowManager;

/**
* Helper class to listen for some magic character sequences
//Synthetic comment -- @@ -181,7 +182,32 @@
if ((input.startsWith("**04") || input.startsWith("**05"))
&& input.endsWith("#")) {
PhoneApp app = PhoneApp.getInstance();
            boolean isMMIHandled = app.phone.handlePinMmi(input);

// if the PUK code is recognized then indicate to the
// phone app that an attempt to unPUK the device was







