//<Beginning of snippet n. 0>

mDayPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mDay = newVal;
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
});

mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonthPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mMonth = newVal - 1;
        // Adjust max day of the month
        adjustMaxDay();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
        updateDaySpinner();
    }
});

mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        // Adjust max day for leap years if needed
        adjustMaxDay();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
        updateDaySpinner();
    }
});

mDay = dayOfMonth;
updateSpinners();
reorderPickers(new DateFormatSymbols().getShortMonths());

private void onCancel() {
    if (mOnDateChangedListener != null) {
        mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
    }
}

private static class SavedState extends BaseSavedState {
    mDay = max;
}
//<End of snippet n. 0>