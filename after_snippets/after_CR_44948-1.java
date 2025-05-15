
//<Beginning of snippet n. 0>


import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RestrictedState;
import com.android.internal.R;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.uicc.UiccController;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.util.EventLog;
import android.util.Log;
import android.util.TimeUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.preference.PreferenceManager;

import java.io.FileDescriptor;
import java.io.PrintWriter;
private int mReasonDataDenied = -1;
private int mNewReasonDataDenied = -1;

    // Key used to read and write the saved network selection numeric value
    private static final String NETWORK_SELECTION_KEY = "network_selection_key";
    private static boolean isManagedRoamingDialogDisplayed = false;

/**
* GSM roaming status solely based on TS 27.007 7.2 CREG. Only used by
* handlePollStateResult to store CREG roaming result.
}

mGsmRoaming = regCodeIsRoaming(regState);

                    if ((regState == 3 || regState == 13) && (states.length >= 14)) {
                        try {
                            int rejCode = Integer.parseInt(states[13]);
                            // Check if rejCode is "Persistent location update reject",
                            if (rejCode == 10) {
                                createManagedRoamingDialog();
                            }
                        } catch (NumberFormatException ex) {
                            Log.w(LOG_TAG, "error parsing regCode: " + ex);
                        }
                    }

newSS.setState (regCodeToServiceState(regState));

if (regState == 10 || regState == 12 || regState == 13 || regState == 14) {
pw.println(" curPlmn=" + curPlmn);
pw.println(" curSpnRule=" + curSpnRule);
}

    /*
     * Show Managed Roaming dialog if user preferred Network Selection mode is 'Manual'
     */
    private void createManagedRoamingDialog() {
        Resources r = Resources.getSystem();
        String networkSelection = PreferenceManager.getDefaultSharedPreferences(phone.getContext())
            .getString(NETWORK_SELECTION_KEY, "");

        Log.i(LOG_TAG, "Managed Roaming case, networkSelection " + networkSelection);
        // networkSelection will be empty for 'Automatic' mode.
        if (!TextUtils.isEmpty(networkSelection) && !isManagedRoamingDialogDisplayed) {
            Log.i(LOG_TAG, "Show Managed Roaming Dialog");
            AlertDialog managedRoamingDialog = new AlertDialog.Builder(phone.getContext())
                    .setTitle(r.getString(R.string.managed_roaming_title))
                    .setMessage(r.getString(R.string.managed_roaming_dialog_content))
                    .setPositiveButton(r.getString(R.string.managed_roaming_dialog_ok_button),
                        onManagedRoamingDialogClick)
                    .setNegativeButton(r.getString(R.string.managed_roaming_dialog_cancel_button),
                        onManagedRoamingDialogClick)
                    .create();

            managedRoamingDialog.setOnKeyListener(mManagedRoamingDialogOnKeyListener);
            isManagedRoamingDialogDisplayed = true;
            managedRoamingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            managedRoamingDialog.show();
        }
    }

    DialogInterface.OnClickListener onManagedRoamingDialogClick =
        new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent networkSettingIntent = new Intent(Intent.ACTION_MAIN);
                    networkSettingIntent.setClassName("com.android.phone",
                            "com.android.phone.NetworkSetting");
                    networkSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    phone.getContext().startActivity(networkSettingIntent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
            isManagedRoamingDialogDisplayed = false;
        }
    };

    DialogInterface.OnKeyListener mManagedRoamingDialogOnKeyListener =
        new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            // Handle the back key to reset the global variable.
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                isManagedRoamingDialogDisplayed = false;
            }
            return false;
        }
    };

}

//<End of snippet n. 0>








