/*Cleaned, Refactored and Optimized

Multiple changes across the whole project, including

* Conversion of if-else blocks to switch where appropriate
* Conversion of if-if blocks to if-else where conditions are exclusive
* Conversion of for loops over Arrays/Collections to foreach
* Final modifier where readability is aided and is not overwhelming
* Removing unused imports
* Other performance changes

Some issues still remain
* No single tab style for switch blocks, some case lines are indented, others not
* Many places deprecated classes and methods are used

Change-Id:I3af3c265f086abfaeb79ea41218dd0c8d09acb96*/
//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaAdapter.java b/src/com/android/calendar/AgendaAdapter.java
//Synthetic comment -- index 723def0..0c741cc 100644

//Synthetic comment -- @@ -76,9 +76,9 @@
holder.overLayColor = 0;
}

        TextView title = holder.title;
        TextView when = holder.when;
        TextView where = holder.where;

/* Calendar Color */
int color = cursor.getInt(AgendaWindowAdapter.INDEX_COLOR);








//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaByDayAdapter.java b/src/com/android/calendar/AgendaByDayAdapter.java
//Synthetic comment -- index acaabc7..eeb4280 100644

//Synthetic comment -- @@ -308,8 +308,8 @@
if (mRowInfo == null) {
return 0;
}
        long millis = time.toMillis(false /* use isDst */);
        int julianDay = Time.getJulianDay(millis, time.gmtoff);
int minDistance = 1000;  // some big number
int minIndex = 0;
int len = mRowInfo.size();
//Synthetic comment -- @@ -346,8 +346,7 @@
int len = mRowInfo.size();
if (position >= len) return 0;  // no row info at this position

        for (int index = position; index >= 0; index--) {
            RowInfo row = mRowInfo.get(index);
if (row.mType == TYPE_DAY) {
return row.mData;
}








//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaWindowAdapter.java b/src/com/android/calendar/AgendaWindowAdapter.java
//Synthetic comment -- index ff74a12..c812559 100644

//Synthetic comment -- @@ -164,13 +164,9 @@

private static class QuerySpec {
long queryStartMillis;

Time goToTime;

int start;

int end;

int queryType;

public QuerySpec(int queryType) {








//Synthetic comment -- diff --git a/src/com/android/calendar/AlertActivity.java b/src/com/android/calendar/AlertActivity.java
//Synthetic comment -- index df73d5c..9906c2a 100644

//Synthetic comment -- @@ -162,7 +162,7 @@
values.put(CalendarAlerts.BEGIN, begin);
values.put(CalendarAlerts.END, end);
values.put(CalendarAlerts.ALARM_TIME, alarmTime);
        long currentTime = System.currentTimeMillis();
values.put(CalendarAlerts.CREATION_TIME, currentTime);
values.put(CalendarAlerts.RECEIVED_TIME, 0);
values.put(CalendarAlerts.NOTIFY_TIME, 0);








//Synthetic comment -- diff --git a/src/com/android/calendar/AlertAdapter.java b/src/com/android/calendar/AlertAdapter.java
//Synthetic comment -- index 839f22a..d11097c 100644

//Synthetic comment -- @@ -60,11 +60,11 @@
}
*/

        String eventName = cursor.getString(AlertActivity.INDEX_TITLE);
        String location = cursor.getString(AlertActivity.INDEX_EVENT_LOCATION);
        long startMillis = cursor.getLong(AlertActivity.INDEX_BEGIN);
        long endMillis = cursor.getLong(AlertActivity.INDEX_END);
        boolean allDay = cursor.getInt(AlertActivity.INDEX_ALL_DAY) != 0;

updateView(context, view, eventName, location, startMillis, endMillis, allDay);
}








//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 9e40558..e80f9f0 100644

//Synthetic comment -- @@ -501,7 +501,7 @@
mDayStrs2Letter = new String[14];

for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            int index = i - Calendar.SUNDAY;
// e.g. Tue for Tuesday
mDayStrs[index] = DateUtils.getDayOfWeekString(i, DateUtils.LENGTH_MEDIUM);
mDayStrs[index + 7] = mDayStrs[index];
//Synthetic comment -- @@ -655,7 +655,7 @@
// Set the base date to the beginning of the week if we are displaying
// 7 days at a time.
if (mNumDays == 7) {
            int dayOfWeek = mBaseDate.weekDay;
int diff = dayOfWeek - mStartDay;
if (diff != 0) {
if (diff < 0) {
//Synthetic comment -- @@ -721,20 +721,20 @@
protected void onSizeChanged(int width, int height, int oldw, int oldh) {
mViewWidth = width;
mViewHeight = height;
        int gridAreaWidth = width - mHoursWidth;
mCellWidth = (gridAreaWidth - (mNumDays * DAY_GAP)) / mNumDays;

Paint p = new Paint();
p.setTextSize(NORMAL_FONT_SIZE);
        int bannerTextHeight = (int) Math.abs(p.ascent());

p.setTextSize(HOURS_FONT_SIZE);
mHoursTextHeight = (int) Math.abs(p.ascent());

p.setTextSize(EVENT_TEXT_FONT_SIZE);
        float ascent = -p.ascent();
mEventTextAscent = (int) Math.ceil(ascent);
        float totalHeight = ascent + p.descent();
mEventTextHeight = (int) Math.ceil(totalHeight);

if (mNumDays > 1) {
//Synthetic comment -- @@ -763,9 +763,7 @@
// the earliest event in each day.
int maxAllDayEvents = 0;
ArrayList<Event> events = mEvents;
        int len = events.size();
        for (int ii = 0; ii < len; ii++) {
            Event event = events.get(ii);
if (event.startDay > mLastJulianDay || event.endDay < mFirstJulianDay)
continue;
if (event.allDay) {
//Synthetic comment -- @@ -826,8 +824,8 @@

mGridAreaHeight = height - mFirstCell;
mCellHeight = (mGridAreaHeight - ((mNumHours + 1) * HOUR_GAP)) / mNumHours;
        int usedGridAreaHeight = (mCellHeight + HOUR_GAP) * mNumHours + HOUR_GAP;
        int bottomSpace = mGridAreaHeight - usedGridAreaHeight;
mEventGeometry.setHourHeight(mCellHeight);

// Create an off-screen bitmap that we can draw into.
//Synthetic comment -- @@ -856,7 +854,7 @@
}
mViewStartY = mFirstHour * (mCellHeight + HOUR_GAP) - mFirstHourOffset;

        int eventAreaWidth = mNumDays * (mCellWidth + DAY_GAP);
//When we get new events we don't want to dismiss the popup unless the event changes
if (mSelectedEvent != null && mLastPopupEventID != mSelectedEvent.id) {
mPopup.dismiss();
//Synthetic comment -- @@ -1287,7 +1285,7 @@
weekStart.hour = 0;
weekStart.minute = 0;
weekStart.second = 0;
        long millis = weekStart.normalize(true /* ignore isDst */);

// Avoid reloading events unnecessarily.
if (millis == mLastReloadMillis) {
//Synthetic comment -- @@ -1359,8 +1357,8 @@
private void drawCalendarView(Canvas canvas) {

// Copy the scrollable region from the big bitmap to the canvas.
        Rect src = mSrcRect;
        Rect dest = mDestRect;

src.top = mViewStartY;
src.bottom = mViewStartY + mGridAreaHeight;
//Synthetic comment -- @@ -1380,8 +1378,8 @@
}

private void drawAfterScroll(Canvas canvas) {
        Paint p = mPaint;
        Rect r = mRect;

if (mMaxAllDayEvents != 0) {
drawAllDayEvents(mFirstJulianDay, mNumDays, r, canvas, p);
//Synthetic comment -- @@ -1448,7 +1446,7 @@
p.setTextSize(NORMAL_FONT_SIZE);
p.setTextAlign(Paint.Align.CENTER);
int x = mHoursWidth;
        int deltaX = mCellWidth + DAY_GAP;
int cell = mFirstJulianDay;

String[] dayNames;
//Synthetic comment -- @@ -1477,7 +1475,7 @@
text = mPmString;
}
int y = mFirstCell + mFirstHourOffset + 2 * mHoursTextHeight + HOUR_GAP;
        int right = mHoursWidth - HOURS_RIGHT_MARGIN;
canvas.drawText(text, right, y, p);

if (mFirstHour < 12 && mFirstHour + mNumHours > 12) {
//Synthetic comment -- @@ -1516,8 +1514,8 @@
}

private void doDraw(Canvas canvas) {
        Paint p = mPaint;
        Rect r = mRect;
int lineY = mCurrentTime.hour*(mCellHeight + HOUR_GAP)
+ ((mCurrentTime.minute * mCellHeight) / 60)
+ 1;
//Synthetic comment -- @@ -1527,7 +1525,7 @@

// Draw each day
int x = mHoursWidth;
        int deltaX = mCellWidth + DAY_GAP;
int cell = mFirstJulianDay;
for (int day = 0; day < mNumDays; day++, cell++) {
drawEvents(cell, x, HOUR_GAP, canvas, p);
//Synthetic comment -- @@ -1576,7 +1574,7 @@

// Also draw the highlight on the grid
p.setColor(mCalendarGridAreaSelected);
            int daynum = mSelectionDay - mFirstJulianDay;
r.left = mHoursWidth + daynum * (mCellWidth + DAY_GAP);
r.right = r.left + mCellWidth;
canvas.drawRect(r, p);
//Synthetic comment -- @@ -1617,7 +1615,7 @@
// if the title text has changed => announce period
CharSequence titleTextViewText = mTitleTextView.getText();
// intended use of identity comparison
        boolean titleChanged = titleTextViewText != mPrevTitleTextViewText;
if (titleChanged) {
mPrevTitleTextViewText = titleTextViewText;
sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
//Synthetic comment -- @@ -1860,9 +1858,8 @@
private int computeMaxStringWidth(int currentMax, String[] strings, Paint p) {
float maxWidthF = 0.0f;

        int len = strings.length;
        for (int i = 0; i < len; i++) {
            float width = p.measureText(strings[i]);
maxWidthF = Math.max(width, maxWidthF);
}
int maxWidth = (int) (maxWidthF + 0.5);
//Synthetic comment -- @@ -1929,11 +1926,9 @@
float left = mHoursWidth;
int lastDay = firstDay + numDays - 1;
ArrayList<Event> events = mEvents;
        int numEvents = events.size();
float drawHeight = mAllDayHeight;
float numRectangles = mMaxAllDayEvents;
        for (int i = 0; i < numEvents; i++) {
            Event event = events.get(i);
if (!event.allDay)
continue;
int startDay = event.startDay;
//Synthetic comment -- @@ -2011,8 +2006,7 @@
}

// First, clear all the links
        for (int ii = 0; ii < len; ii++) {
            Event ev = mSelectedEvents.get(ii);
ev.nextUp = null;
ev.nextDown = null;
ev.nextLeft = null;
//Synthetic comment -- @@ -2100,11 +2094,9 @@
selectionArea.right = selectionArea.left + cellWidth;

ArrayList<Event> events = mEvents;
        int numEvents = events.size();
EventGeometry geometry = mEventGeometry;

        for (int i = 0; i < numEvents; i++) {
            Event event = events.get(i);
if (!geometry.computeEventRect(date, left, top, cellWidth, event)) {
continue;
}
//Synthetic comment -- @@ -2137,8 +2129,7 @@
}

// First, clear all the links
        for (int ii = 0; ii < len; ii++) {
            Event ev = mSelectedEvents.get(ii);
ev.nextUp = null;
ev.nextDown = null;
ev.nextLeft = null;
//Synthetic comment -- @@ -2423,11 +2414,11 @@
// Fade visible boxes if event was declined.
boolean declined = (event.selfAttendeeStatus == Attendees.ATTENDEE_STATUS_DECLINED);
if (declined) {
            int alpha = color & 0xff000000;
color &= 0x00ffffff;
            int red = (color & 0x00ff0000) >> 16;
            int green = (color & 0x0000ff00) >> 8;
            int blue = (color & 0x0000ff);
color = ((red >> 1) << 16) | ((green >> 1) << 8) | (blue >> 1);
color += 0x7F7F7F + alpha;
}
//Synthetic comment -- @@ -2490,11 +2481,11 @@
return;
}

        float width = rf.right - rf.left;
float height = rf.bottom - rf.top;

// Leave one pixel extra space between lines
        int lineHeight = mEventTextHeight + 1;

// If the rectangle is too small for text, then return
if (width < MIN_CELL_WIDTH_FOR_TEXT || height <= lineHeight) {
//Synthetic comment -- @@ -2691,8 +2682,8 @@
// Use the distance from the current point to the initial touch instead
// of deltaX and deltaY to avoid accumulating floating-point rounding
// errors.  Also, we don't need floats, we can use ints.
        int distanceX = (int) e1.getX() - (int) e2.getX();
        int distanceY = (int) e1.getY() - (int) e2.getY();

// If we haven't figured out the predominant scroll direction yet,
// then do it now.
//Synthetic comment -- @@ -2772,10 +2763,10 @@
mTouchMode = TOUCH_MODE_INITIAL_STATE;
mSelectionMode = SELECTION_HIDDEN;
mOnFlingCalled = true;
        int deltaX = (int) e2.getX() - (int) e1.getX();
        int distanceX = Math.abs(deltaX);
        int deltaY = (int) e2.getY() - (int) e1.getY();
        int distanceY = Math.abs(deltaY);

if ((distanceX >= HORIZONTAL_SCROLL_THRESHOLD) && (distanceX > distanceY)) {
boolean switchForward = initNextView(deltaX);








//Synthetic comment -- diff --git a/src/com/android/calendar/DeleteEventHelper.java b/src/com/android/calendar/DeleteEventHelper.java
//Synthetic comment -- index 61abc8d..034fd6c 100644

//Synthetic comment -- @@ -245,16 +245,16 @@
}

private void deleteRepeatingEvent(int which) {
        int indexDtstart = mCursor.getColumnIndexOrThrow(Events.DTSTART);
        int indexAllDay = mCursor.getColumnIndexOrThrow(Events.ALL_DAY);
        int indexTitle = mCursor.getColumnIndexOrThrow(Events.TITLE);
        int indexTimezone = mCursor.getColumnIndexOrThrow(Events.EVENT_TIMEZONE);
        int indexCalendarId = mCursor.getColumnIndexOrThrow(Events.CALENDAR_ID);

        String rRule = mCursor.getString(mEventIndexRrule);
        boolean allDay = mCursor.getInt(indexAllDay) != 0;
        long dtstart = mCursor.getLong(indexDtstart);
        long id = mCursor.getInt(mEventIndexId);

// If the repeating event has not been given a sync id from the server
// yet, then we can't delete a single instance of this event.  (This is








//Synthetic comment -- diff --git a/src/com/android/calendar/EditEvent.java b/src/com/android/calendar/EditEvent.java
//Synthetic comment -- index 43d99a3..3017697 100644

//Synthetic comment -- @@ -365,9 +365,9 @@
long endMillis;
if (mView == mStartDateButton) {
// The start date was changed.
                int yearDuration = endTime.year - startTime.year;
                int monthDuration = endTime.month - startTime.month;
                int monthDayDuration = endTime.monthDay - startTime.monthDay;

startTime.year = year;
startTime.month = month;
//Synthetic comment -- @@ -460,8 +460,8 @@
}

if (v == mDeleteButton) {
            long begin = mStartTime.toMillis(false /* use isDst */);
            long end = mEndTime.toMillis(false /* use isDst */);
int which = -1;
switch (mModification) {
case MODIFY_SELECTED:
//Synthetic comment -- @@ -563,7 +563,7 @@
return;
}

                int defaultCalendarPosition = findDefaultCalendarPosition(mCalendarsCursor);

// populate the calendars spinner
CalendarsAdapter adapter = new CalendarsAdapter(EditEvent.this, mCalendarsCursor);
//Synthetic comment -- @@ -674,7 +674,7 @@
long calendarId = mEventCursor.getInt(EVENT_INDEX_CALENDAR_ID);
mOwnerAccount = mEventCursor.getString(EVENT_INDEX_OWNER_ACCOUNT);
if (!TextUtils.isEmpty(mOwnerAccount)) {
                String ownerDomain = extractDomain(mOwnerAccount);
if (ownerDomain != null) {
domain = ownerDomain;
}
//Synthetic comment -- @@ -824,17 +824,16 @@
// Initialize the reminder values array.
Resources r = getResources();
String[] strings = r.getStringArray(R.array.reminder_minutes_values);
        int size = strings.length;
        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0 ; i < size ; i++) {
            list.add(Integer.parseInt(strings[i]));
}
mReminderValues = list;
        String[] labels = r.getStringArray(R.array.reminder_minutes_labels);
mReminderLabels = new ArrayList<String>(Arrays.asList(labels));

SharedPreferences prefs = CalendarPreferenceActivity.getSharedPreferences(this);
        String durationString =
prefs.getString(CalendarPreferenceActivity.KEY_DEFAULT_REMINDER, "0");
mDefaultReminderMinutes = Integer.parseInt(durationString);

//Synthetic comment -- @@ -851,20 +850,20 @@
&& (mEventCursor.getInt(EVENT_INDEX_HAS_ALARM) != 0);
if (hasAlarm) {
Uri uri = Reminders.CONTENT_URI;
            String where = String.format(REMINDERS_WHERE, eventId);
Cursor reminderCursor = cr.query(uri, REMINDERS_PROJECTION, where, null, null);
try {
// First pass: collect all the custom reminder minutes (e.g.,
// a reminder of 8 minutes) into a global list.
while (reminderCursor.moveToNext()) {
                    int minutes = reminderCursor.getInt(REMINDERS_INDEX_MINUTES);
EditEvent.addMinutesToList(this, mReminderValues, mReminderLabels, minutes);
}

// Second pass: create the reminder spinners
reminderCursor.moveToPosition(-1);
while (reminderCursor.moveToNext()) {
                    int minutes = reminderCursor.getInt(REMINDERS_INDEX_MINUTES);
mOriginalMinutes.add(minutes);
EditEvent.addReminder(this, this, mReminderItems, mReminderValues,
mReminderLabels, minutes);
//Synthetic comment -- @@ -888,15 +887,15 @@

// Attendees cursor
if (mHasAttendeeData && eventId != -1) {
            Uri uri = Attendees.CONTENT_URI;
            String[] whereArgs = {Long.toString(eventId)};
Cursor attendeeCursor = cr.query(uri, ATTENDEES_PROJECTION, ATTENDEES_WHERE, whereArgs,
null);
try {
StringBuilder b = new StringBuilder();
while (attendeeCursor.moveToNext()) {
                    String name = attendeeCursor.getString(ATTENDEES_INDEX_NAME);
                    String email = attendeeCursor.getString(ATTENDEES_INDEX_EMAIL);
if (email != null) {
if (name != null && name.length() > 0 && !name.equals(email)) {
b.append('"').append(name).append("\" ");
//Synthetic comment -- @@ -960,32 +959,32 @@
private static InputFilter[] sRecipientFilters = new InputFilter[] { new Rfc822InputFilter() };

private void initFromIntent(Intent intent) {
        String title = intent.getStringExtra(Events.TITLE);
if (title != null) {
mTitleTextView.setText(title);
}

        String location = intent.getStringExtra(Events.EVENT_LOCATION);
if (location != null) {
mLocationTextView.setText(location);
}

        String description = intent.getStringExtra(Events.DESCRIPTION);
if (description != null) {
mDescriptionTextView.setText(description);
}

        int availability = intent.getIntExtra(Events.TRANSPARENCY, -1);
if (availability != -1) {
mAvailabilitySpinner.setSelection(availability);
}

        int visibility = intent.getIntExtra(Events.VISIBILITY, -1);
if (visibility != -1) {
mVisibilitySpinner.setSelection(visibility);
}

        String rrule = intent.getStringExtra(Events.RRULE);
if (!TextUtils.isEmpty(rrule)) {
mRrule = rrule;
mEventRecurrence.parse(rrule);
//Synthetic comment -- @@ -1009,9 +1008,9 @@
cursor.moveToFirst();

mRrule = cursor.getString(EVENT_INDEX_RRULE);
            String title = cursor.getString(EVENT_INDEX_TITLE);
            String description = cursor.getString(EVENT_INDEX_DESCRIPTION);
            String location = cursor.getString(EVENT_INDEX_EVENT_LOCATION);
int availability = cursor.getInt(EVENT_INDEX_TRANSPARENCY);
int visibility = cursor.getInt(EVENT_INDEX_VISIBILITY);
if (visibility > 0) {
//Synthetic comment -- @@ -1063,14 +1062,18 @@
.setTitle(R.string.edit_event_label)
.setItems(items, new OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
mModification =
                                            (mSyncId == null) ? MODIFY_ALL : MODIFY_SELECTED;
                                } else if (which == 1) {
mModification =
(mSyncId == null) ? MODIFY_ALL_FOLLOWING : MODIFY_ALL;
                                } else if (which == 2) {
mModification = MODIFY_ALL_FOLLOWING;
}

// If we are modifying all the events in a
//Synthetic comment -- @@ -1103,7 +1106,7 @@

// Round the time to the nearest half hour.
mStartTime.second = 0;
                int minute = mStartTime.minute;
if (minute == 0) {
// We are already on a half hour increment
} else if (minute > 0 && minute <= 30) {
//Synthetic comment -- @@ -1113,7 +1116,7 @@
mStartTime.hour += 1;
}

                long startMillis = mStartTime.normalize(true /* ignore isDst */);
mEndTime.set(startMillis + DateUtils.HOUR_IN_MILLIS);
}

//Synthetic comment -- @@ -1243,15 +1246,15 @@
* formats and displays them.
*/
private void updateHomeTime() {
        String tz = Utils.getTimeZone(this, mUpdateTZ);
if (!mAllDayCheckBox.isChecked() && !TextUtils.equals(tz, mTimezone)) {
int flags = DateUtils.FORMAT_SHOW_TIME;
boolean is24Format = DateFormat.is24HourFormat(this);
if (is24Format) {
flags |= DateUtils.FORMAT_24HOUR;
}
            long millisStart = mStartTime.toMillis(false);
            long millisEnd = mEndTime.toMillis(false);

boolean isDSTStart = mStartTime.isDst != 0;
boolean isDSTEnd = mEndTime.isDst != 0;
//Synthetic comment -- @@ -1357,8 +1360,8 @@

private void populateRepeats() {
Time time = mStartTime;
        Resources r = getResources();
        int resource = android.R.layout.simple_spinner_item;

String[] days = new String[] {
DateUtils.getDayOfWeekString(Calendar.SUNDAY, DateUtils.LENGTH_MEDIUM),
//Synthetic comment -- @@ -1396,7 +1399,7 @@
recurrenceIndexes.add(REPEATS_WEEKLY_ON_DAY);

// Calculate whether this is the 1st, 2nd, 3rd, 4th, or last appearance of the given day.
        int dayNumber = (time.monthDay - 1) / 7;
format = r.getString(R.string.monthly_on_day_count);
repeatArray.add(String.format(format, ordinals[dayNumber], days[time.weekDay]));
recurrenceIndexes.add(REPEATS_MONTHLY_ON_DAY_COUNT);
//Synthetic comment -- @@ -1405,7 +1408,7 @@
repeatArray.add(String.format(format, time.monthDay));
recurrenceIndexes.add(REPEATS_MONTHLY_ON_DAY);

        long when = time.toMillis(false);
format = r.getString(R.string.yearly);
int flags = 0;
if (DateFormat.is24HourFormat(this)) {
//Synthetic comment -- @@ -1483,7 +1486,7 @@
reminderRemoveButton = (ImageButton) reminderItem.findViewById(R.id.reminder_remove);
reminderRemoveButton.setOnClickListener(listener);

        int index = findMinutesInReminderList(values, minutes);
spinner.setSelection(index);
items.add(reminderItem);

//Synthetic comment -- @@ -1492,7 +1495,7 @@

static void addMinutesToList(Context context, ArrayList<Integer> values,
ArrayList<String> labels, int minutes) {
        int index = values.indexOf(minutes);
if (index != -1) {
return;
}
//Synthetic comment -- @@ -1501,7 +1504,7 @@
// into the list.

String label = constructReminderLabel(context, minutes, false);
        int len = values.size();
for (int i = 0; i < len; i++) {
if (minutes < values.get(i)) {
values.add(i, minutes);
//Synthetic comment -- @@ -1522,7 +1525,7 @@
* @return the index of "minutes" in the "values" list
*/
private static int findMinutesInReminderList(ArrayList<Integer> values, int minutes) {
        int index = values.indexOf(minutes);
if (index == -1) {
// This should never happen.
Log.e("Cal", "Cannot find minutes (" + minutes + ") in list");
//Synthetic comment -- @@ -1569,7 +1572,7 @@
}

private void setDate(TextView view, long millis) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR |
DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH |
DateUtils.FORMAT_ABBREV_WEEKDAY;

//Synthetic comment -- @@ -1652,7 +1655,7 @@
// Update the "hasAlarm" field for the event
ArrayList<Integer> reminderMinutes = reminderItemsToMinutes(mReminderItems,
mReminderValues);
        int len = reminderMinutes.size();
values.put(Events.HAS_ALARM, (len > 0) ? 1 : 0);

// For recurring events, we must make sure that we use duration rather
//Synthetic comment -- @@ -1760,7 +1763,7 @@
}

// New Event or New Exception to an existing event
        boolean newEvent = (eventIdIndex != -1);

if (newEvent) {
saveRemindersWithBackRef(ops, eventIdIndex, reminderMinutes, mOriginalMinutes,
//Synthetic comment -- @@ -1826,7 +1829,7 @@

// the eventId is only used if eventIdIndex is -1.
// TODO: clean up this code.
                long eventId = uri != null ? ContentUris.parseId(uri) : -1;

// only compute deltas if this is an existing event.
// new events (being inserted into the Events table) won't
//Synthetic comment -- @@ -1894,8 +1897,8 @@
ContentProviderResult[] results =
getContentResolver().applyBatch(android.provider.Calendar.AUTHORITY, ops);
if (DEBUG) {
                for (int i = 0; i < results.length; i++) {
                    Log.v(TAG, "results = " + results[i].toString());
}
}
} catch (RemoteException e) {
//Synthetic comment -- @@ -2041,19 +2044,16 @@
}

// Delete all the existing reminders for this event
        String where = Reminders.EVENT_ID + "=?";
String[] args = new String[] { Long.toString(eventId) };
Builder b = ContentProviderOperation.newDelete(Reminders.CONTENT_URI);
b.withSelection(where, args);
ops.add(b.build());

ContentValues values = new ContentValues();
        int len = reminderMinutes.size();

// Insert the new reminders, if any
        for (int i = 0; i < len; i++) {
            int minutes = reminderMinutes.get(i);

values.clear();
values.put(Reminders.MINUTES, minutes);
values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
//Synthetic comment -- @@ -2079,12 +2079,9 @@
ops.add(b.build());

ContentValues values = new ContentValues();
        int len = reminderMinutes.size();

// Insert the new reminders, if any
        for (int i = 0; i < len; i++) {
            int minutes = reminderMinutes.get(i);

values.clear();
values.put(Reminders.MINUTES, minutes);
values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
//Synthetic comment -- @@ -2103,16 +2100,17 @@
}

values.put(Events.RRULE, mRrule);
        long end = mEndTime.toMillis(true /* ignore dst */);
        long start = mStartTime.toMillis(true /* ignore dst */);
String duration;

boolean isAllDay = mAllDayCheckBox.isChecked();
if (isAllDay) {
            long days = (end - start + DateUtils.DAY_IN_MILLIS - 1) / DateUtils.DAY_IN_MILLIS;
duration = "P" + days + "D";
} else {
            long seconds = (end - start) / DateUtils.SECOND_IN_MILLIS;
duration = "P" + seconds + "S";
}
values.put(Events.DURATION, duration);
//Synthetic comment -- @@ -2129,22 +2127,23 @@
}

private void updateRecurrenceRule() {
        int position = mRepeatsSpinner.getSelectedItemPosition();
        int selection = mRecurrenceIndexes.get(position);
// Make sure we don't have any leftover data from the previous setting
clearRecurrence();

        if (selection == DOES_NOT_REPEAT) {
mRrule = null;
return;
        } else if (selection == REPEATS_CUSTOM) {
// Keep custom recurrence as before.
return;
        } else if (selection == REPEATS_DAILY) {
mEventRecurrence.freq = EventRecurrence.DAILY;
        } else if (selection == REPEATS_EVERY_WEEKDAY) {
mEventRecurrence.freq = EventRecurrence.WEEKLY;
            int dayCount = 5;
int[] byday = new int[dayCount];
int[] bydayNum = new int[dayCount];

//Synthetic comment -- @@ -2160,10 +2159,12 @@
mEventRecurrence.byday = byday;
mEventRecurrence.bydayNum = bydayNum;
mEventRecurrence.bydayCount = dayCount;
        } else if (selection == REPEATS_WEEKLY_ON_DAY) {
mEventRecurrence.freq = EventRecurrence.WEEKLY;
int[] days = new int[1];
            int dayCount = 1;
int[] dayNum = new int[dayCount];

days[0] = EventRecurrence.timeDay2Day(mStartTime.weekDay);
//Synthetic comment -- @@ -2173,14 +2174,17 @@
mEventRecurrence.byday = days;
mEventRecurrence.bydayNum = dayNum;
mEventRecurrence.bydayCount = dayCount;
        } else if (selection == REPEATS_MONTHLY_ON_DAY) {
mEventRecurrence.freq = EventRecurrence.MONTHLY;
mEventRecurrence.bydayCount = 0;
mEventRecurrence.bymonthdayCount = 1;
int[] bymonthday = new int[1];
bymonthday[0] = mStartTime.monthDay;
mEventRecurrence.bymonthday = bymonthday;
        } else if (selection == REPEATS_MONTHLY_ON_DAY_COUNT) {
mEventRecurrence.freq = EventRecurrence.MONTHLY;
mEventRecurrence.bydayCount = 1;
mEventRecurrence.bymonthdayCount = 0;
//Synthetic comment -- @@ -2196,8 +2200,10 @@
byday[0] = EventRecurrence.timeDay2Day(mStartTime.weekDay);
mEventRecurrence.byday = byday;
mEventRecurrence.bydayNum = bydayNum;
        } else if (selection == REPEATS_YEARLY) {
mEventRecurrence.freq = EventRecurrence.YEARLY;
}

// Set the week start day.
//Synthetic comment -- @@ -2206,10 +2212,10 @@
}

private ContentValues getContentValuesFromUi() {
        String title = mTitleTextView.getText().toString().trim();
        boolean isAllDay = mAllDayCheckBox.isChecked();
        String location = mLocationTextView.getText().toString().trim();
        String description = mDescriptionTextView.getText().toString().trim();

ContentValues values = new ContentValues();

//Synthetic comment -- @@ -2274,17 +2280,17 @@
}

private boolean isEmpty() {
        String title = mTitleTextView.getText().toString().trim();
if (title.length() > 0) {
return false;
}

        String location = mLocationTextView.getText().toString().trim();
if (location.length() > 0) {
return false;
}

        String description = mDescriptionTextView.getText().toString().trim();
if (description.length() > 0) {
return false;
}








//Synthetic comment -- diff --git a/src/com/android/calendar/EmailAddressAdapter.java b/src/com/android/calendar/EmailAddressAdapter.java
//Synthetic comment -- index bfcb986..e5de00c 100644

//Synthetic comment -- @@ -69,8 +69,8 @@

@Override
public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        String filter = constraint == null ? "" : constraint.toString();
        Uri uri = Uri.withAppendedPath(Email.CONTENT_FILTER_URI, Uri.encode(filter));
return mContentResolver.query(uri, PROJECTION, null, null, SORT_ORDER);
}
}








//Synthetic comment -- diff --git a/src/com/android/calendar/EventGeometry.java b/src/com/android/calendar/EventGeometry.java
//Synthetic comment -- index f4c1db2..2839c71 100644

//Synthetic comment -- @@ -50,9 +50,9 @@
return false;
}

        float cellMinuteHeight = mMinuteHeight;
        int startDay = event.startDay;
        int endDay = event.endDay;

if (startDay > date || endDay < date) {
return false;
//Synthetic comment -- @@ -73,9 +73,9 @@
endTime = CalendarView.MINUTES_PER_DAY;
}

        int col = event.getColumn();
        int maxCols = event.getMaxColumns();
        int startHour = startTime / 60;
int endHour = endTime / 60;

// If the end point aligns on a cell boundary then count it as
//Synthetic comment -- @@ -97,7 +97,7 @@
event.bottom = event.top + mMinEventHeight;
}

        float colWidth = (float) (cellWidth - 2 * mCellMargin) / (float) maxCols;
event.left = left + mCellMargin + col * colWidth;
event.right = event.left + colWidth;
return true;
//Synthetic comment -- @@ -118,10 +118,10 @@
* Computes the distance from the given point to the given event.
*/
float pointToEvent(float x, float y, Event event) {
        float left = event.left;
        float right = event.right;
        float top = event.top;
        float bottom = event.bottom;

if (x >= left) {
if (x <= right) {








//Synthetic comment -- diff --git a/src/com/android/calendar/EventInfoActivity.java b/src/com/android/calendar/EventInfoActivity.java
//Synthetic comment -- index 62dfae2..583af19 100644

//Synthetic comment -- @@ -384,10 +384,9 @@
// Initialize the reminder values array.
Resources r = getResources();
String[] strings = r.getStringArray(R.array.reminder_minutes_values);
        int size = strings.length;
        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0 ; i < size ; i++) {
            list.add(Integer.parseInt(strings[i]));
}
mReminderValues = list;
String[] labels = r.getStringArray(R.array.reminder_minutes_labels);
//Synthetic comment -- @@ -592,8 +591,8 @@
cr.applyBatch(Calendars.CONTENT_URI.getAuthority(), ops);
// Update the "hasAlarm" field for the event
Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, mEventId);
            int len = reminderMinutes.size();
            boolean hasAlarm = len > 0;
if (hasAlarm != mOriginalHasAlarm) {
ContentValues values = new ContentValues();
values.put(Events.HAS_ALARM, hasAlarm ? 1 : 0);
//Synthetic comment -- @@ -707,12 +706,12 @@
return false;
}
Spinner spinner = (Spinner) findViewById(R.id.response_value);
        int position = spinner.getSelectedItemPosition() - mResponseOffset;
if (position <= 0) {
return false;
}

        int status = ATTENDEE_VALUES[position];

// If the status has not changed, then don't update the database
if (status == mOriginalAttendeeResponse) {
//Synthetic comment -- @@ -782,11 +781,11 @@
try {
ContentValues values = new ContentValues();

            String title = cursor.getString(EVENT_INDEX_TITLE);
            String timezone = cursor.getString(EVENT_INDEX_EVENT_TIMEZONE);
            int calendarId = cursor.getInt(EVENT_INDEX_CALENDAR_ID);
            boolean allDay = cursor.getInt(EVENT_INDEX_ALL_DAY) != 0;
            String syncId = cursor.getString(EVENT_INDEX_SYNC_ID);

values.put(Events.TITLE, title);
values.put(Events.EVENT_TIMEZONE, timezone);
//Synthetic comment -- @@ -801,13 +800,12 @@
values.put(Events.SELF_ATTENDEE_STATUS, status);

ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int eventIdIndex = ops.size();

ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(
values).build());

if (mHasAttendeeData) {
                ContentProviderOperation.Builder b;
// Insert the new attendees
for (Attendee attendee : mAcceptedAttendees) {
addAttendee(values, ops, eventIdIndex, attendee,








//Synthetic comment -- diff --git a/src/com/android/calendar/EventLoader.java b/src/com/android/calendar/EventLoader.java
//Synthetic comment -- index 57201f2..56d01c7 100644

//Synthetic comment -- @@ -84,16 +84,16 @@
//query which days have events
Cursor cursor = EventDays.query(cr, startDay, numDays);
try {
                int startDayColumnIndex = cursor.getColumnIndexOrThrow(EventDays.STARTDAY);
                int endDayColumnIndex = cursor.getColumnIndexOrThrow(EventDays.ENDDAY);

//Set all the days with events to true
while (cursor.moveToNext()) {
                    int firstDay = cursor.getInt(startDayColumnIndex);
                    int lastDay = cursor.getInt(endDayColumnIndex);
//we want the entire range the event occurs, but only within the month
                    int firstIndex = Math.max(firstDay - startDay, 0);
                    int lastIndex = Math.min(lastDay - startDay, 30);

for(int i = firstIndex; i <= lastIndex; i++) {
eventDays[i] = true;








//Synthetic comment -- diff --git a/src/com/android/calendar/EventRecurrenceFormatter.java b/src/com/android/calendar/EventRecurrenceFormatter.java
//Synthetic comment -- index 06dfed1..2a96436 100644

//Synthetic comment -- @@ -38,7 +38,7 @@

// Do one less iteration in the loop so the last element is added out of the
// loop. This is done so the comma is not placed after the last item.
                    int count = recurrence.bydayCount - 1;
if (count >= 0) {
for (int i = 0 ; i < count ; i++) {
days.append(dayToString(recurrence.byday[i]));
//Synthetic comment -- @@ -57,7 +57,7 @@
return null;
}

                    int day = EventRecurrence.timeDay2Day(recurrence.startDate.weekDay);
return String.format(format, dayToString(day));
}
}








//Synthetic comment -- diff --git a/src/com/android/calendar/LaunchActivity.java b/src/com/android/calendar/LaunchActivity.java
//Synthetic comment -- index ea11051..bd01993 100644

//Synthetic comment -- @@ -48,7 +48,7 @@

// Only try looking for an account if this is the first launch.
if (icicle == null) {
            Account[] accounts = AccountManager.get(this).getAccounts();
if(accounts.length > 0) {
// If the only account is an account that can't use Calendar we let the user into
// Calendar, but they can't create any events until they add an account with a
//Synthetic comment -- @@ -70,7 +70,7 @@

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Account[] accounts = AccountManager.get(this).getAccounts();
if(accounts.length > 0) {
// If the only account is an account that can't use Calendar we let the user into
// Calendar, but they can't create any events until they add an account with a








//Synthetic comment -- diff --git a/src/com/android/calendar/MonthActivity.java b/src/com/android/calendar/MonthActivity.java
//Synthetic comment -- index 4b895e9..ad4a9b3 100644

//Synthetic comment -- @@ -136,8 +136,8 @@
// This is faster than calling getSelectedTime() because we avoid
// a call to Time#normalize().
if (animate) {
            int currentMonth = currentTime.month + currentTime.year * 12;
            int nextMonth = time.month + time.year * 12;
if (nextMonth < currentMonth) {
mSwitcher.setInAnimation(mInAnimationPast);
mSwitcher.setOutAnimation(mOutAnimationPast);
//Synthetic comment -- @@ -257,7 +257,7 @@

// Get first day of week based on locale and populate the day headers
mStartDay = Calendar.getInstance().getFirstDayOfWeek();
        int diff = mStartDay - Calendar.SUNDAY - 1;
final int startDay = Utils.getFirstDayOfWeek();
final int sundayColor = getResources().getColor(R.color.sunday_text_color);
final int saturdayColor = getResources().getColor(R.color.saturday_text_color);
//Synthetic comment -- @@ -296,7 +296,7 @@

@Override
protected void onNewIntent(Intent intent) {
        long timeMillis = Utils.timeFromIntentInMillis(intent);
if (timeMillis > 0) {
Time time = new Time(Utils.getTimeZone(this, mUpdateTZ));
time.set(timeMillis);








//Synthetic comment -- diff --git a/src/com/android/calendar/MonthView.java b/src/com/android/calendar/MonthView.java
//Synthetic comment -- index afee98b..2dfeb13 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
//Synthetic comment -- @@ -324,8 +323,8 @@
}

public void setSelectedCell(MotionEvent e) {
                int x = (int) e.getX();
                int y = (int) e.getY();
int row = (y - WEEK_GAP) / (WEEK_GAP + mCellHeight);
int col = (x - mBorder) / (MONTH_DAY_GAP + mCellWidth);
if (row > 5) {
//Synthetic comment -- @@ -386,8 +385,8 @@
mRedrawScreen = true;
invalidate();
mLaunchDayView = false;
                    int x = (int) e.getX();
                    int y = (int) e.getY();
long millis = getSelectedMillisFor(x, y);
Utils.startActivity(getContext(), mDetailedView, millis);
}
//Synthetic comment -- @@ -428,18 +427,18 @@
public boolean onMenuItemClick(MenuItem item) {
switch (item.getItemId()) {
case MenuHelper.MENU_DAY: {
                    long startMillis = getSelectedTimeInMillis();
Utils.startActivity(mParentActivity, DayActivity.class.getName(), startMillis);
break;
}
case MenuHelper.MENU_AGENDA: {
                    long startMillis = getSelectedTimeInMillis();
Utils.startActivity(mParentActivity, AgendaActivity.class.getName(), startMillis);
break;
}
case MenuHelper.MENU_EVENT_CREATE: {
                    long startMillis = getSelectedTimeInMillis();
                    long endMillis = startMillis + DateUtils.HOUR_IN_MILLIS;
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setClassName(mParentActivity, EditEvent.class.getName());
intent.putExtra(EVENT_BEGIN_TIME, startMillis);
//Synthetic comment -- @@ -475,7 +474,7 @@
mRedrawScreen = true;
mParentActivity.stopProgressSpinner();
invalidate();
                int numEvents = events.size();

// Clear out event days
for (int i = 0; i < EVENT_NUM_DAYS; i++) {
//Synthetic comment -- @@ -490,14 +489,12 @@
if (startDay < 31 || endDay >= 0) {
if (startDay < 0) {
startDay = 0;
                        }
                        if (startDay > 31) {
startDay = 31;
}
if (endDay < 0) {
endDay = 0;
                        }
                        if (endDay > 31) {
endDay = 31;
}
for (int j = startDay; j < endDay; j++) {
//Synthetic comment -- @@ -679,7 +676,7 @@
}

private long getSelectedMillisFor(int x, int y) {
        int row = (y - WEEK_GAP) / (WEEK_GAP + mCellHeight);
int column = (x - mBorder) / (MONTH_DAY_GAP + mCellWidth);
if (column > 6) {
column = 6;
//Synthetic comment -- @@ -710,6 +707,7 @@
for(int i = 0; i < size; i++) {
bitmapCache.valueAt(i).recycle();
}
bitmapCache.clear();

}
//Synthetic comment -- @@ -727,11 +725,11 @@
final int height = getMeasuredHeight();

for (int row = 0; row < 6; row++) {
            int y = WEEK_GAP + row * (WEEK_GAP + mCellHeight) - 1;
canvas.drawLine(0, y, width, y, p);
}
for (int column = 1; column < 7; column++) {
            int x = mBorder + column * (MONTH_DAY_GAP + mCellWidth) - 1;
canvas.drawLine(x, WEEK_GAP, x, height, p);
}
}
//Synthetic comment -- @@ -883,19 +881,18 @@
*easy to find tags draw number draw day*/
p.setTextAlign(Paint.Align.CENTER);
// center of text
        int textX = r.left + (r.right - BUSY_BITS_MARGIN - BUSY_BITS_WIDTH - r.left) / 2;
        int textY = (int) (r.top + p.getTextSize() + TEXT_TOP_MARGIN); // bottom of text
canvas.drawText(String.valueOf(mCursor.getDayAt(row, column)), textX, textY, p);
}

///Create and draw the event busybits for this day
private void drawEvents(int date, Canvas canvas, Rect rect, Paint p, boolean drawBg) {
// The top of the busybits section lines up with the top of the day number
        int top = rect.top + TEXT_TOP_MARGIN + BUSY_BITS_MARGIN;
        int left = rect.right - BUSY_BITS_MARGIN - BUSY_BITS_WIDTH;

ArrayList<Event> events = mEvents;
        int numEvents = events.size();
EventGeometry geometry = mEventGeometry;

if (drawBg) {
//Synthetic comment -- @@ -909,9 +906,8 @@
p.setStyle(Style.FILL);
canvas.drawRect(rf, p);
}

        for (int i = 0; i < numEvents; i++) {
            Event event = events.get(i);
if (!geometry.computeEventRect(date, left, top, BUSY_BITS_WIDTH, event)) {
continue;
}
//Synthetic comment -- @@ -1059,16 +1055,13 @@

getHandler().removeCallbacks(mDismissPopup);
ArrayList<Event> events = mEvents;
        int numEvents = events.size();
        if (numEvents == 0) {
mPopup.dismiss();
return;
}

int eventIndex = 0;
        for (int i = 0; i < numEvents; i++) {
            Event event = events.get(i);

if (event.startDay > date || event.endDay < date) {
continue;
}
//Synthetic comment -- @@ -1200,7 +1193,7 @@
if (eventIndex > 5) {
eventIndex = 5;
}
        int popupHeight = 20 * eventIndex + 15;
mPopup.setHeight(popupHeight);

if (mPreviousPopupHeight != popupHeight) {
//Synthetic comment -- @@ -1217,20 +1210,19 @@

switch (keyCode) {
case KeyEvent.KEYCODE_DPAD_CENTER:
            if (mSelectionMode == SELECTION_HIDDEN) {
                // Don't do anything unless the selection is visible.
                break;
            }

            if (mSelectionMode == SELECTION_PRESSED) {
                // This was the first press when there was nothing selected.
                // Change the selection from the "pressed" state to the
                // the "selected" state.  We treat short-press and
                // long-press the same here because nothing was selected.
                mSelectionMode = SELECTION_SELECTED;
                mRedrawScreen = true;
                invalidate();
                break;
}

// Check the duration to determine if this was a short press








//Synthetic comment -- diff --git a/src/com/android/calendar/SelectCalendarsAdapter.java b/src/com/android/calendar/SelectCalendarsAdapter.java
//Synthetic comment -- index 03426b7..6b944a2 100644

//Synthetic comment -- @@ -231,8 +231,8 @@
}
//Collect proper description for account types
mAuthDescs = AccountManager.get(context).getAuthenticatorTypes();
        for (int i = 0; i < mAuthDescs.length; i++) {
            mTypeToAuthDescription.put(mAuthDescs[i].type, mAuthDescs[i]);
}
mView = mActivity.getExpandableListView();
mRefresh = true;








//Synthetic comment -- diff --git a/src/com/android/calendar/Utils.java b/src/com/android/calendar/Utils.java
//Synthetic comment -- index 4a3a37f..70d6a8a 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
private static final TimeZoneUtils mTZUtils = new TimeZoneUtils(SHARED_PREFS_NAME);

public static void startActivity(Context context, String className, long time) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

intent.setClassName(context, className);
intent.putExtra(EVENT_BEGIN_TIME, time);
//Synthetic comment -- @@ -68,7 +68,7 @@
}

static String getSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences prefs = CalendarPreferenceActivity.getSharedPreferences(context);
return prefs.getString(key, defaultValue);
}

//Synthetic comment -- @@ -120,17 +120,17 @@
}

static void setSharedPreference(Context context, String key, String value) {
        SharedPreferences prefs = CalendarPreferenceActivity.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
editor.putString(key, value);
editor.apply();
}

static void setDefaultView(Context context, int viewId) {
        String activityString = CalendarApplication.ACTIVITY_NAMES[viewId];

        SharedPreferences prefs = CalendarPreferenceActivity.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
if (viewId == CalendarApplication.AGENDA_VIEW_ID ||
viewId == CalendarApplication.DAY_VIEW_ID) {
// Record the (new) detail start view only for Agenda and Day
//Synthetic comment -- @@ -143,15 +143,15 @@
}

public static final Time timeFromIntent(Intent intent) {
        Time time = new Time();
time.set(timeFromIntentInMillis(intent));
return time;
}

public static MatrixCursor matrixCursorFromCursor(Cursor cursor) {
        MatrixCursor newCursor = new MatrixCursor(cursor.getColumnNames());
        int numColumns = cursor.getColumnCount();
        String data[] = new String[numColumns];
cursor.moveToPosition(-1);
while (cursor.moveToNext()) {
for (int i = 0; i < numColumns; i++) {
//Synthetic comment -- @@ -173,7 +173,7 @@
return false;
}

        int numColumns = c1.getColumnCount();
if (numColumns != c2.getColumnCount()) {
return false;
}
//Synthetic comment -- @@ -201,14 +201,14 @@
*/
public static final long timeFromIntentInMillis(Intent intent) {
// If the time was specified, then use that.  Otherwise, use the current time.
        Uri data = intent.getData();
long millis = intent.getLongExtra(EVENT_BEGIN_TIME, -1);
if (millis == -1 && data != null && data.isHierarchical()) {
            List<String> path = data.getPathSegments();
if(path.size() == 2 && path.get(0).equals("time")) {
try {
millis = Long.valueOf(data.getLastPathSegment());
                } catch (NumberFormatException e) {
Log.i("Calendar", "timeFromIntentInMillis: Data existed but no valid time " +
"found. Using current time.");
}
//Synthetic comment -- @@ -221,12 +221,12 @@
}

public static final void applyAlphaAnimation(ViewFlipper v) {
        AlphaAnimation in = new AlphaAnimation(0.0f, 1.0f);

in.setStartOffset(0);
in.setDuration(500);

        AlphaAnimation out = new AlphaAnimation(1.0f, 0.0f);

out.setStartOffset(0);
out.setDuration(500);
//Synthetic comment -- @@ -247,11 +247,11 @@
* be even darker.
*/
color &= CLEAR_ALPHA_MASK;
        int startColor = color | HIGH_ALPHA;
        int middleColor = color | MED_ALPHA;
        int endColor = color | LOW_ALPHA;
        int[] colors = new int[] {startColor, middleColor, endColor};
        GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
d.setCornerRadii(CORNERS);
return d;
}
//Synthetic comment -- @@ -288,9 +288,9 @@
* @return a string contained the things joined together
*/
public static String join(List<?> things, String delim) {
        StringBuilder builder = new StringBuilder();
boolean first = true;
        for (Object thing : things) {
if (first) {
first = false;
} else {
//Synthetic comment -- @@ -316,13 +316,11 @@
* @return the first day of week in android.text.format.Time
*/
public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
}
}

//Synthetic comment -- @@ -364,7 +362,7 @@
isDuplicateName.clear();
cursor.moveToPosition(-1);
while (cursor.moveToNext()) {
            String displayName = cursor.getString(nameIndex);
// Set it to true if we've seen this name before, false otherwise
if (displayName != null) {
isDuplicateName.put(displayName, isDuplicateName.containsKey(displayName));








//Synthetic comment -- diff --git a/src/com/android/calendar/WeekActivity.java b/src/com/android/calendar/WeekActivity.java
//Synthetic comment -- index fe90998..aa11f10 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
protected void onNewIntent(Intent intent) {
long timeMillis = Utils.timeFromIntentInMillis(intent);
if (timeMillis > 0) {
            Time time = new Time(Utils.getTimeZone(this, null));
time.set(timeMillis);
goTo(time, false);
}







