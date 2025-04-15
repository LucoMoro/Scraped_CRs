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
import android.telephony.TelephonyManager;

/**
* Helper class to listen for some magic character sequences
//Synthetic comment -- @@ -181,7 +182,32 @@
if ((input.startsWith("**04") || input.startsWith("**05"))
&& input.endsWith("#")) {
PhoneApp app = PhoneApp.getInstance();
            Phone phone = null;
            boolean isPukRequired = false;
            if (input.startsWith("**05")) {
                // Called when user tries to unblock PIN by entering the MMI code
                // through emergency dialer app. Send the request on the right
                // sub which is in PUK_REQUIRED state. Use the default subscription
                // when none of the subscriptions are PUK-Locked. This may be
                // a change PIN request.
                int numPhones = TelephonyManager.getPhoneCount();
                for (int i = 0; i < numPhones; i++) {
                    if (app.isSimPukLocked(i)) {
                        phone = app.getPhone(i);
                        log("Sending PUK on subscription :" + phone.getSubscription());
                        break;
                    }
                }
                if (phone == null) {
                    log("No Subscription is PUK-Locked..Using default phone");
                    phone = app.phone;
                }
            } else {
                // Change Pin request (**04). Use voice phone.
                int voiceSub = app.getVoiceSubscription();
                phone = app.getPhone(voiceSub);
            }
            boolean isMMIHandled = phone.handlePinMmi(input);

// if the PUK code is recognized then indicate to the
// phone app that an attempt to unPUK the device was







