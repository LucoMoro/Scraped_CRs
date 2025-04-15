/*Code cleanup

* Comment out/remove unused variables/debug code
* Use DateUtils.formatDateRange() instead of local
  Utils.formatDateRange()
	- Enables not using deprecated constant DateUtils.FORMAT_UTC

Change-Id:I3ee9f1e703a91f628922a9369cb81aaa7496ade6*/




//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaAdapter.java b/src/com/android/calendar/AgendaAdapter.java
//Synthetic comment -- index 723def0..fc60336 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.calendar;

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
//Synthetic comment -- @@ -23,6 +26,7 @@
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
//Synthetic comment -- @@ -96,18 +100,21 @@
long begin = cursor.getLong(AgendaWindowAdapter.INDEX_BEGIN);
long end = cursor.getLong(AgendaWindowAdapter.INDEX_END);
boolean allDay = cursor.getInt(AgendaWindowAdapter.INDEX_ALL_DAY) != 0;
        int flags = 0;
String whenString;
        String tz;
if (allDay) {
            tz = Time.TIMEZONE_UTC;
} else {
flags = DateUtils.FORMAT_SHOW_TIME;
            tz = Utils.getTimeZone(context, null);
}
if (DateFormat.is24HourFormat(context)) {
flags |= DateUtils.FORMAT_24HOUR;
}

        Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
        whenString = DateUtils.formatDateRange(context, f, begin, end, flags, tz).toString();
when.setText(whenString);

String rrule = cursor.getString(AgendaWindowAdapter.INDEX_RRULE);








//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaListView.java b/src/com/android/calendar/AgendaListView.java
//Synthetic comment -- index 3b9593c..05663d4 100644

//Synthetic comment -- @@ -74,7 +74,6 @@
@Override
public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
int titlePosition = 0 ;
// find the index of the item with day title
for (titlePosition = getSelectedItemPosition() - 1; titlePosition >= 0; titlePosition--) {
Object previousItem = mWindowAdapter.getItem(titlePosition);








//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaWindowAdapter.java b/src/com/android/calendar/AgendaWindowAdapter.java
//Synthetic comment -- index ff74a12..db1b52e 100644

//Synthetic comment -- @@ -715,10 +715,10 @@

if (cursorSize != 0) {
// Remove the query that just completed
                    /* QuerySpec x = mQueryQueue.poll();
if (BASICLOG && !x.equals(data)) {
Log.e(TAG, "onQueryComplete - cookie != head of queue");
                    } */
mEmptyCursorCount = 0;
if (data.queryType == QUERY_TYPE_NEWER) {
mNewerRequestsProcessed++;
//Synthetic comment -- @@ -833,11 +833,11 @@
|| data.end <= mAdapterInfos.getFirst().start) {
mAdapterInfos.addFirst(info);
listPositionOffset += info.size;
                /* } else if (BASICLOG && data.start < mAdapterInfos.getLast().end) {
mAdapterInfos.addLast(info);
for (DayAdapterInfo info2 : mAdapterInfos) {
Log.e("========== BUG ==", info2.toString());
                    } */
} else {
mAdapterInfos.addLast(info);
}








//Synthetic comment -- diff --git a/src/com/android/calendar/AlertAdapter.java b/src/com/android/calendar/AlertAdapter.java
//Synthetic comment -- index 839f22a..8eec626 100644

//Synthetic comment -- @@ -16,12 +16,16 @@

package com.android.calendar;

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
//Synthetic comment -- @@ -84,17 +88,20 @@

// When
String when;
        int flags = DateUtils.FORMAT_SHOW_DATE;
        String tz;
if (allDay) {
            flags |= DateUtils.FORMAT_SHOW_WEEKDAY;
            tz = Time.TIMEZONE_UTC;
} else {
            flags |= DateUtils.FORMAT_SHOW_TIME;
            tz = Utils.getTimeZone(context, null);
}
if (DateFormat.is24HourFormat(context)) {
flags |= DateUtils.FORMAT_24HOUR;
}
        Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
        when = DateUtils.formatDateRange(context, f, startMillis, endMillis, flags, tz).toString();
textView = (TextView) view.findViewById(R.id.when);
textView.setText(when);









//Synthetic comment -- diff --git a/src/com/android/calendar/AlertService.java b/src/com/android/calendar/AlertService.java
//Synthetic comment -- index 7bac553..fd417d7 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
private static final int ALERT_INDEX_TITLE = 3;
private static final int ALERT_INDEX_EVENT_LOCATION = 4;
private static final int ALERT_INDEX_SELF_ATTENDEE_STATUS = 5;
    // private static final int ALERT_INDEX_ALL_DAY = 6;
private static final int ALERT_INDEX_ALARM_TIME = 7;
private static final int ALERT_INDEX_MINUTES = 8;
private static final int ALERT_INDEX_BEGIN = 9;








//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 9e40558..6bca20c 100644

//Synthetic comment -- @@ -47,7 +47,6 @@
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
//Synthetic comment -- @@ -67,6 +66,7 @@

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -276,7 +276,6 @@
/* package */ static long EVENT_OVERWRAP_MARGIN_TIME = MILLIS_PER_MINUTE * 15;

private static int mSelectionColor;
private static int mSelectedEventTextColor;
private static int mEventTextColor;
private static int mWeek_saturdayColor;
//Synthetic comment -- @@ -467,7 +466,6 @@
mCalendarHourLabel = mResources.getColor(R.color.calendar_hour_label);
mCalendarHourSelected = mResources.getColor(R.color.calendar_hour_selected);
mSelectionColor = mResources.getColor(R.color.selection);
mSelectedEventTextColor = mResources.getColor(R.color.calendar_event_selected_text_color);
mEventTextColor = mResources.getColor(R.color.calendar_event_text_color);
mCurrentTimeMarkerColor = mResources.getColor(R.color.current_time_marker);
//Synthetic comment -- @@ -697,7 +695,6 @@
}
start = System.currentTimeMillis();

boolean isDST = mBaseDate.isDst != 0;
StringBuilder title = new StringBuilder(mDateRange);
title.append(" (").append(Utils.formatDateRange(mContext, start, start, flags))
//Synthetic comment -- @@ -2224,7 +2221,7 @@
if (bottom > box.bottom) {
bottom = box.bottom;
}
            /* if (false) {
int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL
| DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
if (DateFormat.is24HourFormat(mParentActivity)) {
//Synthetic comment -- @@ -2234,7 +2231,7 @@
ev.startMillis, ev.endMillis, flags);
Log.i("Cal", "left: " + left + " right: " + right + " top: " + top
+ " bottom: " + bottom + " ev: " + timeRange + " " + ev.title);
            } */
int upDistanceMin = 10000;     // any large number
int downDistanceMin = 10000;   // any large number
int leftDistanceMin = 10000;   // any large number
//Synthetic comment -- @@ -2600,20 +2597,25 @@
imageView = (ImageView) mPopupView.findViewById(R.id.repeat_icon);
imageView.setVisibility(event.isRepeating ? View.VISIBLE : View.GONE);

        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
            | DateUtils.FORMAT_ABBREV_ALL;

        String tz;
if (event.allDay) {
            tz = Time.TIMEZONE_UTC;
} else {
            tz = Utils.getTimeZone(mParentActivity, null);
            flags |= DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
}

if (DateFormat.is24HourFormat(mParentActivity)) {
flags |= DateUtils.FORMAT_24HOUR;
}

        Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
        String timeRange = DateUtils.formatDateRange(mParentActivity, f, event.startMillis,
                event.endMillis, flags, tz).toString();

TextView timeView = (TextView) mPopupView.findViewById(R.id.time);
timeView.setText(timeRange);









//Synthetic comment -- diff --git a/src/com/android/calendar/Event.java b/src/com/android/calendar/Event.java
//Synthetic comment -- index a989414..3bfbba2 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
private static final int PROJECTION_LOCATION_INDEX = 1;
private static final int PROJECTION_ALL_DAY_INDEX = 2;
private static final int PROJECTION_COLOR_INDEX = 3;
    // private static final int PROJECTION_TIMEZONE_INDEX = 4;
private static final int PROJECTION_EVENT_ID_INDEX = 5;
private static final int PROJECTION_BEGIN_INDEX = 6;
private static final int PROJECTION_END_INDEX = 7;
//Synthetic comment -- @@ -421,13 +421,13 @@
continue;

long start = event.getStartMillis();
            /* if (event.allDay) {
Event e = event;
Log.i("Cal", "event start,end day: " + e.startDay + "," + e.endDay
+ " start,end time: " + e.startTime + "," + e.endTime
+ " start,end millis: " + e.getStartMillis() + "," + e.getEndMillis()
+ " "  + e.title);
            } */

// Remove the inactive events.
// An event on the active list becomes inactive when its end time + margin time is less
//Synthetic comment -- @@ -439,13 +439,13 @@
final long duration = Math.max(active.getEndMillis() - active.getStartMillis(),
CalendarView.EVENT_OVERWRAP_MARGIN_TIME);
if ((active.getStartMillis() + duration) <= start) {
                    /* if (event.allDay) {
Event e = active;
Log.i("Cal", "  removing: start,end day: " + e.startDay + "," + e.endDay
+ " start,end time: " + e.startTime + "," + e.endTime
+ " start,end millis: " + e.getStartMillis() + "," + e.getEndMillis()
+ " "  + e.title);
                    } */
colMask &= ~(1L << active.getColumn());
iter.remove();
}








//Synthetic comment -- diff --git a/src/com/android/calendar/EventInfoActivity.java b/src/com/android/calendar/EventInfoActivity.java
//Synthetic comment -- index 62dfae2..a09ac0e 100644

//Synthetic comment -- @@ -78,7 +78,9 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -131,7 +133,7 @@
private static final int EVENT_INDEX_COLOR = 12;
private static final int EVENT_INDEX_HAS_ATTENDEE_DATA = 13;
private static final int EVENT_INDEX_GUESTS_CAN_MODIFY = 14;
    // private static final int EVENT_INDEX_CAN_INVITE_OTHERS = 15;
private static final int EVENT_INDEX_ORGANIZER = 16;

private static final String[] ATTENDEES_PROJECTION = new String[] {
//Synthetic comment -- @@ -807,7 +809,6 @@
values).build());

if (mHasAttendeeData) {
// Insert the new attendees
for (Attendee attendee : mAcceptedAttendees) {
addAttendee(values, ops, eventIdIndex, attendee,
//Synthetic comment -- @@ -919,23 +920,23 @@

// When
String when;
        int flags = DateUtils.FORMAT_SHOW_DATE;
        String localTimezone;
if (allDay) {
            flags |= DateUtils.FORMAT_SHOW_WEEKDAY;
            localTimezone = Time.TIMEZONE_UTC;
} else {
            flags |= DateUtils.FORMAT_SHOW_TIME;
            localTimezone = Utils.getTimeZone(this, mUpdateTZ);
if (DateFormat.is24HourFormat(this)) {
flags |= DateUtils.FORMAT_24HOUR;
}
}

        Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
        when = DateUtils.formatDateRange(this, f, mStartMillis, mEndMillis, flags, localTimezone).toString();
setTextCommon(R.id.when, when);

if (eventTimezone != null && !allDay &&
(!TextUtils.equals(localTimezone, eventTimezone) ||
!TextUtils.equals(localTimezone, Time.getCurrentTimezone()))) {








//Synthetic comment -- diff --git a/src/com/android/calendar/MonthView.java b/src/com/android/calendar/MonthView.java
//Synthetic comment -- index afee98b..e18add2 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
//Synthetic comment -- @@ -54,6 +53,8 @@

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class MonthView extends View implements View.OnCreateContextMenuListener {

//Synthetic comment -- @@ -1082,30 +1083,32 @@

int flags;
boolean showEndTime = false;
            String tz;
if (event.allDay) {
int numDays = event.endDay - event.startDay;
                flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL;
                tz = Time.TIMEZONE_UTC;
if (numDays == 0) {
                    flags |= DateUtils.FORMAT_SHOW_WEEKDAY;
} else {
showEndTime = true;
}
} else {
flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_CAP_NOON_MIDNIGHT;
                tz = Utils.getTimeZone(mParentActivity, null);
if (DateFormat.is24HourFormat(mParentActivity)) {
flags |= DateUtils.FORMAT_24HOUR;
}
}

            Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
String timeRange;
if (showEndTime) {
                timeRange = DateUtils.formatDateRange(mParentActivity, f,
                        event.startMillis, event.endMillis, flags, tz).toString();
} else {
                timeRange = DateUtils.formatDateRange(mParentActivity, f,
                        event.startMillis, event.startMillis, flags, tz).toString();
}

TextView timeView = null;








//Synthetic comment -- diff --git a/src/com/android/calendar/SelectCalendarsAdapter.java b/src/com/android/calendar/SelectCalendarsAdapter.java
//Synthetic comment -- index 03426b7..ddcfa20 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
};
//Keep these in sync with the projection
private static final int ID_COLUMN = 0;
    // private static final int ACCOUNT_COLUMN = 1;
private static final int OWNER_COLUMN = 2;
private static final int NAME_COLUMN = 3;
private static final int COLOR_COLUMN = 4;
//Synthetic comment -- @@ -177,7 +177,6 @@
public void onClick(View v) {
View view = (View)v.getTag();
long id = (Long)view.getTag();
String status = syncedNotVisible;
Boolean[] change;
Boolean[] initialState = mCalendarInitialStates.get(id);








//Synthetic comment -- diff --git a/src/com/android/calendar/TimezoneAdapter.java b/src/com/android/calendar/TimezoneAdapter.java
//Synthetic comment -- index f59885c..cff3ee6 100644

//Synthetic comment -- @@ -43,7 +43,7 @@
*/
public class TimezoneAdapter extends ArrayAdapter<TimezoneRow> {
private static final String TAG = "TimezoneAdapter";
    // private static final boolean DEBUG = true;

/**
* {@link TimezoneRow} is an immutable class for representing a timezone. We








//Synthetic comment -- diff --git a/src/com/android/calendar/Utils.java b/src/com/android/calendar/Utils.java
//Synthetic comment -- index 4a3a37f..79a6aab 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
import java.util.Map;

public class Utils {
    // private static final boolean DEBUG = true;
    // private static final String TAG = "CalUtils";
private static final int CLEAR_ALPHA_MASK = 0x00FFFFFF;
private static final int HIGH_ALPHA = 255 << 24;
private static final int MED_ALPHA = 180 << 24;







