/*Fixing counting the number of days in call log screen.

Counting the number of days in call log screen when a
call was made/received within a week is not correct.

Issue 3132: Call Log Reporting Wrong

Change-Id:I99c3cb40267b4b5938ccc4225993b7aac7e2d018Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 9dd8ceb..5c7cb9d 100644

//Synthetic comment -- @@ -497,7 +497,7 @@
}
}
} else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
            count = getNumberOfDaysPassed(time, now);
if (past) {
if (abbrevRelative) {
resId = com.android.internal.R.plurals.abbrev_num_days_ago;
//Synthetic comment -- @@ -522,6 +522,22 @@
}

/**
     * Returns the number of days passed between two dates.
     *
     * @param date1 first date
     * @param date2 second date
     * @return number of days passed between to dates.
     */
    private static long getNumberOfDaysPassed(long date1, long date2) {
        Time time = new Time();
        time.set(date1);
        int day1 = Time.getJulianDay(date1, time.gmtoff);
        time.set(date2);
        int day2 = Time.getJulianDay(date2, time.gmtoff);
        return Math.abs(day2 - day1);
    }
    
    /**
* Return string describing the elapsed time since startTime formatted like
* "[relative time/date], [time]".
* <p>







