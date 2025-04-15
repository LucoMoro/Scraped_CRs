/*Updated MonthView
  Added Color highlighting to the dates instead of a black bold

Change-Id:I5f29c0008294e9fbe12f0ecfbabca511ec4c4aed*/




//Synthetic comment -- diff --git a/src/com/android/calendar/MonthView.java b/src/com/android/calendar/MonthView.java
//Synthetic comment -- index 070dc5b..dfe4d31 100644

//Synthetic comment -- @@ -908,7 +908,17 @@
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







