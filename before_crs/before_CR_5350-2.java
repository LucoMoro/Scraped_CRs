/*Switch long date format to something more human friendly:

      December 09, 2008 --> December 9, 2008
      09 December, 2008 --> 9 December, 2008

This human visible date is prominently displayed on the status bar
when you roll it down. Right now, it says "December 12, 2008" which is
soothing and relaxing. However, a few days ago it said "December 09,
2008" in which  the leading zero caused myself, and presumably others,
a small amount of mental discord.  Put another way, this change will
the end user experience slightly better 30% of the time.*/
//Synthetic comment -- diff --git a/core/java/android/pim/DateFormat.java b/core/java/android/pim/DateFormat.java
//Synthetic comment -- index 802e045..c6173fa 100644

//Synthetic comment -- @@ -228,9 +228,9 @@
public static final java.text.DateFormat getLongDateFormat(Context context) {
String value = getDateFormatString(context);
if (value.indexOf('M') < value.indexOf('d')) {
            value = "MMMM dd, yyyy";
} else {
            value = "dd MMMM, yyyy";
}
return new java.text.SimpleDateFormat(value);
}







