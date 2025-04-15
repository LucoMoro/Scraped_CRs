/*Draw events through a StaticLayout.

Less code, and supports Bidi much better.

Change-Id:If6158493f0125824099cc42e2b3dd786b29fa900*/




//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index da17af6..a81fa94 100644

//Synthetic comment -- @@ -43,6 +43,10 @@
import android.provider.Calendar.Calendars;
import android.provider.Calendar.Events;
import android.text.TextUtils;
import android.text.StaticLayout;
import android.text.Layout.Alignment;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
//Synthetic comment -- @@ -159,6 +163,7 @@
private long mLastReloadMillis;

private ArrayList<Event> mEvents = new ArrayList<Event>();
    private ArrayList<StaticLayout> mLayouts = new ArrayList<StaticLayout>();
private int mSelectionDay;        // Julian day
private int mSelectionHour;

//Synthetic comment -- @@ -294,6 +299,8 @@
private boolean mComputeSelectedEvents;
private Event mSelectedEvent;
private Event mPrevSelectedEvent;
    // Cache of mSelectedEvent's corresponding StaticLayout. Set this to null whenever you change mSelectedEvent
    private StaticLayout mSelectedLayout;
private Rect mPrevBox = new Rect();
protected final Resources mResources;
private String mAmString;
//Synthetic comment -- @@ -570,6 +577,7 @@
mBaseDate.set(time);
mSelectionHour = mBaseDate.hour;
mSelectedEvent = null;
        mSelectedLayout = null;
mPrevSelectedEvent = null;
long millis = mBaseDate.toMillis(false /* use isDst */);
mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
//Synthetic comment -- @@ -814,6 +822,7 @@
view.remeasure(getWidth(), getHeight());

view.mSelectedEvent = null;
        view.mSelectedLayout = null;
view.mPrevSelectedEvent = null;
view.mStartDay = mStartDay;
if (view.mEvents.size() > 0) {
//Synthetic comment -- @@ -1011,6 +1020,7 @@
case KeyEvent.KEYCODE_DPAD_LEFT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextLeft;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1022,6 +1032,7 @@
case KeyEvent.KEYCODE_DPAD_RIGHT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextRight;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1033,6 +1044,7 @@
case KeyEvent.KEYCODE_DPAD_UP:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextUp;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1049,6 +1061,7 @@
case KeyEvent.KEYCODE_DPAD_DOWN:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextDown;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
mLastPopupEventID = INVALID_EVENT_ID;
//Synthetic comment -- @@ -1107,11 +1120,13 @@
if (mSelectionHour < mFirstHour + 1) {
mSelectionHour = mFirstHour + 1;
mSelectedEvent = null;
            mSelectedLayout = null;
mSelectedEvents.clear();
mComputeSelectedEvents = true;
} else if (mSelectionHour > mFirstHour + mNumHours - 3) {
mSelectionHour = mFirstHour + mNumHours - 3;
mSelectedEvent = null;
            mSelectedLayout = null;
mSelectedEvents.clear();
mComputeSelectedEvents = true;
}
//Synthetic comment -- @@ -1213,6 +1228,8 @@
mPrevSelectedEvent = null;
mSelectedEvents.clear();

        mSelectedLayout = null;

// The start date is the beginning of the week at 12am
Time weekStart = new Time();
weekStart.set(mBaseDate);
//Synthetic comment -- @@ -1233,6 +1250,11 @@
mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
public void run() {
mEvents = events;
                mLayouts = new ArrayList<StaticLayout>(events.size()); // New events, new layouts
                // Fill the layouts with nulls
                while (mLayouts.size() < events.size()) {
                    mLayouts.add(null);
                }
mRemeasure = true;
mRedrawScreen = true;
mComputeSelectedEvents = true;
//Synthetic comment -- @@ -1681,6 +1703,45 @@
return box;
}

    /**
     * Return the layout for a numbered event. Create it if not already existing
     */
    private StaticLayout getEventLayout(int i, Event event, Paint paint, RectF rf) {
        StaticLayout layout = mLayouts.get(i);

        // Check if we have already initialized the StaticLayout
        if (layout == null) {
            // No, we haven't...
            String text = event.getTitleAndLocation();

            // XXX Is this really needed when working with a StaticLayout?
            text = drawTextSanitizer(text);

            // Leave a one pixel boundary on the left and right of the rectangle for the event
            layout = new StaticLayout(text, 0, text.length(), new TextPaint(paint),
                    (int) rf.width() - 2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true,
                    TextUtils.TruncateAt.END, (int) rf.width() - 2);

            mLayouts.set(i, layout);
        }

        return layout;
    }

    /**
     * Return the layout matching the currently selected event.
     */
    private StaticLayout getSelectedLayout(Paint paint, RectF rf) {
        if (mSelectedLayout != null) {
            return mSelectedLayout;
        }

        int index = mEvents.indexOf(mSelectedEvent);
        mSelectedLayout = getEventLayout(index, mSelectedEvent, paint, rf);

        return mSelectedLayout;
    }

private void drawAllDayEvents(int firstDay, int numDays,
Rect r, Canvas canvas, Paint p) {
p.setTextSize(NORMAL_FONT_SIZE);
//Synthetic comment -- @@ -1755,7 +1816,8 @@
event.bottom = event.top + height * 0.9f;

RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
            StaticLayout layout = getEventLayout(i, event, eventTextPaint, rf);
            drawEventText(layout, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

// Check if this all-day event intersects the selected day
if (mSelectionAllDay && mComputeSelectedEvents) {
//Synthetic comment -- @@ -1772,7 +1834,8 @@
if (mSelectedEvent != null) {
Event event = mSelectedEvent;
RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
                StaticLayout layout = getSelectedLayout(eventTextPaint, rf);
                drawEventText(layout, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
}

// Draw the highlight on the selected all-day area
//Synthetic comment -- @@ -1852,6 +1915,7 @@
} else {
mSelectedEvent = maxPositionEvent;
}
        mSelectedLayout = null;
}

RectF drawAllDayEventRect(Event event, Canvas canvas, Paint p, Paint eventTextPaint) {
//Synthetic comment -- @@ -1907,7 +1971,8 @@
}

RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
            StaticLayout layout = getEventLayout(i, event, eventTextPaint, rf);
            drawEventText(layout, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}

if (date == mSelectionDay && !mSelectionAllDay && isFocused()
//Synthetic comment -- @@ -1915,7 +1980,8 @@
computeNeighbors();
if (mSelectedEvent != null) {
RectF rf = drawEventRect(mSelectedEvent, canvas, p, eventTextPaint);
                StaticLayout layout = getSelectedLayout(eventTextPaint, rf);
                drawEventText(layout, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}
}
}
//Synthetic comment -- @@ -2205,6 +2271,7 @@
ev.nextRight = rightEvent;
}
mSelectedEvent = startEvent;
        mSelectedLayout = null;
}


//Synthetic comment -- @@ -2285,7 +2352,7 @@
return string;
}

    private void drawEventText(StaticLayout eventLayout, RectF rf, Canvas canvas, Paint p, int topMargin) {
if (!mDrawTextInEventRect) {
return;
}
//Synthetic comment -- @@ -2301,78 +2368,15 @@
return;
}

        // Use a StaticLayout to format the string.
        canvas.save();
        canvas.translate(rf.left + 1, rf.top ); // So the layout draw happens at the right place
        // When creating the layout, we defined a width that was -2 from the rect to leave a border.
        // We now draw at +1 so that border will be equal on the right and left.

        eventLayout.draw(canvas);

        canvas.restore();
}

private void updateEventDetails() {
//Synthetic comment -- @@ -2966,6 +2970,7 @@
int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
int top = 0;
mSelectedEvent = null;
        mSelectedLayout = null;

mSelectedEvents.clear();
if (mSelectionAllDay) {
//Synthetic comment -- @@ -3053,6 +3058,7 @@
}
}
mSelectedEvent = closestEvent;
            mSelectedLayout = null;

// Keep the selected hour and day consistent with the selected
// event.  They could be different if we touched on an empty hour







