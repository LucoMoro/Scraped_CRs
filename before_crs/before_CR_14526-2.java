/*Fixed deserialization problem in DatePicker.

During onRestoreInstanceState for DatePicker, the internal
state of the widget is restored properly (thus setting the
internal year, month, and day), but the spinners aren't
visually updated to that state immediately. That is to say,
the internal state of the widget doesn't match the spinners
in that case, which can cause confusion.

Change-Id:I96d1a299d0ee159d41450470acb30a3bf6006d44*/
//Synthetic comment -- diff --git a/core/java/android/widget/DatePicker.java b/core/java/android/widget/DatePicker.java
//Synthetic comment -- index 736475e..ebaf474 100644

//Synthetic comment -- @@ -316,6 +316,7 @@
mYear = ss.getYear();
mMonth = ss.getMonth();
mDay = ss.getDay();
}

/**







