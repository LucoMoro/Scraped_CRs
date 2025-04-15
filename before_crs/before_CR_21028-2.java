/*Fixing crash when query events in Arabic locale

It is not safe to use the default locale when using String.format
to produce SQL statements. Some locales will break the SQL
and as a consequence crash the app. Change the code so it is
using selection args instead.

Change-Id:If1b06fec2d87d91a798e530b17abf85c123630cd*/
//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 9e40558..a210179 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
};
private static final int CALENDARS_INDEX_ACCESS_LEVEL = 1;
private static final int CALENDARS_INDEX_OWNER_ACCOUNT = 2;
    private static final String CALENDARS_WHERE = Calendars._ID + "=%d";

private static float SMALL_ROUND_RADIUS = 3.0F;

//Synthetic comment -- @@ -3085,8 +3085,8 @@
cursor.close();

Uri uri = Calendars.CONTENT_URI;
        String where = String.format(CALENDARS_WHERE, calId);
        cursor = cr.query(uri, CALENDARS_PROJECTION, where, null, null);

String calendarOwnerAccount = null;
if (cursor != null) {








//Synthetic comment -- diff --git a/src/com/android/calendar/EditEvent.java b/src/com/android/calendar/EditEvent.java
//Synthetic comment -- index 31b6059..0b97f24 100644

//Synthetic comment -- @@ -170,7 +170,7 @@
Reminders.MINUTES,  // 1
};
private static final int REMINDERS_INDEX_MINUTES = 1;
    private static final String REMINDERS_WHERE = Reminders.EVENT_ID + "=%d AND (" +
Reminders.METHOD + "=" + Reminders.METHOD_ALERT + " OR " + Reminders.METHOD + "=" +
Reminders.METHOD_DEFAULT + ")";

//Synthetic comment -- @@ -851,8 +851,9 @@
&& (mEventCursor.getInt(EVENT_INDEX_HAS_ALARM) != 0);
if (hasAlarm) {
Uri uri = Reminders.CONTENT_URI;
            String where = String.format(REMINDERS_WHERE, eventId);
            Cursor reminderCursor = cr.query(uri, REMINDERS_PROJECTION, where, null, null);
try {
// First pass: collect all the custom reminder minutes (e.g.,
// a reminder of 8 minutes) into a global list.








//Synthetic comment -- diff --git a/src/com/android/calendar/EventInfoActivity.java b/src/com/android/calendar/EventInfoActivity.java
//Synthetic comment -- index 50652b6..5723012 100644

//Synthetic comment -- @@ -147,7 +147,7 @@
private static final int ATTENDEES_INDEX_RELATIONSHIP = 3;
private static final int ATTENDEES_INDEX_STATUS = 4;

    private static final String ATTENDEES_WHERE = Attendees.EVENT_ID + "=%d";

private static final String ATTENDEES_SORT_ORDER = Attendees.ATTENDEE_NAME + " ASC, "
+ Attendees.ATTENDEE_EMAIL + " ASC";
//Synthetic comment -- @@ -162,7 +162,7 @@
static final int CALENDARS_INDEX_OWNER_ACCOUNT = 2;
static final int CALENDARS_INDEX_OWNER_CAN_RESPOND = 3;

    static final String CALENDARS_WHERE = Calendars._ID + "=%d";
static final String CALENDARS_DUPLICATE_NAME_WHERE = Calendars.DISPLAY_NAME + "=?";

private static final String[] REMINDERS_PROJECTION = new String[] {
//Synthetic comment -- @@ -170,7 +170,7 @@
Reminders.MINUTES,  // 1
};
private static final int REMINDERS_INDEX_MINUTES = 1;
    private static final String REMINDERS_WHERE = Reminders.EVENT_ID + "=%d AND (" +
Reminders.METHOD + "=" + Reminders.METHOD_ALERT + " OR " + Reminders.METHOD + "=" +
Reminders.METHOD_DEFAULT + ")";
private static final String REMINDERS_SORT = Reminders.MINUTES;
//Synthetic comment -- @@ -347,8 +347,11 @@

// Calendars cursor
Uri uri = Calendars.CONTENT_URI;
        String where = String.format(CALENDARS_WHERE, mEventCursor.getLong(EVENT_INDEX_CALENDAR_ID));
        mCalendarsCursor = managedQuery(uri, CALENDARS_PROJECTION, where, null, null);
mCalendarOwnerAccount = "";
if (mCalendarsCursor != null) {
mCalendarsCursor.moveToFirst();
//Synthetic comment -- @@ -367,8 +370,8 @@

// Attendees cursor
uri = Attendees.CONTENT_URI;
        where = String.format(ATTENDEES_WHERE, mEventId);
        mAttendeesCursor = managedQuery(uri, ATTENDEES_PROJECTION, where, null,
ATTENDEES_SORT_ORDER);
initAttendeesCursor();

//Synthetic comment -- @@ -402,9 +405,8 @@
boolean hasAlarm = mEventCursor.getInt(EVENT_INDEX_HAS_ALARM) != 0;
if (hasAlarm) {
uri = Reminders.CONTENT_URI;
            where = String.format(REMINDERS_WHERE, mEventId);
            Cursor reminderCursor = cr.query(uri, REMINDERS_PROJECTION, where, null,
                    REMINDERS_SORT);
try {
// First pass: collect all the custom reminder minutes (e.g.,
// a reminder of 8 minutes) into a global list.







