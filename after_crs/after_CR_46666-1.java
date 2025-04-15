/*Fix Dec is missing in DatePicker

It is regressed by 21c3897231d5650fe86ad3223d76b3a55411ac64,
We should call setMin/MaxValue() after setDisplayedValues(),
otherwise they will be resetted sometimes.

Change-Id:Idf2f8fc45234666094e08f00e5c94a6f4a199de4Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/contacts/datepicker/DatePicker.java b/src/com/android/contacts/datepicker/DatePicker.java
//Synthetic comment -- index fe23415..597e0a8 100644

//Synthetic comment -- @@ -133,14 +133,13 @@
for (int i = 0; i < months.length; i++) {
months[i] = String.valueOf(i + 1);
}
} else {
mMonthPicker.setDisplayedValues(months);
}

        mMonthPicker.setMinValue(1);
        mMonthPicker.setMaxValue(12);

mMonthPicker.setOnLongPressUpdateInterval(200);
mMonthPicker.setOnValueChangedListener(new OnValueChangeListener() {
@Override







