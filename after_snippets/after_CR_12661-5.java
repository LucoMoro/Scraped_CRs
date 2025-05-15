
//<Beginning of snippet n. 0>


import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.TextUtils;
import android.text.StaticLayout;
import android.text.Layout.Alignment;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
private long mLastReloadMillis;

private ArrayList<Event> mEvents = new ArrayList<Event>();
    private ArrayList<StaticLayout> mLayouts = new ArrayList<StaticLayout>();
private int mSelectionDay;        // Julian day
private int mSelectionHour;

private boolean mComputeSelectedEvents;
private Event mSelectedEvent;
private Event mPrevSelectedEvent;
    private StaticLayout mSelectedLayout;
private Rect mPrevBox = new Rect();
protected final Resources mResources;
private String mAmString;
mBaseDate.set(time);
mSelectionHour = mBaseDate.hour;
mSelectedEvent = null;
        mSelectedLayout = null;
mPrevSelectedEvent = null;
long millis = mBaseDate.toMillis(false /* use isDst */);
mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
view.remeasure(getWidth(), getHeight());

view.mSelectedEvent = null;
        view.mSelectedLayout = null;
view.mPrevSelectedEvent = null;
view.mStartDay = mStartDay;
if (view.mEvents.size() > 0) {
case KeyEvent.KEYCODE_DPAD_LEFT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextLeft;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
selectionDay -= 1;
case KeyEvent.KEYCODE_DPAD_RIGHT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextRight;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
selectionDay += 1;
case KeyEvent.KEYCODE_DPAD_UP:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextUp;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
if (!mSelectionAllDay) {
case KeyEvent.KEYCODE_DPAD_DOWN:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextDown;
                mSelectedLayout = null;
}
if (mSelectedEvent == null) {
if (mSelectionAllDay) {
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
}

mSelectedEvent = null;
        mSelectedLayout = null;
mPrevSelectedEvent = null;
mSelectedEvents.clear();

mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
public void run() {
mEvents = events;
                mLayouts = new ArrayList<StaticLayout>(events.size()); // Replace layouts as well
                // Fill the layouts with nulls
                while (mLayouts.size() < events.size()) {
                    mLayouts.add(null);
                }
mRemeasure = true;
mRedrawScreen = true;
mComputeSelectedEvents = true;
return box;
}

    /**
     * Return the layout for a numbered event. Create it if not already existing
     */
    private StaticLayout getEventLayout(int i, Event event, Paint paint, RectF rf) {
        StaticLayout layout=mLayouts.get(i);

        // Check if we have already initialized the StaticLayout
        if (layout == null) {
            // No, we haven't...
            String text = event.getTitleAndLocation();

            // XXX Is this really needed when working with a StaticLayout?
            text=drawTextSanitizer(text);

            layout=new StaticLayout(text, 0, text.length(), new TextPaint(paint), (int)rf.width()-2,
                    Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true, TextUtils.TruncateAt.END,
                    (int)rf.width()-2);

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
event.bottom = event.top + height * 0.9f;

RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);

            StaticLayout layout=getEventLayout(i, event, eventTextPaint, rf);
            drawEventText(layout, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

// Check if this all-day event intersects the selected day
if (mSelectionAllDay && mComputeSelectedEvents) {
if (mSelectedEvent != null) {
Event event = mSelectedEvent;
RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
                StaticLayout layout = getSelectedLayout(eventTextPaint, rf);
                drawEventText(layout, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
}

// Draw the highlight on the selected all-day area
}
if (startEvent != null) {
mSelectedEvent = startEvent;
            mSelectedLayout = null;
} else {
mSelectedEvent = maxPositionEvent;
            mSelectedLayout = null;
}
}

}

RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
            StaticLayout layout=getEventLayout(i, event, eventTextPaint, rf);
            drawEventText(layout, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}

if (date == mSelectionDay && !mSelectionAllDay && isFocused()
computeNeighbors();
if (mSelectedEvent != null) {
RectF rf = drawEventRect(mSelectedEvent, canvas, p, eventTextPaint);
                StaticLayout layout = getSelectedLayout(eventTextPaint, rf);
                drawEventText(layout, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}
}
}
ev.nextRight = rightEvent;
}
mSelectedEvent = startEvent;
        mSelectedLayout = null;
}


return string;
}

    private void drawEventText(
            StaticLayout eventLayout, RectF rf, Canvas canvas, Paint p, int topMargin) {
if (!mDrawTextInEventRect) {
return;
}

// Leave one pixel extra space between lines
int lineHeight = mEventTextHeight + 1;

// If the rectangle is too small for text, then return
        if (rf.width() < MIN_CELL_WIDTH_FOR_TEXT || rf.height() <= lineHeight) {
return;
}

        // Use a StaticLayout to format the string.
        canvas.save();
        canvas.translate(rf.left+1, rf.top); // So the layout happens at the right place

        eventLayout.draw(canvas);

        canvas.restore();
}

private void updateEventDetails() {
int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
int top = 0;
mSelectedEvent = null;
        mSelectedLayout = null;

mSelectedEvents.clear();
if (mSelectionAllDay) {

//<End of snippet n. 0>








