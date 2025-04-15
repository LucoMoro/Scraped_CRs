/*Airplane mode is shown in status bar.

When flightmode is activated, Airplane mode is ON is shown in the
expanded status bar.

Change-Id:Ic36e3bc1dfbef4a0d0a7be5cf3c8d084304c3da4*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/phone/CarrierLabel.java b/packages/SystemUI/src/com/android/systemui/statusbar/phone/CarrierLabel.java
//Synthetic comment -- index 66494e4..6e1d022 100644

//Synthetic comment -- @@ -17,9 +17,11 @@
package com.android.systemui.statusbar.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Slog;
//Synthetic comment -- @@ -62,6 +64,7 @@
mAttached = true;
IntentFilter filter = new IntentFilter();
filter.addAction(TelephonyIntents.SPN_STRINGS_UPDATED_ACTION);
            filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
}
}
//Synthetic comment -- @@ -84,16 +87,28 @@
intent.getStringExtra(TelephonyIntents.EXTRA_SPN),
intent.getBooleanExtra(TelephonyIntents.EXTRA_SHOW_PLMN, false),
intent.getStringExtra(TelephonyIntents.EXTRA_PLMN));
            } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                updateNetworkName(false, null, false, null);
}
}
};

    private final boolean isAirPlaneModeOn() {
        ContentResolver resolver = mContext.getContentResolver();
        return Settings.System.getInt(resolver, Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }

void updateNetworkName(boolean showSpn, String spn, boolean showPlmn, String plmn) {
if (false) {
Slog.d("CarrierLabel", "updateNetworkName showSpn=" + showSpn + " spn=" + spn
+ " showPlmn=" + showPlmn + " plmn=" + plmn);
}
final String str;
        if (isAirPlaneModeOn()) {
            showSpn = false;
            showPlmn = true;
            plmn = mContext.getText(R.string.global_actions_airplane_mode_on_status).toString();
        }
// match logic in KeyguardStatusViewManager
final boolean plmnValid = showPlmn && !TextUtils.isEmpty(plmn);
final boolean spnValid = showSpn && !TextUtils.isEmpty(spn);







