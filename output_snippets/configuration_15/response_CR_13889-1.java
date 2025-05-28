//<Beginning of snippet n. 0>
mDayPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mDay = newVal;
        updateDateChangedListener();
        updateSpinners();
    }
});

mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonthPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mMonth = newVal - 1;
        adjustMaxDay();
        updateDateChangedListener();
        updateSpinners();
    }
});

mYearPicker = (NumberPicker) findViewById(R.id.year);
mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        adjustMaxDay();
        updateDateChangedListener();
        updateSpinners();
    }
});

mDay = dayOfMonth;
updateSpinners();
reorderPickers(new DateFormatSymbols().getShortMonths());

private void updateDateChangedListener() {
    if (mOnDateChangedListener != null) {
        mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
    }
}

private static class SavedState extends BaseSavedState {
    int mDay;
    int mYear;
    int mMonth;
    
    SavedState(Parcelable superState) {
        super(superState);
    }
}
//<End of snippet n. 0>