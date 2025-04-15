/*Resolve issue 31246: infinite alarms every 15 seconds

Resolves issue with Email (and Exchange?) that caused an ALARM_RTC to be
signaled every 15 seconds without ending (repeating alarm that was never
properly stopped.) The cause was a self-repeating alarm that wasn't being
canceled (and service that should have cancelled it was destroyed before
it got canceled.) This was great for battery life (as long as you didn't want
to use the phone unplugged.)

By replacing the self-repeating alarm with one that's manually re-set each
iteration, the alarm will gracefully go away when the service does. For a
discussion on this issue, please see:http://code.google.com/p/android/issues/detail?id=31246Change-Id:I208786830eaf12f02c14194b4aecbbe35d48fe0fSigned-off-by: Gary Dezern <garyd9@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/email/service/AttachmentDownloadService.java b/src/com/android/email/service/AttachmentDownloadService.java
//Synthetic comment -- index 18468fb..a6a1b09 100644

//Synthetic comment -- @@ -416,12 +416,17 @@
* have failed silently (the connection dropped, for example)
*/
private void onWatchdogAlarm() {
            // If our service instance is gone, just leave (alarm will go away on its own)
if (mStop) {
return;
}
long now = System.currentTimeMillis();

            // schedule the next watchdog alarm
            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + WATCHDOG_CHECK_INTERVAL,
                    mWatchdogPendingIntent);

for (DownloadRequest req: mDownloadsInProgress.values()) {
// Check how long it's been since receiving a callback
long timeSinceCallback = now - req.lastCallbackTime;
//Synthetic comment -- @@ -493,9 +498,9 @@
if (mWatchdogPendingIntent == null) {
createWatchdogPendingIntent(mContext);
}
            // Set the alarm for one shot
            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + WATCHDOG_CHECK_INTERVAL,
mWatchdogPendingIntent);
}








