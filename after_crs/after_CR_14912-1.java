/*Issue id:3132
Call Log Reporting Wrong.

Change-Id:Ib4d414ec3eb518c8039ca093dd4117ac376fa5ec*/




//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 9dd8ceb..5dff587 100644

//Synthetic comment -- @@ -449,6 +449,14 @@
boolean past = (now >= time);
long duration = Math.abs(now - time);

        Time startTime = new Time();
        startTime.set(time);
        Time currentTime = new Time();
        currentTime.set(now);

        int startDay = Time.getJulianDay(time, startTime.gmtoff);
        int currentDay = Time.getJulianDay(now, currentTime.gmtoff);

int resId;
long count;
if (duration < MINUTE_IN_MILLIS && minResolution < MINUTE_IN_MILLIS) {
//Synthetic comment -- @@ -497,7 +505,7 @@
}
}
} else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
            count = Math.abs(currentDay - startDay);
if (past) {
if (abbrevRelative) {
resId = com.android.internal.R.plurals.abbrev_num_days_ago;







