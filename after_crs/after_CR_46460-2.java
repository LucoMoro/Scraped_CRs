/*Add status bar icon for HAC

Display icon for HAC (Hearing Aid Compatibility) in status bar,
when HAC is turned on.

Change-Id:Idfe217b496c22d0ee662aad54001747b6f3f1463*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallFeaturesSetting.java b/src/com/android/phone/CallFeaturesSetting.java
//Synthetic comment -- index ffed7b3..2b17f92 100644

//Synthetic comment -- @@ -61,6 +61,7 @@

import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HacIntent;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.cdma.TtyIntent;
//Synthetic comment -- @@ -502,6 +503,12 @@

// Update HAC Value in AudioManager
mAudioManager.setParameter(HAC_KEY, hac != 0 ? HAC_VAL_ON : HAC_VAL_OFF);

            // Update HAC icon in status bar
            Intent hacModeChanged = new Intent(HacIntent.HAC_ENABLED_CHANGE_ACTION);
            hacModeChanged.putExtra(HacIntent.HAC_ENABLED, mButtonHAC.isChecked());
            sendBroadcast(hacModeChanged);

return true;
} else if (preference == mVoicemailSettings) {
if (DBG) log("onPreferenceTreeClick: Voicemail Settings Preference is clicked.");








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneGlobals.java b/src/com/android/phone/PhoneGlobals.java
//Synthetic comment -- index acfdd7e..2c6a5be 100644

//Synthetic comment -- @@ -59,6 +59,7 @@

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.HacIntent;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MmiCode;
//Synthetic comment -- @@ -634,6 +635,11 @@
audioManager.setParameter(CallFeaturesSetting.HAC_KEY, hac != 0 ?
CallFeaturesSetting.HAC_VAL_ON :
CallFeaturesSetting.HAC_VAL_OFF);
            if (hac != 0) {
                Intent hacModeChanged = new Intent(HacIntent.HAC_ENABLED_CHANGE_ACTION);
                hacModeChanged.putExtra(HacIntent.HAC_ENABLED, true);
                phone.getContext().sendBroadcast(hacModeChanged);
            }
}
}








