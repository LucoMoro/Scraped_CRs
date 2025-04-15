/*Telephony: Handling of supplimentary services in Call

When there are active calls, the candial() returns false
and if the user supplied an MMI code for supplementary
service it will not be accepted.

The codes for supplementary services such as hold, reflect
transfer etc are now handled with this patch, even when
dial cannot be done.

Change-Id:Ic90d71112ca9fd3250b82660a2929590491e41baAuthor: Fabien Hubert <fabien.hubert@intel.com>
Signed-off-by: Fabien Hubert <fabien.hubert@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 577*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallManager.java b/src/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index 1c1799f..c1cbfc8 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.os.Message;
import android.os.RegistrantList;
import android.os.Registrant;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.Rlog;
//Synthetic comment -- @@ -749,7 +750,17 @@
}

if (!canDial(phone)) {
            /*
             * canDial function only checks whether the phone can make a new call.
             * InCall MMI commmands are basically supplementary services
             * within a call eg: call hold, call deflection, explicit call transfer etc.
             */
            String newDialString = PhoneNumberUtils.stripSeparators(dialString);
            if (basePhone.handleInCallMmiCommands(newDialString)) {
                return null;
            } else {
                throw new CallStateException("cannot dial in current state");
            }
}

if ( hasActiveFgCall() ) {







