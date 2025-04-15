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
import android.text.format.DateUtils;
import android.util.AttributeSet;

import java.util.Calendar;

public class RepeatPreference extends ListPreference {
//Synthetic comment -- @@ -36,15 +36,14 @@
public RepeatPreference(Context context, AttributeSet attrs) {
super(context, attrs);

String[] values = new String[] {
            DateUtils.getDayOfWeekString(Calendar.MONDAY, DateUtils.LENGTH_LONG),
            DateUtils.getDayOfWeekString(Calendar.TUESDAY, DateUtils.LENGTH_LONG),
            DateUtils.getDayOfWeekString(Calendar.WEDNESDAY, DateUtils.LENGTH_LONG),
            DateUtils.getDayOfWeekString(Calendar.THURSDAY, DateUtils.LENGTH_LONG),
            DateUtils.getDayOfWeekString(Calendar.FRIDAY, DateUtils.LENGTH_LONG),
            DateUtils.getDayOfWeekString(Calendar.SATURDAY, DateUtils.LENGTH_LONG),
            DateUtils.getDayOfWeekString(Calendar.SUNDAY, DateUtils.LENGTH_LONG)
};
setEntries(values);
setEntryValues(values);







