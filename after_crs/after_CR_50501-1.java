/*avoid multiple-"0" to exceed the length of upper bound

Limits number of "0"s not to exceed length of maximum number allowed
for the NumberPicker.

Steps to reproduce:
1. Settings -> Date & time -> uncheck "Automatic date & time"
2. Select "Set time"
3. Select Time Area on Set time dialog
4. insert 00000000 via NumberPicker

Bug: 8073759
Change-Id:I0f3f5303d9a4b559217adb436f244407a23e58c0*/




//Synthetic comment -- diff --git a/core/java/android/widget/NumberPicker.java b/core/java/android/widget/NumberPicker.java
//Synthetic comment -- index 74ded18..2ac5a12 100644

//Synthetic comment -- @@ -1969,8 +1969,10 @@
* Ensure the user can't type in a value greater than the max
* allowed. We have to allow less than min as the user might
* want to delete some numbers and then type a new number.
                 * And prevent multiple-"0" that exceeds the length of upper
                 * bound number.
*/
                if (val > mMaxValue || result.length() > String.valueOf(mMaxValue).length()) {
return "";
} else {
return filtered;







