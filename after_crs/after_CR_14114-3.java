/*Fixing counting the number of days in call log screen.

Counting the number of days in call log screen when a
call was made/received within a week is not correct.

Issue 3132: Call Log Reporting Wrong

Change-Id:I99c3cb40267b4b5938ccc4225993b7aac7e2d018Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 9dd8ceb..8ae1e79 100644

//Synthetic comment -- @@ -497,7 +497,7 @@
}
}
} else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
            count = getNumberOfDaysPassed(time, now);
if (past) {
if (abbrevRelative) {
resId = com.android.internal.R.plurals.abbrev_num_days_ago;
//Synthetic comment -- @@ -522,6 +522,24 @@
}

/**
     * Returns the number of days passed between two dates.
     *
     * @param date1 first date
     * @param date2 second date
     * @return number of days passed between to dates.
     */
    private synchronized static long getNumberOfDaysPassed(long date1, long date2) {
        if (sThenTime == null) {
            sThenTime = new Time();
        }
        sThenTime.set(date1);
        int day1 = Time.getJulianDay(date1, sThenTime.gmtoff);
        sThenTime.set(date2);
        int day2 = Time.getJulianDay(date2, sThenTime.gmtoff);
        return Math.abs(day2 - day1);
    }
    
    /**
* Return string describing the elapsed time since startTime formatted like
* "[relative time/date], [time]".
* <p>
//Synthetic comment -- @@ -1655,40 +1673,45 @@
public static CharSequence getRelativeTimeSpanString(Context c, long millis,
boolean withPreposition) {

        String result;
long now = System.currentTimeMillis();
long span = now - millis;
        
        synchronized (DateUtils.class) {
            if (sNowTime == null) {
                sNowTime = new Time();
            }
            
            if (sThenTime == null) {
                sThenTime = new Time();
            }
            
            sNowTime.set(now);
            sThenTime.set(millis);
    
            int prepositionId;
            if (span < DAY_IN_MILLIS && sNowTime.weekDay == sThenTime.weekDay) {
                // Same day
                int flags = FORMAT_SHOW_TIME;
                result = formatDateRange(c, millis, millis, flags);
                prepositionId = R.string.preposition_for_time;
            } else if (sNowTime.year != sThenTime.year) {
                // Different years
                int flags = FORMAT_SHOW_DATE | FORMAT_SHOW_YEAR | FORMAT_NUMERIC_DATE;
                result = formatDateRange(c, millis, millis, flags);
    
                // This is a date (like "10/31/2008" so use the date preposition)
                prepositionId = R.string.preposition_for_date;
            } else {
                // Default
                int flags = FORMAT_SHOW_DATE | FORMAT_ABBREV_MONTH;
                result = formatDateRange(c, millis, millis, flags);
                prepositionId = R.string.preposition_for_date;
            }
            if (withPreposition) {
                Resources res = c.getResources();
                result = res.getString(prepositionId, result);
            }
}
return result;
}







