/*Fix CalendarView CTS failures

Why: Google added a condition to save cpu time(Maybe). It would cause
the CalendarView not showing the title in Jan. because the default
value of mCurrentMonthDisplayed is 0.
How: Assign -1 as default value to mCurrentMonthDisplayed. The
constructor will call goTo() to assign a correct value to it. Not only
that, previously it only shows "month" in the title but now it also
shows year. So I also added a newYearDisplayed for showing it correctly.
Verify steps: CTS should not fail in the Holo test in Jan. I have
verified it with android-cts-4.0.3_r1.

Change-Id:I44f938ff416b4943ea36abcd0ceadcbf03ae7377*/




//Synthetic comment -- diff --git a/core/java/android/widget/CalendarView.java b/core/java/android/widget/CalendarView.java
//Synthetic comment -- index e0403ff..dace524 100644

//Synthetic comment -- @@ -241,7 +241,12 @@
/**
* Which month should be displayed/highlighted [0-11].
*/
    private int mCurrentMonthDisplayed = -1;

    /**
     * Which year should be displayed/highlighted.
     */
    private int mCurrentYearDisplayed = -1;

/**
* Used for tracking during a scroll.
//Synthetic comment -- @@ -935,8 +940,11 @@
*/
private void setMonthDisplayed(Calendar calendar) {
final int newMonthDisplayed = calendar.get(Calendar.MONTH);
        final int newYearDisplayed = calendar.get(Calendar.YEAR);

        if (mCurrentMonthDisplayed != newMonthDisplayed || mCurrentYearDisplayed != newYearDisplayed) {
mCurrentMonthDisplayed = newMonthDisplayed;
            mCurrentYearDisplayed = newYearDisplayed;
mAdapter.setFocusMonth(mCurrentMonthDisplayed);
final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY
| DateUtils.FORMAT_SHOW_YEAR;







