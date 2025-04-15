/*Fix rotation bug

Change mScale to static otherwise the code block in line 94 will be
execute multiple times when device rotates.*/
//Synthetic comment -- diff --git a/src/com/android/calendar/selectcalendars/SelectCalendarsSimpleAdapter.java b/src/com/android/calendar/selectcalendars/SelectCalendarsSimpleAdapter.java
//Synthetic comment -- index e42af62..acdd3e6 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
private int mColorColumn;
private int mVisibleColumn;
private int mOwnerAccountColumn;
    private float mScale = 0;
private int mColorCalendarVisible;
private int mColorCalendarHidden;
private int mColorCalendarSecondaryVisible;







