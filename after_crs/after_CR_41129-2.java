/*Telephony: Ignore SipPhone

UiccController's code isn't ready to handle having multiple
different PhoneBase objects working at the same time

Bug: 6975696
Change-Id:I8f83174cc7db478743445c09fb8f03c66cf1e41e*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index 211dad6..53a222e 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.internal.telephony.IccCardStatus;
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.sip.SipPhone;

import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -132,6 +133,12 @@
}

if (DBG) log("setNewPhone");
        // TODO: remove this
        if (phone instanceof SipPhone) {
            if (DBG) log("Got SipPhone. Ignore.");
            return;
        }

if (mCurrentPhone != phone) {
if (mIccCard != null) {
// Refresh card if phone changed







