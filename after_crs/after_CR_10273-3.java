/*Fix for Android issue 1802: DatePickerDialog.updateDate() does not update the title of the dialog.*/




//Synthetic comment -- diff --git a/core/java/android/app/DatePickerDialog.java b/core/java/android/app/DatePickerDialog.java
//Synthetic comment -- index 78bbb4f4..59b9ce4 100644

//Synthetic comment -- @@ -155,6 +155,7 @@
mInitialMonth = monthOfYear;
mInitialDay = dayOfMonth;
mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
        updateTitle(year, monthOfYear, dayOfMonth);
}

private void updateTitle(int year, int month, int day) {








//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/NumberPicker.java b/core/java/com/android/internal/widget/NumberPicker.java
//Synthetic comment -- index 0424ced..c484326 100644

//Synthetic comment -- @@ -68,10 +68,10 @@
private final Runnable mRunnable = new Runnable() {
public void run() {
if (mIncrement) {
                setCurrent(mCurrent + 1);
mHandler.postDelayed(this, mSpeed);
} else if (mDecrement) {
                setCurrent(mCurrent - 1);
mHandler.postDelayed(this, mSpeed);
}
}
//Synthetic comment -- @@ -175,11 +175,6 @@
updateView();
}

/**
* The speed (in milliseconds) at which the numbers will scroll
* when the the +/- buttons are longpressed. Default is 300ms.
//Synthetic comment -- @@ -194,9 +189,9 @@

// now perform the increment/decrement
if (R.id.increment == v.getId()) {
            setCurrent(mCurrent + 1);
} else if (R.id.decrement == v.getId()) {
            setCurrent(mCurrent - 1);
}
}

//Synthetic comment -- @@ -206,7 +201,7 @@
: String.valueOf(value);
}

    public void setCurrent(int current) {

// Wrap around the values if we go past the start or end
if (current > mEnd) {







