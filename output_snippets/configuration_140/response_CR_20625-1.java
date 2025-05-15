//<Beginning of snippet n. 0>


p.setColor(mMonthDayNumberColor);
// Highlight the day if there's an event that day
if (eventDay[day - mFirstJulianDay]) {
    p.setColor(mEventDayColor); // Use a distinct color for event days
} else {
    p.setColor(mMonthDayNumberColor); // Default color for non-event days
}
/*Drawing of day number is done here
*easy to find tags draw number draw day*/

//<End of snippet n. 0>