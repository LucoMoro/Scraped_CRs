/*code cleanup:
	- removed unused import statements.
        - removed unused local variables
        - removed unused private methods and..

Change-Id:If0270c7bbedc9d84ec67624d4fcef9b7ea465148*/




//Synthetic comment -- diff --git a/src/com/android/calendar/AlertActivity.java b/src/com/android/calendar/AlertActivity.java
//Synthetic comment -- index 1e35c50..df73d5c 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;








//Synthetic comment -- diff --git a/src/com/android/calendar/AlertService.java b/src/com/android/calendar/AlertService.java
//Synthetic comment -- index 73c1831..ee6a897 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Calendar.Attendees;
import android.provider.Calendar.CalendarAlerts;
import android.text.TextUtils;
//Synthetic comment -- @@ -89,7 +88,6 @@

private static final String ACTIVE_ALERTS_SORT = "begin DESC, end DESC";

void processMessage(Message msg) {
Bundle bundle = (Bundle) msg.obj;

//Synthetic comment -- @@ -153,7 +151,6 @@
final int minutes = alertCursor.getInt(ALERT_INDEX_MINUTES);
final String eventName = alertCursor.getString(ALERT_INDEX_TITLE);
final String location = alertCursor.getString(ALERT_INDEX_EVENT_LOCATION);
final int status = alertCursor.getInt(ALERT_INDEX_SELF_ATTENDEE_STATUS);
final boolean declined = status == Attendees.ATTENDEE_STATUS_DECLINED;
final long beginTime = alertCursor.getLong(ALERT_INDEX_BEGIN);








//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarApplication.java b/src/com/android/calendar/CalendarApplication.java
//Synthetic comment -- index 129b7c2..002f051 100644

//Synthetic comment -- @@ -17,8 +17,6 @@
package com.android.calendar;

import android.app.Application;

public class CalendarApplication extends Application {









//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarPreferenceActivity.java b/src/com/android/calendar/CalendarPreferenceActivity.java
//Synthetic comment -- index e2f84e3..a02a1d5 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;








//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 1a2e787..68d07ab 100644

//Synthetic comment -- @@ -100,13 +100,6 @@
private static final int CALENDARS_INDEX_OWNER_ACCOUNT = 2;
private static final String CALENDARS_WHERE = Calendars._ID + "=%d";

private static float SMALL_ROUND_RADIUS = 3.0F;

private static final int FROM_NONE = 0;
//Synthetic comment -- @@ -2860,7 +2853,6 @@
ContentResolver cr = context.getContentResolver();

int visibility = Calendars.NO_ACCESS;

// Get the calendar id for this event
Cursor cursor = cr.query(ContentUris.withAppendedId(Events.CONTENT_URI, e.id),








//Synthetic comment -- diff --git a/src/com/android/calendar/ContactsAsyncHelper.java b/src/com/android/calendar/ContactsAsyncHelper.java
//Synthetic comment -- index dfccd37..157cc34 100644

//Synthetic comment -- @@ -57,11 +57,6 @@

// static objects
private static Handler sThreadHandler;

private static final class WorkerArgs {
public Context context;
//Synthetic comment -- @@ -183,14 +178,12 @@
WorkerArgs args = (WorkerArgs) msg.obj;
switch (msg.arg1) {
case EVENT_LOAD_IMAGE:

// if the image has been loaded then display it, otherwise set default.
// in either case, make sure the image is visible.
if (args.result != null) {
args.view.setVisibility(View.VISIBLE);
args.view.setImageDrawable((Drawable) args.result);
} else if (args.defaultResource != -1) {
args.view.setVisibility(View.VISIBLE);
args.view.setImageResource(args.defaultResource);








//Synthetic comment -- diff --git a/src/com/android/calendar/DismissAllAlarmsService.java b/src/com/android/calendar/DismissAllAlarmsService.java
//Synthetic comment -- index fee76e0..8e68d56 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
return null;
}

@Override
public void onHandleIntent(Intent intent) {
// Mark all fired alarms as dismissed








//Synthetic comment -- diff --git a/src/com/android/calendar/EditEvent.java b/src/com/android/calendar/EditEvent.java
//Synthetic comment -- index 1f646b5..0bb180c 100644

//Synthetic comment -- @@ -85,7 +85,6 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;








//Synthetic comment -- diff --git a/src/com/android/calendar/Event.java b/src/com/android/calendar/Event.java
//Synthetic comment -- index 32a6131..d931b99 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Debug;
import android.provider.Calendar.Attendees;
import android.provider.Calendar.Events;
import android.provider.Calendar.Instances;
//Synthetic comment -- @@ -101,7 +100,7 @@

public boolean hasAlarm;
public boolean isRepeating;

public int selfAttendeeStatus;

// The coordinates of the event rectangle drawn on the screen.
//Synthetic comment -- @@ -334,8 +333,6 @@
e.organizer = c.getString(PROJECTION_ORGANIZER_INDEX);
e.guestsCanModify = c.getInt(PROJECTION_GUESTS_CAN_INVITE_OTHERS_INDEX) != 0;

if (e.title == null || e.title.length() == 0) {
e.title = res.getString(R.string.no_title_label);
}
//Synthetic comment -- @@ -372,7 +369,7 @@
} else {
e.isRepeating = false;
}

e.selfAttendeeStatus = c.getInt(PROJECTION_SELF_ATTENDEE_STATUS_INDEX);

events.add(e);
//Synthetic comment -- @@ -488,63 +485,6 @@
return 64;
}

public final void dump() {
Log.e("Cal", "+-----------------------------------------+");
Log.e("Cal", "+        id = " + id);








//Synthetic comment -- diff --git a/src/com/android/calendar/EventInfoActivity.java b/src/com/android/calendar/EventInfoActivity.java
//Synthetic comment -- index 296359e..1eed0f8 100644

//Synthetic comment -- @@ -828,7 +828,6 @@
String location = mEventCursor.getString(EVENT_INDEX_EVENT_LOCATION);
String description = mEventCursor.getString(EVENT_INDEX_DESCRIPTION);
String rRule = mEventCursor.getString(EVENT_INDEX_RRULE);
String eventTimezone = mEventCursor.getString(EVENT_INDEX_EVENT_TIMEZONE);
mColor = mEventCursor.getInt(EVENT_INDEX_COLOR) & 0xbbffffff;









//Synthetic comment -- diff --git a/src/com/android/calendar/LaunchActivity.java b/src/com/android/calendar/LaunchActivity.java
//Synthetic comment -- index 5dcb4f4..ea11051 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import android.provider.Settings;

public class LaunchActivity extends Activity {

static final String KEY_DETAIL_VIEW = "DETAIL_VIEW";
static final String KEY_VIEW_TYPE = "VIEW";








//Synthetic comment -- diff --git a/src/com/android/calendar/MonthActivity.java b/src/com/android/calendar/MonthActivity.java
//Synthetic comment -- index ec7c670..5da1cc1 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Calendar.Events;
import android.text.format.DateUtils;
import android.text.format.Time;








//Synthetic comment -- diff --git a/src/com/android/calendar/MonthView.java b/src/com/android/calendar/MonthView.java
//Synthetic comment -- index 712a788..3ff7e4e 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
//Synthetic comment -- @@ -34,12 +33,9 @@
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.GestureDetector;
//Synthetic comment -- @@ -59,8 +55,6 @@

public class MonthView extends View implements View.OnCreateContextMenuListener {

private static float mScale = 0; // Used for supporting different screen densities
private static int WEEK_GAP = 0;
private static int MONTH_DAY_GAP = 1;
//Synthetic comment -- @@ -105,7 +99,6 @@
private Drawable mBoxSelected;
private Drawable mBoxPressed;
private Drawable mBoxLongPressed;
private int mCellWidth;

private Resources mResources;
//Synthetic comment -- @@ -154,9 +147,6 @@
private static final int SELECTION_SELECTED = 2;
private static final int SELECTION_LONGPRESS = 3;

private int mSelectionMode = SELECTION_HIDDEN;

/**
//Synthetic comment -- @@ -241,7 +231,6 @@
mBoxPressed = mResources.getDrawable(R.drawable.month_view_pressed);
mBoxLongPressed = mResources.getDrawable(R.drawable.month_view_longpress);

mTodayBackground = mResources.getDrawable(R.drawable.month_view_today_background);

// Cache color lookups
//Synthetic comment -- @@ -449,17 +438,9 @@
monthStart.minute = 0;
monthStart.second = 0;
long millis = monthStart.normalize(true /* ignore isDst */);

// Load the days with events in the background
mParentActivity.startProgressSpinner();

final ArrayList<Event> events = new ArrayList<Event>();
mEventLoader.loadEventsInBackground(EVENT_NUM_DAYS, events, millis, new Runnable() {
//Synthetic comment -- @@ -621,25 +602,6 @@
}

/**
* Clears the bitmap cache. Generally only needed when the screen size changed.
*/
private void clearBitmapCache() {
//Synthetic comment -- @@ -844,9 +806,6 @@
int top = rect.top + TEXT_TOP_MARGIN + BUSY_BITS_MARGIN;
int left = rect.right - BUSY_BITS_MARGIN - BUSY_BITS_WIDTH;

ArrayList<Event> events = mEvents;
int numEvents = events.size();
EventGeometry geometry = mEventGeometry;
//Synthetic comment -- @@ -893,16 +852,6 @@
return rf;
}

private int getWeekOfYear(int row, int column, boolean isWithinCurrentMonth,
Calendar calendar) {
calendar.set(Calendar.DAY_OF_MONTH, mCursor.getDayAt(row, column));








//Synthetic comment -- diff --git a/src/com/android/calendar/MultiStateButton.java b/src/com/android/calendar/MultiStateButton.java
//Synthetic comment -- index 7b2a5dd..8034b28 100644

//Synthetic comment -- @@ -16,7 +16,6 @@
package com.android.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
//Synthetic comment -- @@ -50,7 +49,6 @@
//A list of all drawable resources used by this button in the order it uses them.
private int[] mButtonResources;
private Drawable mButtonDrawable;

public MultiStateButton(Context context) {
this(context, null);








//Synthetic comment -- diff --git a/src/com/android/calendar/SelectCalendarsActivity.java b/src/com/android/calendar/SelectCalendarsActivity.java
//Synthetic comment -- index 8468d58..a8ed401 100644

//Synthetic comment -- @@ -20,13 +20,9 @@
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.Calendar.Calendars;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
//Synthetic comment -- @@ -36,7 +32,6 @@
public class SelectCalendarsActivity extends ExpandableListActivity
implements AdapterView.OnItemClickListener, View.OnClickListener {

private static final String EXPANDED_KEY = "is_expanded";
private View mView = null;
private Cursor mCursor = null;








//Synthetic comment -- diff --git a/src/com/android/calendar/SelectCalendarsAdapter.java b/src/com/android/calendar/SelectCalendarsAdapter.java
//Synthetic comment -- index c2c21ac..03426b7 100644

//Synthetic comment -- @@ -121,7 +121,6 @@
private static final int COLOR_COLUMN = 4;
private static final int SELECTED_COLUMN = 5;
private static final int SYNCED_COLUMN = 6;

private class AsyncCalendarsUpdater extends AsyncQueryHandler {

//Synthetic comment -- @@ -304,10 +303,8 @@

@Override
protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
String status = notSyncedNotVisible;
int state = 2;
long id = cursor.getLong(ID_COLUMN);

// First see if the user has already changed the state of this calendar








//Synthetic comment -- diff --git a/src/com/android/calendar/WeekActivity.java b/src/com/android/calendar/WeekActivity.java
//Synthetic comment -- index 5bfdd3a..2c3a096 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup.LayoutParams;







