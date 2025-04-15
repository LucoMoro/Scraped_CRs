/*Made snooze boolean persistent. Specifcally, changed it from a static member variable to a SharedPreference.*/




//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index 20212d4..4d8d04d 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
private static final int SNOOZE = 1;
private static final int DISMISS = 2;
private static final int KILLED = 3;
    final static String PREF_SNOOZED = "snoozed";

private KeyguardManager mKeyguardManager;
private KeyguardManager.KeyguardLock mKeyguardLock;
//Synthetic comment -- @@ -56,11 +57,17 @@
private int mAlarmId;
private String mLabel;

    static public boolean getSnoozed(Context context){
        SharedPreferences prefs = context.getSharedPreferences(AlarmClock.PREFERENCES, 0);
        return prefs.getBoolean(PREF_SNOOZED, false);
    }

    static protected void setSnoozed(Context context, boolean snoozed) {
        SharedPreferences prefs = context.getSharedPreferences(AlarmClock.PREFERENCES, 0);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(PREF_SNOOZED, snoozed);
        ed.commit();
    }

@Override
protected void onCreate(Bundle icicle) {
//Synthetic comment -- @@ -194,7 +201,7 @@
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        setSnoozed(this,true);

final long snoozeTime = System.currentTimeMillis()
+ (1000 * 60 * SNOOZE_MINUTES);
//Synthetic comment -- @@ -228,7 +235,7 @@
if (mState != UNKNOWN) {
return;
}
        setSnoozed(this,false);
mState = DISMISS;
mKlaxon.stop(this, false);
releaseLocks();








//Synthetic comment -- diff --git a/src/com/android/alarmclock/Alarms.java b/src/com/android/alarmclock/Alarms.java
//Synthetic comment -- index 06cde27..514df41 100644

//Synthetic comment -- @@ -397,9 +397,9 @@
ContentValues values = new ContentValues(8);
ContentResolver resolver = context.getContentResolver();

            long time = 0;
            if(!AlarmAlert.getSnoozed(context) ){
                time = calculateAlarm(hour, minutes, daysOfWeek).getTimeInMillis();
}


//Synthetic comment -- @@ -419,7 +419,7 @@
values, null, null);

int aid = 0;
        if( !AlarmAlert.getSnoozed(context) ){
aid = disableSnoozeAlert(context);
if (aid != -1 && aid != id) enableAlarmInternal(context, aid, false);
}







