/*Fixed android.com issues 2145 and 2399: ringtone change changes snoozehttp://code.google.com/p/android/issues/detail?id=2145http://code.google.com/p/android/issues/detail?id=2399*/




//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index a89a884..20212d4 100644

//Synthetic comment -- @@ -56,6 +56,12 @@
private int mAlarmId;
private String mLabel;

    static private boolean mIsSnoozed = false;
	
    static public boolean isSnoozed(){
       return mIsSnoozed;
    } 

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -188,6 +194,8 @@
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        mIsSnoozed = true;

final long snoozeTime = System.currentTimeMillis()
+ (1000 * 60 * SNOOZE_MINUTES);
final long nextAlarm =
//Synthetic comment -- @@ -220,6 +228,7 @@
if (mState != UNKNOWN) {
return;
}
        mIsSnoozed = false;
mState = DISMISS;
mKlaxon.stop(this, false);
releaseLocks();








//Synthetic comment -- diff --git a/src/com/android/alarmclock/Alarms.java b/src/com/android/alarmclock/Alarms.java
//Synthetic comment -- index 8817234..06cde27 100644

//Synthetic comment -- @@ -396,7 +396,12 @@

ContentValues values = new ContentValues(8);
ContentResolver resolver = context.getContentResolver();

	long time = 0;
	if(!AlarmAlert.isSnoozed() ){
            time = calculateAlarm(hour, minutes, daysOfWeek).getTimeInMillis();
        }


if (Log.LOGV) Log.v(
"**  setAlarm * idx " + id + " hour " + hour + " minutes " +
//Synthetic comment -- @@ -413,8 +418,12 @@
resolver.update(ContentUris.withAppendedId(AlarmColumns.CONTENT_URI, id),
values, null, null);

        int aid = 0;
        if( !AlarmAlert.isSnoozed() ){
           aid = disableSnoozeAlert(context);
           if (aid != -1 && aid != id) enableAlarmInternal(context, aid, false);
        }

setNextAlert(context);
}








