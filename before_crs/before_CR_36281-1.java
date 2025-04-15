/*Revert "Telephony: Register/Unregister Phones with CallManager"

This reverts commit cbca38522ca90322cfc9ccbb7872586807bf6c48*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index e1892ee..77f1e6c 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.gsm.GsmDataConnection;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.CallManager;

import java.util.List;

//Synthetic comment -- @@ -89,7 +88,6 @@

if(mOutgoingPhone.equals("GSM")) {
logd("Make a new CDMAPhone and destroy the old GSMPhone.");
                CallManager.getInstance().unregisterPhone(mActivePhone);

((GSMPhone)mActivePhone).dispose();
Phone oldPhone = mActivePhone;
//Synthetic comment -- @@ -102,11 +100,9 @@

mActivePhone = PhoneFactory.getCdmaPhone();
((GSMPhone)oldPhone).removeReferences();
                CallManager.getInstance().registerPhone(mActivePhone);
oldPhone = null;
} else {
logd("Make a new GSMPhone and destroy the old CDMAPhone.");
                CallManager.getInstance().unregisterPhone(mActivePhone);

((CDMAPhone)mActivePhone).dispose();
//mActivePhone = null;
//Synthetic comment -- @@ -120,7 +116,6 @@

mActivePhone = PhoneFactory.getGsmPhone();
((CDMAPhone)oldPhone).removeReferences();
                CallManager.getInstance().registerPhone(mActivePhone);
oldPhone = null;
}








