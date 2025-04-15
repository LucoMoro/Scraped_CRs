/*Draw events through a StaticLayout.

Less code, and supports Bidi much better (i.e. - BiDi is now supported).

Change-Id:Id279ceaf66abc8290cde9ce1b9e5e94ad551b6ea*/
//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 1a2e787..eb8a7fe 100644

//Synthetic comment -- @@ -43,6 +43,10 @@
import android.provider.Calendar.Calendars;
import android.provider.Calendar.Events;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
//Synthetic comment -- @@ -159,6 +163,7 @@
private long mLastReloadMillis;

private ArrayList<Event> mEvents = new ArrayList<Event>();
private int mSelectionDay;        // Julian day
private int mSelectionHour;

//Synthetic comment -- @@ -294,6 +299,8 @@
private boolean mComputeSelectedEvents;
private Event mSelectedEvent;
private Event mPrevSelectedEvent;
private Rect mPrevBox = new Rect();
protected final Resources mResources;
private String mAmString;
//Synthetic comment -- @@ -570,6 +577,7 @@
mBaseDate.set(time);
mSelectionHour = mBaseDate.hour;
mSelectedEvent = null;
mPrevSelectedEvent = null;
long millis = mBaseDate.toMillis(false /* use isDst */);
mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
//Synthetic comment -- @@ -814,6 +822,7 @@
view.remeasure(getWidth(), getHeight());

view.mSelectedEvent = null;
view.mPrevSelectedEvent = null;
view.mStartDay = mStartDay;
if (view.mEvents.size() > 0) {
//Synthetic comment -- @@ -1011,6 +1020,7 @@
case KeyEvent.KEYCODE_DPAD_LEFT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextLeft;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1022,6 +1032,7 @@
case KeyEvent.KEYCODE_DPAD_RIGHT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextRight;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1033,6 +1044,7 @@
case KeyEvent.KEYCODE_DPAD_UP:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextUp;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1049,6 +1061,7 @@
case KeyEvent.KEYCODE_DPAD_DOWN:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextDown;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1107,11 +1120,13 @@
if (mSelectionHour < mFirstHour + 1) {
mSelectionHour = mFirstHour + 1;
mSelectedEvent = null;
mSelectedEvents.clear();
mComputeSelectedEvents = true;
} else if (mSelectionHour > mFirstHour + mNumHours - 3) {
mSelectionHour = mFirstHour + mNumHours - 3;
mSelectedEvent = null;
mSelectedEvents.clear();
mComputeSelectedEvents = true;
}
//Synthetic comment -- @@ -1213,6 +1228,8 @@
mPrevSelectedEvent = null;
mSelectedEvents.clear();

// The start date is the beginning of the week at 12am
Time weekStart = new Time();
weekStart.set(mBaseDate);
//Synthetic comment -- @@ -1233,6 +1250,11 @@
mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
public void run() {
mEvents = events;
mRemeasure = true;
mRedrawScreen = true;
mComputeSelectedEvents = true;
//Synthetic comment -- @@ -1681,6 +1703,45 @@
return box;
}

private void drawAllDayEvents(int firstDay, int numDays,
Rect r, Canvas canvas, Paint p) {
p.setTextSize(NORMAL_FONT_SIZE);
//Synthetic comment -- @@ -1755,7 +1816,8 @@
event.bottom = event.top + height * 0.9f;

RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
            drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

// Check if this all-day event intersects the selected day
if (mSelectionAllDay && mComputeSelectedEvents) {
//Synthetic comment -- @@ -1772,7 +1834,8 @@
if (mSelectedEvent != null) {
Event event = mSelectedEvent;
RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
                drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
}

// Draw the highlight on the selected all-day area
//Synthetic comment -- @@ -1852,6 +1915,7 @@
} else {
mSelectedEvent = maxPositionEvent;
}
}

RectF drawAllDayEventRect(Event event, Canvas canvas, Paint p, Paint eventTextPaint) {
//Synthetic comment -- @@ -1907,7 +1971,8 @@
}

RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
            drawEventText(event, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}

if (date == mSelectionDay && !mSelectionAllDay && isFocused()
//Synthetic comment -- @@ -1915,7 +1980,8 @@
computeNeighbors();
if (mSelectedEvent != null) {
RectF rf = drawEventRect(mSelectedEvent, canvas, p, eventTextPaint);
                drawEventText(mSelectedEvent, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}
}
}
//Synthetic comment -- @@ -2205,6 +2271,7 @@
ev.nextRight = rightEvent;
}
mSelectedEvent = startEvent;
}


//Synthetic comment -- @@ -2285,7 +2352,7 @@
return string;
}

    private void drawEventText(Event event, RectF rf, Canvas canvas, Paint p, int topMargin) {
if (!mDrawTextInEventRect) {
return;
}
//Synthetic comment -- @@ -2301,78 +2368,15 @@
return;
}

        // Truncate the event title to a known (large enough) limit
        String text = event.getTitleAndLocation();

        text = drawTextSanitizer(text);

        int len = text.length();
        if (len > MAX_EVENT_TEXT_LEN) {
            text = text.substring(0, MAX_EVENT_TEXT_LEN);
            len = MAX_EVENT_TEXT_LEN;
        }

        // Figure out how much space the event title will take, and create a
        // String fragment that will fit in the rectangle.  Use multiple lines,
        // if available.
        p.getTextWidths(text, mCharWidths);
        String fragment = text;
        float top = rf.top + mEventTextAscent + topMargin;
        int start = 0;

        // Leave one pixel extra space at the bottom
        while (start < len && height >= (lineHeight + 1)) {
            boolean lastLine = (height < 2 * lineHeight + 1);
            // Skip leading spaces at the beginning of each line
            do {
                char c = text.charAt(start);
                if (c != ' ') break;
                start += 1;
            } while (start < len);

            float sum = 0;
            int end = start;
            for (int ii = start; ii < len; ii++) {
                char c = text.charAt(ii);

                // If we found the end of a word, then remember the ending
                // position.
                if (c == ' ') {
                    end = ii;
                }
                sum += mCharWidths[ii];
                // If adding this character would exceed the width and this
                // isn't the last line, then break the line at the previous
                // word.  If there was no previous word, then break this word.
                if (sum > width) {
                    if (end > start && !lastLine) {
                        // There was a previous word on this line.
                        fragment = text.substring(start, end);
                        start = end;
                        break;
                    }

                    // This is the only word and it is too long to fit on
                    // the line (or this is the last line), so take as many
                    // characters of this word as will fit.
                    fragment = text.substring(start, ii);
                    start = ii;
                    break;
                }
            }

            // If sum <= width, then we can fit the rest of the text on
            // this line.
            if (sum <= width) {
                fragment = text.substring(start, len);
                start = len;
            }

            canvas.drawText(fragment, rf.left + 1, top, p);

            top += lineHeight;
            height -= lineHeight;
        }
}

private void updateEventDetails() {
//Synthetic comment -- @@ -2965,6 +2969,7 @@
int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
int top = 0;
mSelectedEvent = null;

mSelectedEvents.clear();
if (mSelectionAllDay) {
//Synthetic comment -- @@ -3052,6 +3057,7 @@
}
}
mSelectedEvent = closestEvent;

// Keep the selected hour and day consistent with the selected
// event.  They could be different if we touched on an empty hour







