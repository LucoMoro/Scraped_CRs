//<Beginning of snippet n. 0>

mDayPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mDay = newVal;
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
        updateTitle();
    }
});

mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonthPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mMonth = newVal - 1;
        adjustMaxDay();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
        updateTitle();
        updateDaySpinner();
    }
});

mYearPicker = (NumberPicker) findViewById(R.id.year);
mYearPicker.setOnChangeListener(new OnChangedListener() {
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        mYear = newVal;
        adjustMaxDay();
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
        updateTitle();
        updateDaySpinner();
    }
});

mDay = dayOfMonth;
updateSpinners();
reorderPickers(new DateFormatSymbols().getShortMonths());

private void updateTitle() {
    setTitle(String.format("%d/%d/%d", mMonth + 1, mDay, mYear));
}

private void saveState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.mDay = mDay;
    savedState.mMonth = mMonth;
    savedState.mYear = mYear;
    // Store savedState as needed
}

private static class SavedState extends BaseSavedState {
    int mDay, mMonth, mYear;
    
    SavedState(Parcelable superState) {
        super(superState);
    }

    SavedState(Parcel in) {
        super(in);
        mDay = in.readInt();
        mMonth = in.readInt();
        mYear = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(mDay);
        out.writeInt(mMonth);
        out.writeInt(mYear);
    }
}

//<End of snippet n. 0>