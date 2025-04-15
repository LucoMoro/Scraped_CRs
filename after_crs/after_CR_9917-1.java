/*Fix for Android Issue 2081: error in DatePicker time - days beyond the end of the month*/




//Synthetic comment -- diff --git a/core/java/android/widget/DatePicker.java b/core/java/android/widget/DatePicker.java
//Synthetic comment -- index 54f2707..b4bafe2 100644

//Synthetic comment -- @@ -110,6 +110,14 @@
* subtract by one to ensure our internal state is always 0-11
*/
mMonth = newVal - 1;
                //We need to make sure that the mDay selected is valid for the new month.
                // e.g. if mDay was previously selected to be 31, but we changed the month from May 
                // to April, we need to re-adjust the value to 30.
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, mMonth);
                cal.set(Calendar.YEAR, mYear);
                int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                mDay = mDay > max ? max : mDay;
if (mOnDateChangedListener != null) {
mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
}
//Synthetic comment -- @@ -121,6 +129,15 @@
mYearPicker.setOnChangeListener(new OnChangedListener() {
public void onChanged(NumberPicker picker, int oldVal, int newVal) {
mYear = newVal;
                // Adjust for leap year.
                if (mMonth == Calendar.FEBRUARY){
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, mYear);
                    cal.set(Calendar.MONTH, mMonth);
                    int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    mDay = mDay > max ? max : mDay;
                    updateDaySpinner();
                }
if (mOnDateChangedListener != null) {
mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
}







