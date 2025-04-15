/*September 09, 2008 -> September 9, 2008*/




//Synthetic comment -- diff --git a/core/java/android/pim/DateFormat.java b/core/java/android/pim/DateFormat.java
//Synthetic comment -- index 802e045..c6173fa 100644

//Synthetic comment -- @@ -228,9 +228,9 @@
public static final java.text.DateFormat getLongDateFormat(Context context) {
String value = getDateFormatString(context);
if (value.indexOf('M') < value.indexOf('d')) {
            value = "MMMM d, yyyy";
} else {
            value = "d MMMM, yyyy";
}
return new java.text.SimpleDateFormat(value);
}







