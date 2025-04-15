/*Dont use the default locale when formatting SQL statements

It is not safe to use the default locale when using String.format
to produce SQL statements. Some locales will break the SQL
and as a consequence crash the app.

Change-Id:I302e0f65108d6ef75610361626779fdb8143d4e6*/




//Synthetic comment -- diff --git a/src/com/android/providers/calendar/CalendarAppWidgetService.java b/src/com/android/providers/calendar/CalendarAppWidgetService.java
//Synthetic comment -- index 109f948..2509e2b 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.view.View;
import android.widget.RemoteViews;

import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

//Synthetic comment -- @@ -566,9 +567,9 @@
long end = now + searchDuration;

Uri uri = Uri.withAppendedPath(Instances.CONTENT_URI,
                String.format((Locale)null, "%d/%d", now, end));

        String selection = String.format((Locale)null, "%s=1 AND %s!=%d",
Calendars.SELECTED, Instances.SELF_ATTENDEE_STATUS,
Attendees.ATTENDEE_STATUS_DECLINED);








