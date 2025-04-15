/*Telephony: Managed Roaming feature.

Change-Id:I4411e86846b80379a373bb1911d292201f72716b*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index a0be5d0..491fc7f 100755

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RestrictedState;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
//Synthetic comment -- @@ -36,12 +37,14 @@
import com.android.internal.telephony.uicc.UiccController;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
//Synthetic comment -- @@ -63,6 +66,9 @@
import android.util.EventLog;
import android.util.Log;
import android.util.TimeUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -93,6 +99,10 @@
private int mReasonDataDenied = -1;
private int mNewReasonDataDenied = -1;

/**
* GSM roaming status solely based on TS 27.007 7.2 CREG. Only used by
* handlePollStateResult to store CREG roaming result.
//Synthetic comment -- @@ -589,6 +599,19 @@
}

mGsmRoaming = regCodeIsRoaming(regState);
newSS.setState (regCodeToServiceState(regState));

if (regState == 10 || regState == 12 || regState == 13 || regState == 14) {
//Synthetic comment -- @@ -1695,4 +1718,64 @@
pw.println(" curPlmn=" + curPlmn);
pw.println(" curSpnRule=" + curSpnRule);
}
}







