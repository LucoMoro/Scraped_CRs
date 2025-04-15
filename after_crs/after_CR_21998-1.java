/*Use internal R.string.abbrev_wday_month_day_year
for formating DatePickerDialog Title, rather then
java.text.DateFormat, because it does not support
many locales. Result with DateFormat of the current
date is shown for a lot of locales as
1, 23 3 2011, which actually means
Mon, 23 Mar 2011 and so on.

Change-Id:Ida2a00400e867f8a3fab7402da8aa256dd4ee75d*/




//Synthetic comment -- diff --git a/core/java/android/app/DatePickerDialog.java b/core/java/android/app/DatePickerDialog.java
//Synthetic comment -- index 8ba480d..aeef090 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
private final DatePicker mDatePicker;
private final OnDateSetListener mCallBack;
private final Calendar mCalendar;
    private final String mTitleDateFormat;
private final String[] mWeekDays;

private int mInitialYear;
//Synthetic comment -- @@ -109,8 +109,8 @@
DateFormatSymbols symbols = new DateFormatSymbols();
mWeekDays = symbols.getShortWeekdays();

        mTitleDateFormat =
		context.getString(com.android.internal.R.string.abbrev_wday_month_day_year);
mCalendar = Calendar.getInstance();
updateTitle(mInitialYear, mInitialMonth, mInitialDay);

//Synthetic comment -- @@ -163,7 +163,7 @@
mCalendar.set(Calendar.YEAR, year);
mCalendar.set(Calendar.MONTH, month);
mCalendar.set(Calendar.DAY_OF_MONTH, day);
        setTitle(DateFormat.format(mTitleDateFormat, mCalendar.getTime()));
}

@Override







