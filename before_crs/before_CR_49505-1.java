/*Telephony: Add emergency call status intent

There are system services which needs to know the emergency
call status before taking any drastic actions.

With this patch, android.intent.action.EMERGENCY_CALL_STATUS
will be broadcasted when an emergency call is initiated and
terminated.

Note: This patch is related to a change in platform/packages/apps/Phone
		and platform/frameworks/opt/telephony

Change-Id:I2f3d27831cb44cddc2241f1c29a61440d3caa194Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 55480*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b930348..ba09683 100644

//Synthetic comment -- @@ -126,6 +126,7 @@
private String mImeiSv;
private String mVmNumber;


// Constructors

//Synthetic comment -- @@ -727,6 +728,16 @@
}
}

public boolean handlePinMmi(String dialString) {
GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this, mUiccApplication.get());









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 2080976..05f58c8 100644

//Synthetic comment -- @@ -16,12 +16,17 @@

package com.android.internal.telephony.gsm;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -37,6 +42,7 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.gsm.CallFailCause;
//Synthetic comment -- @@ -383,6 +389,60 @@
}
}

private void
updatePhoneState() {
PhoneConstants.State oldState = state;
//Synthetic comment -- @@ -399,9 +459,11 @@
if (state == PhoneConstants.State.IDLE && oldState != state) {
voiceCallEndedRegistrants.notifyRegistrants(
new AsyncResult(null, null, null));
} else if (oldState == PhoneConstants.State.IDLE && oldState != state) {
voiceCallStartedRegistrants.notifyRegistrants (
new AsyncResult(null, null, null));
}

if (state != oldState) {







