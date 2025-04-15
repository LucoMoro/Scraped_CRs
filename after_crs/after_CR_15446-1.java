/*Replaced deprecated Notification Constructor

Change-Id:I10eceddae9dc0be5f0adf5d2d3af5d39f15808e0*/




//Synthetic comment -- diff --git a/src/com/android/phone/NotificationMgr.java b/src/com/android/phone/NotificationMgr.java
//Synthetic comment -- index 65595de..c335b02 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.CallLog.Calls;
//Synthetic comment -- @@ -50,7 +49,6 @@
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;

/**
* NotificationManager-related utility code for the Phone app.
*/
//Synthetic comment -- @@ -414,14 +412,14 @@
final Intent intent = PhoneApp.createCallLogIntent();

// make the notification
        Notification note = new Notification(
android.R.drawable.stat_notify_missed_call, // icon
mContext.getString(R.string.notification_missedCallTicker, callName), // tickerText
                date // when
);
        note.setLatestEventInfo(mContext, mContext.getText(titleResId), expandedText,
                PendingIntent.getActivity(mContext, 0, intent, 0));

configureLedNotification(note);
mNotificationMgr.notify(MISSED_CALL_NOTIFICATION, note);
}
//Synthetic comment -- @@ -818,17 +816,17 @@
intent.setClassName("com.android.phone",
"com.android.phone.CallFeaturesSetting");


                notification = new Notification(
                        android.R.drawable.stat_sys_phone_call_forward, // icon
                        null, // tickerText
                        0); // The "timestamp" of this notification is meaningless;
                            // we only care about whether CFI is currently on or not.
                notification.setLatestEventInfo(
                        mContext, // context
                        mContext.getString(R.string.labelCF), // expandedTitle
                        mContext.getString(R.string.sum_cfu_enabled_indicator), // expandedText
                        PendingIntent.getActivity(mContext, 0, intent, 0)); // contentIntent
} else {
notification = new Notification(
android.R.drawable.stat_sys_phone_call_forward,  // icon
//Synthetic comment -- @@ -859,14 +857,15 @@
Settings.class);  // "Mobile network settings" screen

Notification notification = new Notification(
                android.R.drawable.stat_sys_warning, // icon
null, // tickerText
                System.currentTimeMillis());
        notification.setLatestEventInfo(
                mContext, // Context
mContext.getString(R.string.roaming), // expandedTitle
                mContext.getString(R.string.roaming_reenable_message), // expandedText
                PendingIntent.getActivity(mContext, 0, intent, 0)); // contentIntent

mNotificationMgr.notify(
DATA_DISCONNECTED_ROAMING_NOTIFICATION,
notification);







