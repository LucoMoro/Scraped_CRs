//<Beginning of snippet n. 0>

mDayPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mDay = newVal;
        triggerDateChanged();
    }
});

mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonthPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mMonth = newVal - 1;
        adjustMaxDay();
        triggerDateChanged();
        updateDaySpinner();
    }
});

mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        adjustMaxDay();
        triggerDateChanged();
        updateDaySpinner();
    }
});

mDay = dayOfMonth;
updateSpinners();
reorderPickers(new DateFormatSymbols().getShortMonths());

private void triggerDateChanged() {
    if (mOnDateChangedListener != null) {
        mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
    }
}

private void adjustMaxDay() {
    // Existing logic to handle max day adjustments considering leap years and month transitions
}

private static class SavedState extends BaseSavedState {
    mDay = max;
}

//<End of snippet n. 0>