/*Date Picker: Update calendar title when year changes

In Date Picker (can be seen thru Settings/Date&time/Set date or
while setting up an appointment in Calendar on tablets)
calendar title is not updated while user scrolls thru year on left side.
It works fine for Month and Day, but not for Year.
This fix introduces a member to hold currently displayed Year and updates
calendar view's title if Year changed. The same way as done for Month.

Change-Id:I5d3b13bbb165ab638291b06e3accc37282f5ad4b*/
//Synthetic comment -- diff --git a/core/java/android/widget/CalendarView.java b/core/java/android/widget/CalendarView.java
//Synthetic comment -- index b06da06..4221aa7 100644

//Synthetic comment -- @@ -250,6 +250,11 @@
private int mCurrentMonthDisplayed;

/**
* Used for tracking during a scroll.
*/
private long mPreviousScrollPosition;
//Synthetic comment -- @@ -1249,7 +1254,10 @@
*/
private void setMonthDisplayed(Calendar calendar) {
final int newMonthDisplayed = calendar.get(Calendar.MONTH);
        if (mCurrentMonthDisplayed != newMonthDisplayed) {
mCurrentMonthDisplayed = newMonthDisplayed;
mAdapter.setFocusMonth(mCurrentMonthDisplayed);
final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY







