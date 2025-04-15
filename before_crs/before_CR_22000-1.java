/*Use DateUtils.getDayOfWeekString rather then
DateFormatSymbols to avoid Numbers for Locales not supported
by DateFormatSymbols.

Change-Id:Ied49aaf3b3f617b92301922e1bd1511194c0f50e*/
//Synthetic comment -- diff --git a/src/com/android/deskclock/RepeatPreference.java b/src/com/android/deskclock/RepeatPreference.java
//Synthetic comment -- index 61e2af5..cd5060b 100644

//Synthetic comment -- @@ -20,9 +20,9 @@
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class RepeatPreference extends ListPreference {
//Synthetic comment -- @@ -36,15 +36,14 @@
public RepeatPreference(Context context, AttributeSet attrs) {
super(context, attrs);

        String[] weekdays = new DateFormatSymbols().getWeekdays();
String[] values = new String[] {
            weekdays[Calendar.MONDAY],
            weekdays[Calendar.TUESDAY],
            weekdays[Calendar.WEDNESDAY],
            weekdays[Calendar.THURSDAY],
            weekdays[Calendar.FRIDAY],
            weekdays[Calendar.SATURDAY],
            weekdays[Calendar.SUNDAY],
};
setEntries(values);
setEntryValues(values);







