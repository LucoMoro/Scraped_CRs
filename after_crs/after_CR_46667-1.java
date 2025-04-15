/*Fix Dec is missing in Contact DatePicker

Adjust min and max values if they are more than the length
of displayedValues

Change-Id:Ib778f8f855d2a1aa9b74cac928e0ae7e94642dd4Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/NumberPicker.java b/core/java/android/widget/NumberPicker.java
//Synthetic comment -- index 6cfeb15..5f7e0f4 100644

//Synthetic comment -- @@ -1393,11 +1393,11 @@
| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
// Make sure the min, max, respect the size of the displayed
// values. This will take care of the current value as well.
            if (getMinValue() > displayedValues.length) {
                setMinValue(displayedValues.length);
}
            if (getMaxValue() > displayedValues.length) {
                setMaxValue(displayedValues.length);
}
} else {
mInputText.setRawInputType(InputType.TYPE_CLASS_NUMBER);







