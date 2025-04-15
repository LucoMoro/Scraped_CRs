/*Replaced deprecated calls to setButton and setButton2

Change-Id:Ifcc3f3afc1689406a3298dab7b00a8edb83c115e*/




//Synthetic comment -- diff --git a/core/java/android/app/TimePickerDialog.java b/core/java/android/app/TimePickerDialog.java
//Synthetic comment -- index 002b01f..a04b9e9 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
/**
* A dialog that prompts the user for the time of day using a {@link TimePicker}.
*/
public class TimePickerDialog extends AlertDialog implements OnClickListener,
OnTimeChangedListener {

/**
//Synthetic comment -- @@ -53,12 +53,12 @@
private static final String HOUR = "hour";
private static final String MINUTE = "minute";
private static final String IS_24_HOUR = "is24hour";

private final TimePicker mTimePicker;
private final OnTimeSetListener mCallback;
private final Calendar mCalendar;
private final java.text.DateFormat mDateFormat;

int mInitialHourOfDay;
int mInitialMinute;
boolean mIs24HourView;
//Synthetic comment -- @@ -98,12 +98,13 @@
mDateFormat = DateFormat.getTimeFormat(context);
mCalendar = Calendar.getInstance();
updateTitle(mInitialHourOfDay, mInitialMinute);

        setButton(BUTTON_POSITIVE, context.getText(R.string.date_time_set), this);
        setButton(BUTTON_NEGATIVE, context.getText(R.string.cancel),
                (OnClickListener) null);
setIcon(R.drawable.ic_dialog_time);

        LayoutInflater inflater =
(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
View view = inflater.inflate(R.layout.time_picker_dialog, null);
setView(view);
//Synthetic comment -- @@ -115,11 +116,11 @@
mTimePicker.setIs24HourView(mIs24HourView);
mTimePicker.setOnTimeChangedListener(this);
}

public void onClick(DialogInterface dialog, int which) {
if (mCallback != null) {
mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
mTimePicker.getCurrentMinute());
}
}
//Synthetic comment -- @@ -127,7 +128,7 @@
public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
updateTitle(hourOfDay, minute);
}

public void updateTime(int hourOfDay, int minutOfHour) {
mTimePicker.setCurrentHour(hourOfDay);
mTimePicker.setCurrentMinute(minutOfHour);
//Synthetic comment -- @@ -138,7 +139,7 @@
mCalendar.set(Calendar.MINUTE, minute);
setTitle(mDateFormat.format(mCalendar.getTime()));
}

@Override
public Bundle onSaveInstanceState() {
Bundle state = super.onSaveInstanceState();
//Synthetic comment -- @@ -147,7 +148,7 @@
state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
return state;
}

@Override
public void onRestoreInstanceState(Bundle savedInstanceState) {
super.onRestoreInstanceState(savedInstanceState);







