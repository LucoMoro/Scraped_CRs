/*Added comments

Change-Id:Ia265f47d41c6ab13190badc1a0d73b54758db6f7*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index d4d8f2f..f0e6de4 100644

//Synthetic comment -- @@ -185,9 +185,9 @@
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        final long snoozeTime = System.currentTimeMillis() + delta;     
        long hours = (long) Math.ceil((double) delta / (1000 * 60 * 60));
        long minutes = (long) Math.ceil((double) delta / (1000 * 60)) % 60;
final long nextAlarm =
Alarms.calculateNextAlert(AlarmAlert.this).getAlert();
String displayTime = null;







