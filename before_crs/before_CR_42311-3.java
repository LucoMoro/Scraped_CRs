/*Add status bar icon for HAC

Display icon for HAC (Hearing Aid Compatibility) in status bar,
when HAC is turned on.

Change-Id:I426d4ec256a057e27bbfc4ec3ff45ae7822e347d*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallFeaturesSetting.java b/src/com/android/phone/CallFeaturesSetting.java
//Synthetic comment -- index ee1233d..b9938b2 100644

//Synthetic comment -- @@ -60,6 +60,7 @@

import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.cdma.TtyIntent;
//Synthetic comment -- @@ -487,6 +488,12 @@

// Update HAC Value in AudioManager
mAudioManager.setParameter(HAC_KEY, hac != 0 ? HAC_VAL_ON : HAC_VAL_OFF);
return true;
} else if (preference == mVoicemailSettings) {
if (DBG) log("onPreferenceTreeClick: Voicemail Settings Preference is clicked.");








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneGlobals.java b/src/com/android/phone/PhoneGlobals.java
//Synthetic comment -- index acfdd7e..2c6a5be 100644

//Synthetic comment -- @@ -59,6 +59,7 @@

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MmiCode;
//Synthetic comment -- @@ -634,6 +635,11 @@
audioManager.setParameter(CallFeaturesSetting.HAC_KEY, hac != 0 ?
CallFeaturesSetting.HAC_VAL_ON :
CallFeaturesSetting.HAC_VAL_OFF);
}
}








