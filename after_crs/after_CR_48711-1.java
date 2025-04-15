/*TIME_TICK was not occurred for one hour

TIME_TICK was not occurred at the end of DST for one hour

Step to reproduce:
1. Settings -> Date & time
2. uncheck Automatic date & time and Automatic time zone
3. select Time zone -> select Brussels
4. set date -> Oct. 28. 2012
5. set time -> 1:59 AM

Bug: 7922117
Change-Id:I2e78bd97b508d6a38471425cfbaca45fb4b89c1e*/




//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index 440f8e1..bd27b29 100644

//Synthetic comment -- @@ -820,9 +820,8 @@
final long currentTime = System.currentTimeMillis();
calendar.setTimeInMillis(currentTime);
calendar.add(Calendar.MINUTE, 1);
            calendar.add(Calendar.SECOND, -calendar.get(Calendar.SECOND));
            calendar.add(Calendar.MILLISECOND, -calendar.get(Calendar.MILLISECOND));
// Schedule this event for the amount of time that it would take to get to
// the top of the next minute.
final long tickEventDelay = calendar.getTimeInMillis() - currentTime;







