/*Nothing important

Change-Id:Ia1c8614f8f1923e63523dc92cf711a159d39189e*/




//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index 52454fc..0c256f5 100644

//Synthetic comment -- @@ -185,8 +185,7 @@
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        final long snoozeTime = System.currentTimeMillis() + delta;       
final long snoozeMinutes = (long) Math.ceil((double) delta / (1000 * 60));
final long nextAlarm =
Alarms.calculateNextAlert(AlarmAlert.this).getAlert();







