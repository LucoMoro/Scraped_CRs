/*Fix system clock stops ticking issue

Due to original logic calculates the RTC of the next minutue
by Calendar. It'll be affected by the daylight saving status.
When device is leaving the daylight saving, it'll get current
time plus the offset of the daylight saving offset (bcz it's
instructed to get the time). This will make the system clock
stop ticking until the offset is passed.
The solution is to advance the next alarm by adding 60*1000
milliseconds directly

Change-Id:I3820b4552ebeae8e99e5fc8a1be35b8ed0f96544*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index 8852cc3..2b69600 100644

//Synthetic comment -- @@ -61,6 +61,8 @@
private static final int ELAPSED_REALTIME_WAKEUP_MASK = 1 << AlarmManager.ELAPSED_REALTIME_WAKEUP; 
private static final int ELAPSED_REALTIME_MASK = 1 << AlarmManager.ELAPSED_REALTIME;
private static final int TIME_CHANGED_MASK = 1 << 16;

private static final String TAG = "AlarmManager";
private static final String ClockReceiver_TAG = "ClockReceiver";
//Synthetic comment -- @@ -785,15 +787,12 @@
	scheduleDateChangedEvent();
}
}
        
public void scheduleTimeTickEvent() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
      
            set(AlarmManager.RTC, calendar.getTimeInMillis(), mTimeTickSender);
}
	
public void scheduleDateChangedEvent() {







