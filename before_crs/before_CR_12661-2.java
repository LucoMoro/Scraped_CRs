/*Display the daily appointements via StaticLayout instead of manual formatting*/
//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 6716edf..4833342 100644

//Synthetic comment -- @@ -46,6 +46,10 @@
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
//Synthetic comment -- @@ -140,6 +144,7 @@
private long mLastReloadMillis;

private ArrayList<Event> mEvents = new ArrayList<Event>();
private int mSelectionDay;        // Julian day
private int mSelectionHour;

//Synthetic comment -- @@ -265,6 +270,7 @@
private boolean mComputeSelectedEvents;
private Event mSelectedEvent;
private Event mPrevSelectedEvent;
private Rect mPrevBox = new Rect();
protected final Resources mResources;
private String mAmString;
//Synthetic comment -- @@ -526,6 +532,7 @@
mBaseDate.set(time);
mSelectionHour = mBaseDate.hour;
mSelectedEvent = null;
mPrevSelectedEvent = null;
long millis = mBaseDate.toMillis(false /* use isDst */);
mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
//Synthetic comment -- @@ -767,6 +774,7 @@
view.remeasure(getWidth(), getHeight());

view.mSelectedEvent = null;
view.mPrevSelectedEvent = null;
view.mStartDay = mStartDay;
if (view.mEvents.size() > 0) {
//Synthetic comment -- @@ -962,6 +970,7 @@
case KeyEvent.KEYCODE_DPAD_LEFT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextLeft;
}
if (mSelectedEvent == null) {
selectionDay -= 1;
//Synthetic comment -- @@ -972,6 +981,7 @@
case KeyEvent.KEYCODE_DPAD_RIGHT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextRight;
}
if (mSelectedEvent == null) {
selectionDay += 1;
//Synthetic comment -- @@ -982,6 +992,7 @@
case KeyEvent.KEYCODE_DPAD_UP:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextUp;
}
if (mSelectedEvent == null) {
if (!mSelectionAllDay) {
//Synthetic comment -- @@ -997,6 +1008,7 @@
case KeyEvent.KEYCODE_DPAD_DOWN:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextDown;
}
if (mSelectedEvent == null) {
if (mSelectionAllDay) {
//Synthetic comment -- @@ -1054,11 +1066,13 @@
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
//Synthetic comment -- @@ -1157,6 +1171,7 @@
}

mSelectedEvent = null;
mPrevSelectedEvent = null;
mSelectedEvents.clear();

//Synthetic comment -- @@ -1180,6 +1195,11 @@
mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
public void run() {
mEvents = events;
mRemeasure = true;
mRedrawScreen = true;
mComputeSelectedEvents = true;
//Synthetic comment -- @@ -1588,6 +1608,46 @@
return box;
}

private void drawAllDayEvents(int firstDay, int numDays,
Rect r, Canvas canvas, Paint p) {
p.setTextSize(NORMAL_FONT_SIZE);
//Synthetic comment -- @@ -1662,7 +1722,9 @@
event.bottom = event.top + height * 0.9f;

RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
            drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

// Check if this all-day event intersects the selected day
if (mSelectionAllDay && mComputeSelectedEvents) {
//Synthetic comment -- @@ -1679,7 +1741,8 @@
if (mSelectedEvent != null) {
Event event = mSelectedEvent;
RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
                drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
}

// Draw the highlight on the selected all-day area
//Synthetic comment -- @@ -1756,8 +1819,10 @@
}
if (startEvent != null) {
mSelectedEvent = startEvent;
} else {
mSelectedEvent = maxPositionEvent;
}
}

//Synthetic comment -- @@ -1814,7 +1879,8 @@
}

RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
            drawEventText(event, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}

if (date == mSelectionDay && !mSelectionAllDay && isFocused()
//Synthetic comment -- @@ -1822,7 +1888,8 @@
computeNeighbors();
if (mSelectedEvent != null) {
RectF rf = drawEventRect(mSelectedEvent, canvas, p, eventTextPaint);
                drawEventText(mSelectedEvent, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}
}
}
//Synthetic comment -- @@ -2112,6 +2179,7 @@
ev.nextRight = rightEvent;
}
mSelectedEvent = startEvent;
}


//Synthetic comment -- @@ -2192,94 +2260,26 @@
return string;
}

    private void drawEventText(Event event, RectF rf, Canvas canvas, Paint p, int topMargin) {
if (!mDrawTextInEventRect) {
return;
}

        float width = rf.right - rf.left;
        float height = rf.bottom - rf.top;

// Leave one pixel extra space between lines
int lineHeight = mEventTextHeight + 1;

// If the rectangle is too small for text, then return
        if (width < MIN_CELL_WIDTH_FOR_TEXT || height <= lineHeight) {
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
//Synthetic comment -- @@ -2850,6 +2850,7 @@
int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
int top = 0;
mSelectedEvent = null;

mSelectedEvents.clear();
if (mSelectionAllDay) {







