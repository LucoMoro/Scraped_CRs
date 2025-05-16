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
        mMonth = newVal - 1; // Assuming newVal is 1-based index for month
        adjustMaxDay();
        updateDaySpinner();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
});

mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        adjustMaxDay();
        updateDaySpinner();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
});

mDay = dayOfMonth;
updateSpinners();
reorderPickers(new DateFormatSymbols().getShortMonths());

private static class SavedState extends BaseSavedState {
    mDay = max;
}

//<End of snippet n. 0>