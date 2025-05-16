//<Beginning of snippet n. 0>


import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout;
import android.graphics.Canvas;
import android.graphics.RectF;

private long mLastReloadMillis;
private ArrayList<Event> mEvents = new ArrayList<Event>();
private int mSelectionDay;        // Julian day
private int mSelectionHour;

private boolean mComputeSelectedEvents;
private Event mSelectedEvent;
private Event mPrevSelectedEvent;
private Rect mPrevBox = new Rect();
protected final Resources mResources;
private String mAmString;

mBaseDate.set(time);
mSelectionHour = mBaseDate.hour;
mSelectedEvent = null;
mPrevSelectedEvent = null;
long millis = mBaseDate.toMillis(false /* use isDst */);
mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
view.remeasure(getWidth(), getHeight());

view.mSelectedEvent = null;
view.mPrevSelectedEvent = null;
view.mStartDay = mStartDay;

if (view.mEvents.size() > 0) {
    case KeyEvent.KEYCODE_DPAD_LEFT:
        if (mSelectedEvent != null) {
            mSelectedEvent = mSelectedEvent.nextLeft;
        }
        if (mSelectedEvent == null) {
            selectionDay -= 1;
        }
    case KeyEvent.KEYCODE_DPAD_RIGHT:
        if (mSelectedEvent != null) {
            mSelectedEvent = mSelectedEvent.nextRight;
        }
        if (mSelectedEvent == null) {
            selectionDay += 1;
        }
    case KeyEvent.KEYCODE_DPAD_UP:
        if (mSelectedEvent != null) {
            mSelectedEvent = mSelectedEvent.nextUp;
        }
        if (mSelectedEvent == null) {
            if (!mSelectionAllDay) {
    case KeyEvent.KEYCODE_DPAD_DOWN:
        if (mSelectedEvent != null) {
            mSelectedEvent = mSelectedEvent.nextDown;
        }
        if (mSelectedEvent == null) {
            if (mSelectionAllDay) {
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
            }
        }
    }
}

mSelectedEvent = null;
mPrevSelectedEvent = null;
mSelectedEvents.clear();

mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
    public void run() {
        mEvents = events;
        mRemeasure = true;
        mRedrawScreen = true;
        mComputeSelectedEvents = true;
    }
    return box;
});

private void drawAllDayEvents(int firstDay, int numDays,
Rect r, Canvas canvas, Paint p) {
    p.setTextSize(NORMAL_FONT_SIZE);
    event.bottom = event.top + height * 0.9f;

    RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
    drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

    if (mSelectionAllDay && mComputeSelectedEvents) {
        if (mSelectedEvent != null) {
            Event event = mSelectedEvent;
            RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
            drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
        }
    }
}

if (startEvent != null) {
    mSelectedEvent = startEvent;
} else {
    mSelectedEvent = maxPositionEvent;
}

RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
drawEventText(event, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);

if (date == mSelectionDay && !mSelectionAllDay && isFocused()) {
    computeNeighbors();
    if (mSelectedEvent != null) {
        RectF rf = drawEventRect(mSelectedEvent, canvas, p, eventTextPaint);
        drawEventText(mSelectedEvent, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
    }
}

ev.nextRight = rightEvent;
mSelectedEvent = startEvent;

return string;
}

private void drawEventText(Event event, RectF rf, Canvas canvas, TextPaint p, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }

    float width = rf.right - rf.left;
    float height = rf.bottom - rf.top;
    int lineHeight = (int) (mEventTextHeight + 1);

    if (width < MIN_CELL_WIDTH_FOR_TEXT || height <= lineHeight) {
        return;
    }

    String text = event.getTitleAndLocation();
    text = drawTextSanitizer(text);

    int len = text.length();
    if (len > MAX_EVENT_TEXT_LEN) {
        text = text.substring(0, MAX_EVENT_TEXT_LEN);
        len = MAX_EVENT_TEXT_LEN;
    }

    StaticLayout staticLayout = new StaticLayout(text, p, (int) width,
            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
    canvas.save();
    canvas.translate(rf.left + 1, rf.top + topMargin);
    staticLayout.draw(canvas);
    canvas.restore();
}

private void updateEventDetails() {
    int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
    int top = 0;
    mSelectedEvent = null;

    mSelectedEvents.clear();
    if (mSelectionAllDay) {
        
//<End of snippet n. 0>

