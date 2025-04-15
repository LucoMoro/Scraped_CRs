/*When entering airplane mode, airplane mode is shown in status bar.

When flightmode is activated, Airplane mode is ON is shown in the
expanded status bar.

Operators request this feature.

Change-Id:Ic36e3bc1dfbef4a0d0a7be5cf3c8d084304c3da4*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/CarrierLabel.java b/packages/SystemUI/src/com/android/systemui/statusbar/CarrierLabel.java
//Synthetic comment -- index 31b78b6..41248ec 100644

//Synthetic comment -- @@ -17,9 +17,11 @@
package com.android.systemui.statusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.util.Slog;
//Synthetic comment -- @@ -60,6 +62,7 @@
mAttached = true;
IntentFilter filter = new IntentFilter();
filter.addAction(Telephony.Intents.SPN_STRINGS_UPDATED_ACTION);
getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
}
}
//Synthetic comment -- @@ -82,10 +85,17 @@
intent.getStringExtra(Telephony.Intents.EXTRA_SPN),
intent.getBooleanExtra(Telephony.Intents.EXTRA_SHOW_PLMN, false),
intent.getStringExtra(Telephony.Intents.EXTRA_PLMN));
}
}
};

void updateNetworkName(boolean showSpn, String spn, boolean showPlmn, String plmn) {
if (false) {
Slog.d("CarrierLabel", "updateNetworkName showSpn=" + showSpn + " spn=" + spn
//Synthetic comment -- @@ -93,6 +103,11 @@
}
StringBuilder str = new StringBuilder();
boolean something = false;
if (showPlmn && plmn != null) {
str.append(plmn);
something = true;







