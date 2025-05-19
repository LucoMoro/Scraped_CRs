//<Beginning of snippet n. 0>

mDayPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mDay = newVal;
        updateDaySpinner();
        notifyDateChanged();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
});

mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonthPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mMonth = newVal - 1;
        adjustMaxDay();
        updateDaySpinner();
        notifyDateChanged();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
});

mYearPicker = (NumberPicker) findViewById(R.id.year);
mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        adjustMaxDay();
        updateDaySpinner();
        notifyDateChanged();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
});

mDay = dayOfMonth;
updateSpinners();
notifyDateChanged();
reorderPickers(new DateFormatSymbols().getShortMonths());

@Override
protected void onShow() {
    super.onShow();
    mDay = dayOfMonth; // Restore state in case of cancellation
    mMonth = month; // Restore state in case of cancellation
    mYear = year; // Restore state in case of cancellation
    updateTitle();
    notifyDateChanged();
    if (mOnDateChangedListener != null) {
        mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
    }
}

private static class SavedState extends BaseSavedState {
    public SavedState(Parcelable superState) {
        super(superState);
    }
}

private void updateTitle() {
    setTitle(String.format("%d/%d/%d", mMonth + 1, mDay, mYear));
}

//<End of snippet n. 0>