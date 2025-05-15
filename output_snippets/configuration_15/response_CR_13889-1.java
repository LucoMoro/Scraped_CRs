//<Beginning of snippet n. 0>
mDayPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mDay = newVal;
        updateDateComponents();
    }
});

mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonthPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mMonth = newVal - 1;
        adjustMaxDay();
        updateDateComponents();
        updateDaySpinner();
    }
});

mYearPicker = (NumberPicker) findViewById(R.id.year);
mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        adjustMaxDay();
        updateDateComponents();
        updateDaySpinner();
    }
});

private void updateDateComponents() {
    if (mOnDateChangedListener != null) {
        mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
    }
}

private void updateSpinners() {
    mDayPicker.setValue(mDay);
    mMonthPicker.setValue(mMonth + 1);
    mYearPicker.setValue(mYear);
}

private static class SavedState extends BaseSavedState {
    private int mDay;
    private int mMonth;
    private int mYear;

    SavedState(Parcelable superState) {
        super(superState);
    }

    // Restore state logic here
}
//<End of snippet n. 0>