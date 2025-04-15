/*Added snooze dialog

Change-Id:Idba3aac867ef65893d258f92e85caac89f4bf634*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index a309618..52454fc 100644

//Synthetic comment -- @@ -17,10 +17,13 @@
package com.android.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -28,6 +31,7 @@
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

//Synthetic comment -- @@ -39,7 +43,7 @@
*/
public class AlarmAlert extends Activity {

    private static final int SNOOZE_MINUTES = 10;
private static final int UNKNOWN = 0;
private static final int SNOOZE = 1;
private static final int DISMISS = 2;
//Synthetic comment -- @@ -51,6 +55,10 @@
private int mAlarmId;
private String mLabel;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -150,8 +158,9 @@
} else {
mSnoozeButton.setOnClickListener(new Button.OnClickListener() {
public void onClick(View v) {
                    snooze();
                    finish();
}
});
}
//Synthetic comment -- @@ -170,14 +179,15 @@
}

// Attempt to snooze this alert.
    private void snooze() {
if (mState != UNKNOWN) {
return;
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        final long snoozeTime = System.currentTimeMillis()
                + (1000 * 60 * SNOOZE_MINUTES);
final long nextAlarm =
Alarms.calculateNextAlert(AlarmAlert.this).getAlert();
String displayTime = null;
//Synthetic comment -- @@ -191,8 +201,7 @@
Alarms.saveSnoozeAlert(AlarmAlert.this, mAlarmId, snoozeTime,
mLabel);
Alarms.setNextAlert(AlarmAlert.this);
            displayTime = getString(R.string.alarm_alert_snooze_set,
                    SNOOZE_MINUTES);
mState = SNOOZE;
}
// Intentionally log the snooze time for debugging.
//Synthetic comment -- @@ -202,6 +211,35 @@
mKlaxon.stop(this, mState == SNOOZE);
releaseLocks();
}

// Dismiss the alarm.
private void dismiss() {
//Synthetic comment -- @@ -252,7 +290,7 @@
super.onStop();
if (Log.LOGV) Log.v("AlarmAlert.onStop()");
// As a last resort, try to snooze if this activity is stopped.
        snooze();
// We might have been killed by the KillerCallback so always release
// the lock and keyguard.
releaseLocks();
//Synthetic comment -- @@ -288,7 +326,7 @@
if (dismiss) {
dismiss();
} else {
                        snooze();
}
finish();
}








//Synthetic comment -- diff --git a/src/com/android/alarmclock/SetAlarm.java b/src/com/android/alarmclock/SetAlarm.java
//Synthetic comment -- index c141812..1fd1eff 100644

//Synthetic comment -- @@ -325,7 +325,7 @@
Alarms.DaysOfWeek daysOfWeek) {
long alarm = Alarms.calculateAlarm(hour, minute,
daysOfWeek).getTimeInMillis();
        long delta = alarm - System.currentTimeMillis();;
long hours = delta / (1000 * 60 * 60);
long minutes = delta / (1000 * 60) % 60;
long days = hours / 24;







