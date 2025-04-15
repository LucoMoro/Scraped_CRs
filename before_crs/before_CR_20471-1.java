/*Telephony: Register/Unregister Phones with CallManager

Unregister the outgoing phone and register the new one when Technology change
is detected. Otherwise, CallManager keeps trying to use an already dead phone,
which causes a crash

Change-Id:I2a04c76ce882d034d1a2073d2a9f9739d4dd9a38*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77f1e6c..e1892ee 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.gsm.GsmDataConnection;
import com.android.internal.telephony.test.SimulatedRadioControl;

import java.util.List;

//Synthetic comment -- @@ -88,6 +89,7 @@

if(mOutgoingPhone.equals("GSM")) {
logd("Make a new CDMAPhone and destroy the old GSMPhone.");

((GSMPhone)mActivePhone).dispose();
Phone oldPhone = mActivePhone;
//Synthetic comment -- @@ -100,9 +102,11 @@

mActivePhone = PhoneFactory.getCdmaPhone();
((GSMPhone)oldPhone).removeReferences();
oldPhone = null;
} else {
logd("Make a new GSMPhone and destroy the old CDMAPhone.");

((CDMAPhone)mActivePhone).dispose();
//mActivePhone = null;
//Synthetic comment -- @@ -116,6 +120,7 @@

mActivePhone = PhoneFactory.getGsmPhone();
((CDMAPhone)oldPhone).removeReferences();
oldPhone = null;
}








