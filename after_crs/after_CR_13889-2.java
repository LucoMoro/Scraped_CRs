/*Title in DatePickerDialog used in Settings application not updated correctly

The DatePickerDialog in the Settings application is not updated correctly if you follow
the following step-by-step:
1. Enter Date option in settings application
2. Modify the values of the date, then cancel the changes
3. Once again enter the date option

and you can see that the title in the dialog has not been updated correctly. This is
due to a missing call to onDateChanged callback in the DatePicker class. Solution was
to add the notify call when updateTime has been called.*/




//Synthetic comment -- diff --git a/core/java/android/widget/DatePicker.java b/core/java/android/widget/DatePicker.java
//Synthetic comment -- index 5e76cc3..736475e 100644

//Synthetic comment -- @@ -94,9 +94,7 @@
mDayPicker.setOnChangeListener(new OnChangedListener() {
public void onChanged(NumberPicker picker, int oldVal, int newVal) {
mDay = newVal;
                notifyDateChanged();
}
});
mMonthPicker = (NumberPicker) findViewById(R.id.month);
//Synthetic comment -- @@ -114,9 +112,7 @@
mMonth = newVal - 1;
// Adjust max day of the month
adjustMaxDay();
                notifyDateChanged();
updateDaySpinner();
}
});
//Synthetic comment -- @@ -127,9 +123,7 @@
mYear = newVal;
// Adjust max day for leap years if needed
adjustMaxDay();
                notifyDateChanged();
updateDaySpinner();
}
});
//Synthetic comment -- @@ -230,11 +224,14 @@
}

public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        if (mYear != year || mMonth != monthOfYear || mDay != dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateSpinners();
            reorderPickers(new DateFormatSymbols().getShortMonths());
            notifyDateChanged();
        }
}

private static class SavedState extends BaseSavedState {
//Synthetic comment -- @@ -376,4 +373,10 @@
mDay = max;
}
}

    private void notifyDateChanged() {
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
}







