/*AlarmManagerService: add trace to know what intent triggers

This adds a trace to know what intent triggered the alarm,
its useful to debug situations when the alarm is constantly
waking up the system, so that we can quickly identify the
culprit from logs. We try to print only alarms of type _WAKEUP
but since the intent of the alarm can only be recovered from
the OnFinished callback, and there is no alarm structure in this
function, we use a global flag to print the first onFinished
intent after a send operation of alarm type _WAKEUP.

Adds the possibility to enable/disable debug messages from AlarmManager at runtime.
By default, debug messages are not enabled, so there's no performance or power impact.
To enable them, set property log.tag.AlarmManager to level "DEBUG" or "VERBOSE".

Change-Id:Ia119bd74c64dc5166d3f35f2eede4c3db1c308a9Author: Axel Haslam <axelx.haslam@intel.com>
Signed-off-by: Catalin Popescu<catalinx.popescu@intel.com>
Signed-off-by: Axel Haslam <axelx.haslam@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 31176, 48403*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index 32ac8e1..8988766 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.os.WorkSource;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Slog;
import android.util.TimeUtils;

//Synthetic comment -- @@ -70,7 +71,6 @@

private static final String TAG = "AlarmManager";
private static final String ClockReceiver_TAG = "ClockReceiver";
    private static final boolean localLOGV = false;
private static final int ALARM_EVENT = 1;
private static final String TIMEZONE_PROPERTY = "persist.sys.timezone";

//Synthetic comment -- @@ -98,7 +98,8 @@
private final ResultReceiver mResultReceiver = new ResultReceiver();
private final PendingIntent mTimeTickSender;
private final PendingIntent mDateChangeSender;
    
private static final class FilterStats {
int count;
}
//Synthetic comment -- @@ -177,7 +178,7 @@
// Remove this alarm if already scheduled.
removeLocked(operation);

            if (localLOGV) Slog.v(TAG, "set: " + alarm);

int index = addAlarmLocked(alarm);
if (index == 0) {
//Synthetic comment -- @@ -201,7 +202,7 @@

// If the requested interval isn't a multiple of 15 minutes, just treat it as exact
if (interval % QUANTUM != 0) {
            if (localLOGV) Slog.v(TAG, "Interval " + interval + " not a quantum multiple");
setRepeating(type, triggerAtTime, interval, operation);
return;
}
//Synthetic comment -- @@ -224,7 +225,7 @@
}

// Set the alarm based on the quantum-aligned start time
        if (localLOGV) Slog.v(TAG, "setInexactRepeating: type=" + type + " interval=" + interval
+ " trigger=" + adjustedTriggerTime + " orig=" + triggerAtTime);
setRepeating(type, adjustedTriggerTime, interval, operation);
}
//Synthetic comment -- @@ -250,7 +251,7 @@
synchronized (this) {
String current = SystemProperties.get(TIMEZONE_PROPERTY);
if (current == null || !current.equals(zone.getID())) {
                if (localLOGV) Slog.v(TAG, "timezone changed: " + current + ", new=" + zone.getID());
timeZoneWasChanged = true; 
SystemProperties.set(TIMEZONE_PROPERTY, zone.getID());
}
//Synthetic comment -- @@ -362,10 +363,10 @@
if (index < 0) {
index = 0 - index - 1;
}
        if (localLOGV) Slog.v(TAG, "Adding alarm " + alarm + " at " + index);
alarmList.add(index, alarm);

        if (localLOGV) {
// Display the list of alarms for this alarm type
Slog.v(TAG, "alarms: " + alarmList.size() + " type: " + alarm.type);
int position = 0;
//Synthetic comment -- @@ -513,7 +514,7 @@
{
Alarm alarm = it.next();

            if (localLOGV) Slog.v(TAG, "Checking active alarm when=" + alarm.when + " " + alarm);

if (alarm.when > now) {
// don't fire alarms in the future
//Synthetic comment -- @@ -524,7 +525,7 @@
// Note that this can happen if the user creates a new event on
// the Calendar app with a reminder that is in the past. In that
// case, the reminder alarm will fire immediately.
            if (localLOGV && now - alarm.when > LATE_ALARM_THRESHOLD) {
Slog.v(TAG, "alarm is late! alarm time: " + alarm.when
+ " now: " + now + " delay (in seconds): "
+ (now - alarm.when) / 1000);
//Synthetic comment -- @@ -532,7 +533,7 @@

// Recurring alarms may have passed several alarm intervals while the
// phone was asleep or off, so pass a trigger count when sending them.
            if (localLOGV) Slog.v(TAG, "Alarm triggering: " + alarm);
alarm.count = 1;
if (alarm.repeatInterval > 0) {
// this adjustment will be zero if we're late by
//Synthetic comment -- @@ -643,7 +644,7 @@
synchronized (mLock) {
final long nowRTC = System.currentTimeMillis();
final long nowELAPSED = SystemClock.elapsedRealtime();
                    if (localLOGV) Slog.v(
TAG, "Checking for alarms... rtc=" + nowRTC
+ ", elapsed=" + nowELAPSED);

//Synthetic comment -- @@ -664,7 +665,12 @@
while (it.hasNext()) {
Alarm alarm = it.next();
try {
                            if (localLOGV) Slog.v(TAG, "sending alarm " + alarm);
alarm.operation.send(mContext, 0,
mBackgroundIntent.putExtra(
Intent.EXTRA_ALARM_COUNT, alarm.count),
//Synthetic comment -- @@ -893,6 +899,12 @@
bs.filterStats.put(fc, fs);
}
fs.count++;
}
}
mInFlight.removeFirst();







