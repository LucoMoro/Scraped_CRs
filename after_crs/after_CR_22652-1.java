/*Fix worldphone mode switch crashes*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 6f08868..0b60fd6 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.telephony.SignalStrength;
import android.util.Log;

import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.telephony.gsm.NetworkInfo;
//Synthetic comment -- @@ -86,6 +87,10 @@
}
}

            CallManager cMgr = CallManager.getInstance();
            if(cMgr != null)
                cMgr.unregisterPhone(mActivePhone);

if(mOutgoingPhone.equals("GSM")) {
logd("Make a new CDMAPhone and destroy the old GSMPhone.");

//Synthetic comment -- @@ -132,6 +137,9 @@
mPhoneSubInfoProxy.setmPhoneSubInfo(this.mActivePhone.getPhoneSubInfo());
mCommandsInterface = ((PhoneBase)mActivePhone).mCM;

            if(cMgr != null)
                cMgr.registerPhone(mActivePhone);

//Send an Intent to the PhoneApp that we had a radio technology change
Intent intent = new Intent(TelephonyIntents.ACTION_RADIO_TECHNOLOGY_CHANGED);
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);







