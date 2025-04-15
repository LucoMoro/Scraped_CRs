/*NumberPicker: Set formatter local value to US.

As the Arabic language formatter would make number "1,2,3" to Arabic
language word means one,two,three, NumberPicker won't like to show
any thing other than numbers, add local settings to US, make the
number 1,2,3

Change-Id:If2d161ac4133f6c9c6e1ec284d6d2fbf01855fa0*/
//Synthetic comment -- diff --git a/core/java/android/widget/NumberPicker.java b/core/java/android/widget/NumberPicker.java
//Synthetic comment -- index 582d9e4..c87b62a 100644

//Synthetic comment -- @@ -72,7 +72,8 @@
public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER =
new NumberPicker.Formatter() {
final StringBuilder mBuilder = new StringBuilder();
                final java.util.Formatter mFmt = new java.util.Formatter(mBuilder);
final Object[] mArgs = new Object[1];
public String toString(int value) {
mArgs[0] = value;







