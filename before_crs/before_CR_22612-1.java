/*Change flipping point of all day events (such as birthdays) to use half way through the day, instead of start

Change-Id:I7bad7ce78d3f0b2a2e78917bfab58d7927d9111c*/
//Synthetic comment -- diff --git a/src/com/android/providers/calendar/CalendarAppWidgetService.java b/src/com/android/providers/calendar/CalendarAppWidgetService.java
//Synthetic comment -- index 584aa9b..45a4c3e 100644

//Synthetic comment -- @@ -334,7 +334,7 @@
*/
private long getEventFlip(Cursor cursor, long start, long end, boolean allDay) {
long duration = end - start;
        if (allDay || duration > DateUtils.DAY_IN_MILLIS) {
return start;
} else {
return (start + end) / 2;







