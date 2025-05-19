//<Beginning of snippet n. 0>
p.setColor(mMonthDayNumberColor);
if (day - mFirstJulianDay >= 0 && day - mFirstJulianDay < eventDay.length) {
    if (eventDay[day - mFirstJulianDay]) {
        p.setColor(mEventDayHighlightColor);
    } else {
        p.setColor(fallbackColor); // Fallback color for non-event days
    }
} else {
    p.setColor(fallbackColor); // Fallback for out-of-bounds days
}
// Drawing of day number is done here
// easy to find tags draw number draw day
//<End of snippet n. 0>