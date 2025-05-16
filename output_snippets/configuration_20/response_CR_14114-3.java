//<Beginning of snippet n. 0>
if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
    long startOfWeek = System.currentTimeMillis() - (System.currentTimeMillis() % WEEK_IN_MILLIS);
    long endOfWeek = startOfWeek + WEEK_IN_MILLIS;

    count = 0;
    boolean[] daysWithCalls = new boolean[7];
    
    for (long callTime : callLog) {
        if (callTime >= startOfWeek && callTime < endOfWeek) {
            int dayIndex = (int) ((callTime / DAY_IN_MILLIS) % 7);
            daysWithCalls[dayIndex] = true;
        }
    }
    
    for (boolean hasCall : daysWithCalls) {
        if (hasCall) count++;
    }

    if (past) {
        if (abbrevRelative) {
            resId = com.android.internal.R.plurals.abbrev_num_days_ago;
        }
    }
}

/**
 * Return string describing the elapsed time since startTime formatted like
 * "[relative time/date], [time]".
 */
public static CharSequence getRelativeTimeSpanString(Context c, long millis,
                                                      boolean withPreposition) {
    long now = System.currentTimeMillis();
    long span = now - millis;

    if (sNowTime == null) {
        sNowTime = new Time();
        sThenTime = new Time();
    }

    sNowTime.set(now);
    sThenTime.set(millis);

    String result;
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
    return result;
}
//<End of snippet n. 0>