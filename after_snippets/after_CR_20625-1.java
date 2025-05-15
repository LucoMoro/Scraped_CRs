
//<Beginning of snippet n. 0>


p.setColor(mMonthDayNumberColor);
}
//bolds the day if there's an event that day
			p.setFakeBoldText(false);
            ArrayList<Event> events = mEvents;
            if (eventDay[day-mFirstJulianDay]) {
                for(int e = 0;e < events.size();e++) {
                    Event event = events.get(e);
                    if (event.startDay <= day && event.endDay >= day && event.allDay) {
                        p.setFakeBoldText(true);
                        p.setColor(event.color);
                    }
                }
            }
}
/*Drawing of day number is done here
*easy to find tags draw number draw day*/

//<End of snippet n. 0>








