/*Fix problem that the month isn't shown when set date at January

If a CalendarView instance is created and it is set by the setDate()
at January, then the field mCurrentMonthDisplayed is initialized as
zero and also newMonthDisplayed is zero.
So the month view (mMonthName) is not invalidated.
It causes the failure of CTS HoloTest.

Change-Id:I60b76a18a5c28564b1143c7ae837871f7b4827aa*/




//Synthetic comment -- diff --git a/core/java/android/widget/CalendarView.java b/core/java/android/widget/CalendarView.java
old mode 100644
new mode 100755
//Synthetic comment -- index e0403ff..df6f96a

//Synthetic comment -- @@ -935,7 +935,8 @@
*/
private void setMonthDisplayed(Calendar calendar) {
final int newMonthDisplayed = calendar.get(Calendar.MONTH);
        if ((mCurrentMonthDisplayed != newMonthDisplayed)
                || ((mCurrentMonthDisplayed == 0) && (newMonthDisplayed == 0))) {
mCurrentMonthDisplayed = newMonthDisplayed;
mAdapter.setFocusMonth(mCurrentMonthDisplayed);
final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY







