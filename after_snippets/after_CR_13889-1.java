
//<Beginning of snippet n. 0>


mDayPicker.setOnChangeListener(new OnChangedListener() {
public void onChanged(NumberPicker picker, int oldVal, int newVal) {
mDay = newVal;
                notifyDateChanged();
}
});
mMonthPicker = (NumberPicker) findViewById(R.id.month);
mMonth = newVal - 1;
// Adjust max day of the month
adjustMaxDay();
                notifyDateChanged();
updateDaySpinner();
}
});
mYear = newVal;
// Adjust max day for leap years if needed
adjustMaxDay();
                notifyDateChanged();
updateDaySpinner();
}
});
mDay = dayOfMonth;
updateSpinners();
reorderPickers(new DateFormatSymbols().getShortMonths());
        notifyDateChanged();
}

private static class SavedState extends BaseSavedState {
mDay = max;
}
}

    private void notifyDateChanged() {
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
        }
    }
}

//<End of snippet n. 0>








