/*Whitespace now conforms to guidelines.*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index 4d8d04d..e3b08c1 100644

//Synthetic comment -- @@ -201,7 +201,7 @@
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        setSnoozed(this,true);

final long snoozeTime = System.currentTimeMillis()
+ (1000 * 60 * SNOOZE_MINUTES);
//Synthetic comment -- @@ -235,7 +235,7 @@
if (mState != UNKNOWN) {
return;
}
        setSnoozed(this,false);
mState = DISMISS;
mKlaxon.stop(this, false);
releaseLocks();








//Synthetic comment -- diff --git a/src/com/android/alarmclock/Alarms.java b/src/com/android/alarmclock/Alarms.java
//Synthetic comment -- index 514df41..82dc94e 100644

//Synthetic comment -- @@ -397,12 +397,11 @@
ContentValues values = new ContentValues(8);
ContentResolver resolver = context.getContentResolver();

            long time = 0;
            if(!AlarmAlert.getSnoozed(context) ){
                time = calculateAlarm(hour, minutes, daysOfWeek).getTimeInMillis();
}


if (Log.LOGV) Log.v(
"**  setAlarm * idx " + id + " hour " + hour + " minutes " +
minutes + " enabled " + enabled + " time " + time);







