//<Beginning of snippet n. 0>
if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
    count = duration / DAY_IN_MILLIS;
    if (past) {
        if (abbrevRelative) {
            resId = com.android.internal.R.plurals.abbrev_num_days_ago;
        }
    }
}

public static CharSequence getRelativeTimeSpanString(Context c, long millis,
boolean withPreposition) {

    long now = System.currentTimeMillis();
    long span = now - millis;

    Time sNowTime = new Time();
    Time sThenTime = new Time();
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