/*Removed some Warnings, Added Type arguments, replaced finally block by simple return statement

Change-Id:Ie52eecf14773768c97e23ae7f96a80df1d067ade*/
//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaByDayAdapter.java b/src/com/android/calendar/AgendaByDayAdapter.java
//Synthetic comment -- index 881c1a2..f7b86de 100644

//Synthetic comment -- @@ -189,7 +189,6 @@
mTodayJulianDay = Time.getJulianDay(now, time.gmtoff);
LinkedList<MultipleDayInfo> multipleDayList = new LinkedList<MultipleDayInfo>();
for (int position = 0; cursor.moveToNext(); position++) {
            boolean allDay = cursor.getInt(AgendaWindowAdapter.INDEX_ALL_DAY) != 0;
int startDay = cursor.getInt(AgendaWindowAdapter.INDEX_START_DAY);

// Skip over the days outside of the adapter's range








//Synthetic comment -- diff --git a/src/com/android/calendar/AlertActivity.java b/src/com/android/calendar/AlertActivity.java
//Synthetic comment -- index 2a041bf..9648bbf 100644

//Synthetic comment -- @@ -179,7 +179,7 @@

private OnItemClickListener mViewListener = new OnItemClickListener() {

        public void onItemClick(AdapterView parent, View view, int position,
long i) {
AlertActivity alertActivity = AlertActivity.this;
Cursor cursor = alertActivity.getItemForView(view);








//Synthetic comment -- diff --git a/src/com/android/calendar/AlertReceiver.java b/src/com/android/calendar/AlertReceiver.java
//Synthetic comment -- index 7943a9e..d4d4e2d 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;








//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarApplication.java b/src/com/android/calendar/CalendarApplication.java
//Synthetic comment -- index 424e422..35d0ea8 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;

public class CalendarApplication extends Application {









//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 6716edf..44b6535 100644

//Synthetic comment -- @@ -2341,7 +2341,6 @@
void doSingleTapUp(MotionEvent ev) {
int x = (int) ev.getX();
int y = (int) ev.getY();
        Event selectedEvent = mSelectedEvent;
int selectedDay = mSelectionDay;
int selectedHour = mSelectionHour;









//Synthetic comment -- diff --git a/src/com/android/calendar/EditEvent.java b/src/com/android/calendar/EditEvent.java
//Synthetic comment -- index 9438419..a5ea11f 100644

//Synthetic comment -- @@ -581,13 +581,8 @@
Log.w(TAG, "Ignoring unexpected exception", e);
} catch (AuthenticatorException e) {
Log.w(TAG, "Ignoring unexpected exception", e);
            } finally {
                if (primaryCalendarPosition != -1) {
                    return primaryCalendarPosition;
                } else {
                    return 0;
                }
}
}
}









//Synthetic comment -- diff --git a/src/com/android/calendar/Event.java b/src/com/android/calendar/Event.java
//Synthetic comment -- index 956a0f6..55f7726 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
import java.util.concurrent.atomic.AtomicInteger;

// TODO: should Event be Parcelable so it can be passed via Intents?
public class Event implements Comparable, Cloneable {

private static final boolean PROFILE = false;

//Synthetic comment -- @@ -117,8 +117,6 @@
public Event nextUp;
public Event nextDown;

    private static final int MIDNIGHT_IN_MINUTES = 24 * 60;

@Override
public final Object clone() throws CloneNotSupportedException {
super.clone();
//Synthetic comment -- @@ -187,32 +185,30 @@
* Compares this event to the given event.  This is just used for checking
* if two events differ.  It's not used for sorting anymore.
*/
    public final int compareTo(Object obj) {
        Event e = (Event) obj;

// The earlier start day and time comes first
        if (startDay < e.startDay) return -1;
        if (startDay > e.startDay) return 1;
        if (startTime < e.startTime) return -1;
        if (startTime > e.startTime) return 1;

// The later end time comes first (in order to put long strips on
// the left).
        if (endDay < e.endDay) return 1;
        if (endDay > e.endDay) return -1;
        if (endTime < e.endTime) return 1;
        if (endTime > e.endTime) return -1;

// Sort all-day events before normal events.
        if (allDay && !e.allDay) return -1;
        if (!allDay && e.allDay) return 1;

        if (guestsCanModify && !e.guestsCanModify) return -1;
        if (!guestsCanModify && e.guestsCanModify) return 1;

// If two events have the same time range, then sort them in
// alphabetical order based on their titles.
        int cmp = compareStrings(title, e.title);
if (cmp != 0) {
return cmp;
}
//Synthetic comment -- @@ -220,12 +216,12 @@
// If the titles are the same then compare the other fields
// so that we can use this function to check for differences
// between events.
        cmp = compareStrings(location, e.location);
if (cmp != 0) {
return cmp;
}

        cmp = compareStrings(organizer, e.organizer);
if (cmp != 0) {
return cmp;
}








//Synthetic comment -- diff --git a/src/com/android/calendar/EventInfoActivity.java b/src/com/android/calendar/EventInfoActivity.java
//Synthetic comment -- index 1abcb0b..aec7953 100644

//Synthetic comment -- @@ -901,7 +901,6 @@
return;
}

        ContentResolver cr = getContentResolver();
// Yes/No/Maybe Title
View titleView = mLayoutInflater.inflate(R.layout.contact_item, null);
titleView.findViewById(R.id.badge).setVisibility(View.GONE);
//Synthetic comment -- @@ -958,11 +957,9 @@

private class PresenceQueryHandler extends AsyncQueryHandler {
Context mContext;
        ContentResolver mContentResolver;

public PresenceQueryHandler(Context context, ContentResolver cr) {
super(cr);
            mContentResolver = cr;
mContext = context;
}









//Synthetic comment -- diff --git a/src/com/android/calendar/GoogleCalendarUriIntentFilter.java b/src/com/android/calendar/GoogleCalendarUriIntentFilter.java
//Synthetic comment -- index 502243a..3daf1e4 100644

//Synthetic comment -- @@ -27,7 +27,6 @@

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
//Synthetic comment -- @@ -56,7 +55,6 @@
if (uri != null) {
String eid = uri.getQueryParameter("eid");
if (eid != null) {
                    ContentResolver cr = getContentResolver();
String selection = Events.HTML_URI + " LIKE \"%eid=" + eid + "%\"";

Cursor eventCursor = managedQuery(Events.CONTENT_URI, EVENT_PROJECTION,








//Synthetic comment -- diff --git a/src/com/android/calendar/IcsImportActivity.java b/src/com/android/calendar/IcsImportActivity.java
//Synthetic comment -- index c5a125d..3ff43ff 100644

//Synthetic comment -- @@ -30,7 +30,6 @@
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;









//Synthetic comment -- diff --git a/src/com/android/calendar/LaunchActivity.java b/src/com/android/calendar/LaunchActivity.java
//Synthetic comment -- index 659eaab..aa73485 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
import android.preference.PreferenceManager;
import android.provider.Calendar.Calendars;
import android.provider.Gmail;
import android.util.Log;

import com.google.android.googlelogin.GoogleLoginServiceConstants;









//Synthetic comment -- diff --git a/src/com/android/calendar/MonthActivity.java b/src/com/android/calendar/MonthActivity.java
//Synthetic comment -- index 62f8602..a09c7dc 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
import android.provider.Calendar.Events;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;








//Synthetic comment -- diff --git a/src/com/android/calendar/MonthView.java b/src/com/android/calendar/MonthView.java
//Synthetic comment -- index 13f6abc..e5bb33a 100644

//Synthetic comment -- @@ -706,8 +706,6 @@

// Draw the cell contents (excluding monthDay number)
if (!withinCurrentMonth) {
            boolean firstDayOfNextmonth = isFirstDayOfNextMonth(row, column);

// Adjust cell boundaries to compensate for the different border
// style.
r.top--;







