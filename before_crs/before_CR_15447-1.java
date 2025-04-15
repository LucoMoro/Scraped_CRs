/*Replaced deprecated setButton Methodcalls

Change-Id:I8358b2dce25c6d5024550c54d98b4891f41b2a40*/
//Synthetic comment -- diff --git a/core/java/android/app/DatePickerDialog.java b/core/java/android/app/DatePickerDialog.java
//Synthetic comment -- index 78bbb4f4..f780e1d 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
//Synthetic comment -- @@ -36,13 +35,13 @@
/**
* A simple dialog containing an {@link android.widget.DatePicker}.
*/
public class DatePickerDialog extends AlertDialog implements OnClickListener, 
OnDateChangedListener {

private static final String YEAR = "year";
private static final String MONTH = "month";
private static final String DAY = "day";
    
private final DatePicker mDatePicker;
private final OnDateSetListener mCallBack;
private final Calendar mCalendar;
//Synthetic comment -- @@ -80,7 +79,7 @@
int year,
int monthOfYear,
int dayOfMonth) {
        this(context, com.android.internal.R.style.Theme_Dialog_Alert, 
callBack, year, monthOfYear, dayOfMonth);
}

//Synthetic comment -- @@ -106,17 +105,17 @@
mInitialDay = dayOfMonth;
DateFormatSymbols symbols = new DateFormatSymbols();
mWeekDays = symbols.getShortWeekdays();
        
mTitleDateFormat = java.text.DateFormat.
getDateInstance(java.text.DateFormat.FULL);
mCalendar = Calendar.getInstance();
updateTitle(mInitialYear, mInitialMonth, mInitialDay);
        
        setButton(context.getText(R.string.date_time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
setIcon(R.drawable.ic_dialog_time);
        
        LayoutInflater inflater = 
(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
View view = inflater.inflate(R.layout.date_picker_dialog, null);
setView(view);
//Synthetic comment -- @@ -136,20 +135,20 @@
title.setSingleLine();
title.setEllipsize(TruncateAt.END);
}
    
public void onClick(DialogInterface dialog, int which) {
if (mCallBack != null) {
mDatePicker.clearFocus();
            mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(), 
mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
}
}
    
public void onDateChanged(DatePicker view, int year,
int month, int day) {
updateTitle(year, month, day);
}
    
public void updateDate(int year, int monthOfYear, int dayOfMonth) {
mInitialYear = year;
mInitialMonth = monthOfYear;
//Synthetic comment -- @@ -163,7 +162,7 @@
mCalendar.set(Calendar.DAY_OF_MONTH, day);
setTitle(mTitleDateFormat.format(mCalendar.getTime()));
}
    
@Override
public Bundle onSaveInstanceState() {
Bundle state = super.onSaveInstanceState();
//Synthetic comment -- @@ -172,7 +171,7 @@
state.putInt(DAY, mDatePicker.getDayOfMonth());
return state;
}
    
@Override
public void onRestoreInstanceState(Bundle savedInstanceState) {
super.onRestoreInstanceState(savedInstanceState);







