/*Dont use the default locale when formatting SQL statements

It is not safe to use the default locale when using String.format
to produce SQL statements. Some locales will break the SQL
and as a consequence crash the app.

Change-Id:I302e0f65108d6ef75610361626779fdb8143d4e6*/
//Synthetic comment -- diff --git a/src/com/android/providers/calendar/CalendarAppWidgetService.java b/src/com/android/providers/calendar/CalendarAppWidgetService.java
//Synthetic comment -- index 109f948..287ce3d 100644

//Synthetic comment -- @@ -62,6 +62,9 @@
Instances.EVENT_ID,
};

static final int INDEX_ALL_DAY = 0;
static final int INDEX_BEGIN = 1;
static final int INDEX_END = 2;
//Synthetic comment -- @@ -566,13 +569,8 @@
long end = now + searchDuration;

Uri uri = Uri.withAppendedPath(Instances.CONTENT_URI,
                String.format("%d/%d", now, end));

        String selection = String.format("%s=1 AND %s!=%d",
                Calendars.SELECTED, Instances.SELF_ATTENDEE_STATUS,
                Attendees.ATTENDEE_STATUS_DECLINED);

        return resolver.query(uri, EVENT_PROJECTION, selection, null,
                EVENT_SORT_ORDER);
}
}







