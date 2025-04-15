/*Fix for Android issue 1802: DatePickerDialog.updateDate() does not update the title of the dialog.*/




//Synthetic comment -- diff --git a/core/java/android/app/DatePickerDialog.java b/core/java/android/app/DatePickerDialog.java
//Synthetic comment -- index 863cbcc..f339b17 100644

//Synthetic comment -- @@ -157,6 +157,7 @@
mInitialMonth = monthOfYear;
mInitialDay = dayOfMonth;
mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
        updateTitle(year, monthOfYear, dayOfMonth);
}

private void updateTitle(int year, int month, int day) {







