//<Beginning of snippet n. 0>
long now = System.currentTimeMillis();
long weekBoundary = now - WEEK_IN_MILLIS;
count = 0;

if (millis >= weekBoundary) {
    count++;
}

if (millis >= now - DAY_IN_MILLIS && millis < now) {
    count++;
}

if (millis >= now - duration) {
    count += (duration / DAY_IN_MILLIS);
}

if (past) {
    if (abbrevRelative) {
        resId = com.android.internal.R.plurals.abbrev_num_days_ago;
    }
}

public static CharSequence getRelativeTimeSpanString(Context c, long millis, boolean withPreposition) {
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

    if (sNowTime.weekDay == sThenTime.weekDay) {
        int flags = FORMAT_SHOW_TIME;
        result = formatDateRange(c, millis, millis, flags);
        prepositionId = R.string.preposition_for_time;
    } else if (sNowTime.year != sThenTime.year) {
        int flags = FORMAT_SHOW_DATE | FORMAT_SHOW_YEAR | FORMAT_NUMERIC_DATE;
        result = formatDateRange(c, millis, millis, flags);
        prepositionId = R.string.preposition_for_date;
    } else {
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