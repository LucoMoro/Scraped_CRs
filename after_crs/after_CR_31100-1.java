/*Fix CalendarView CTS failures

Why: Google added a condition to save cpu time(Maybe). It would cause
the CalendarView not showing the title in Jan. because the default
value of mCurrentMonthDisplayed is 0.
How: Remove that condition
Verify steps: CTS should not fail in the Holo test in Jan.

Change-Id:I44f938ff416b4943ea36abcd0ceadcbf03ae7377*/




//Synthetic comment -- diff --git a/core/java/android/widget/CalendarView.java b/core/java/android/widget/CalendarView.java
//Synthetic comment -- index e0403ff..a33329f 100644

//Synthetic comment -- @@ -934,17 +934,14 @@
* @param calendar A day in the new focus month.
*/
private void setMonthDisplayed(Calendar calendar) {
        mCurrentMonthDisplayed = calendar.get(Calendar.MONTH);
        mAdapter.setFocusMonth(mCurrentMonthDisplayed);
        final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY
                | DateUtils.FORMAT_SHOW_YEAR;
        final long millis = calendar.getTimeInMillis();
        String newMonthName = DateUtils.formatDateRange(mContext, millis, millis, flags);
        mMonthName.setText(newMonthName);
        mMonthName.invalidate();
}

/**







