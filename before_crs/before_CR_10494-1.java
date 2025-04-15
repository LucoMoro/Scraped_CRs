/*Fix to Android issue #2081 Error in DatePicker Time - days beyond the end of the monthhttp://code.google.com/p/android/issues/detail?id=2081Also fixed a related bug that was calling onChanged() twice.*/
//Synthetic comment -- diff --git a/core/java/android/widget/DatePicker.java b/core/java/android/widget/DatePicker.java
//Synthetic comment -- index 54f2707..7d65467 100644

//Synthetic comment -- @@ -110,6 +110,7 @@
* subtract by one to ensure our internal state is always 0-11
*/
mMonth = newVal - 1;
if (mOnDateChangedListener != null) {
mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
}
//Synthetic comment -- @@ -121,9 +122,11 @@
mYearPicker.setOnChangeListener(new OnChangedListener() {
public void onChanged(NumberPicker picker, int oldVal, int newVal) {
mYear = newVal;
if (mOnDateChangedListener != null) {
mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
}
}
});

//Synthetic comment -- @@ -307,6 +310,20 @@
mDayPicker.setCurrent(mDay);
}

public int getYear() {
return mYear;
}








//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/NumberPicker.java b/core/java/com/android/internal/widget/NumberPicker.java
//Synthetic comment -- index 2f08c8d..288b3a0 100644

//Synthetic comment -- @@ -244,8 +244,7 @@
int val = getSelectedPos(str.toString());
if ((val >= mStart) && (val <= mEnd)) {
mPrevious = mCurrent;
            mCurrent = val;
            notifyChange();
}
updateView();
}







