/*When entering airplane mode, airplane mode is shown in status bar.

When flightmode is activated, Airplane mode is ON is shown in the
expanded status bar.

Operators request this feature.

Change-Id:Ic36e3bc1dfbef4a0d0a7be5cf3c8d084304c3da4*/




//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarService.java b/services/java/com/android/server/status/StatusBarService.java
//Synthetic comment -- index b9a57d6..9acd7f35 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Slog;
import android.view.Display;
//Synthetic comment -- @@ -319,7 +320,7 @@
mDateView.setVisibility(View.INVISIBLE);

// before we register for broadcasts
        mPlmnLabel.setText(getDefaultPlmnText());
mPlmnLabel.setVisibility(View.VISIBLE);
mSpnLabel.setText("");
mSpnLabel.setVisibility(View.GONE);
//Synthetic comment -- @@ -330,6 +331,7 @@
filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
filter.addAction(Intent.ACTION_SCREEN_OFF);
filter.addAction(Telephony.Intents.SPN_STRINGS_UPDATED_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
context.registerReceiver(mBroadcastReceiver, filter);
}

//Synthetic comment -- @@ -1757,9 +1759,25 @@
else if (Intent.ACTION_CONFIGURATION_CHANGED.equals(action)) {
updateResources();
}
            else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                final boolean showSpn = (mSpnLabel.getVisibility() == View.VISIBLE);
                updateNetworkName(showSpn, mSpnLabel.getText().toString(), true, null);
            }
}
};
    private String getDefaultPlmnText() {
        String plmn;
        if (Settings.System.getInt(mContext.getContentResolver(),
            Settings.System.AIRPLANE_MODE_ON, 0) == 1) {
            plmn = mContext
                    .getText(R.string.global_actions_airplane_mode_on_status)
                    .toString();
        } else {
            plmn = mContext.getText(R.string.lockscreen_carrier_default)
                    .toString();
        }
        return plmn;
    }
void updateNetworkName(boolean showSpn, String spn, boolean showPlmn, String plmn) {
if (false) {
Slog.d(TAG, "updateNetworkName showSpn=" + showSpn + " spn=" + spn
//Synthetic comment -- @@ -1771,7 +1789,7 @@
if (plmn != null) {
mPlmnLabel.setText(plmn);
} else {
                mPlmnLabel.setText(getDefaultPlmnText());
}
} else {
mPlmnLabel.setText("");







