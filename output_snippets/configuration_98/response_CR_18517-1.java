//<Beginning of snippet n. 0>


import android.provider.Calendar.Calendars;
import android.provider.Calendar.Events;
import android.text.TextUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;

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
    switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextLeft;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextRight;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextUp;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextDown;
            }
            break;
    }
    
    if (mSelectedEvent == null) {
        mLastPopupEventID = INVALID_EVENT_ID;
    }
}

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

mPrevSelectedEvent = null;
mSelectedEvents.clear();

Time weekStart = new Time();
weekStart.set(mBaseDate);
mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
    public void run() {
        mEvents = events;
        mRemeasure = true;
        mRedrawScreen = true;
        mComputeSelectedEvents = true;
    }
});

private void drawAllDayEvents(int firstDay, int numDays, Rect r, Canvas canvas, Paint p) {
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
    } else {
        mSelectedEvent = maxPositionEvent;
    }
}

RectF drawAllDayEventRect(Event event, Canvas canvas, Paint p, Paint eventTextPaint) {
    // Implementation details here
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

private void drawEventText(Event event, RectF rf, Canvas canvas, Paint p, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }
    
    String text = event.getTitleAndLocation();
    text = drawTextSanitizer(text);

    int len = text.length();
    if (len > MAX_EVENT_TEXT_LEN) {
        text = text.substring(0, MAX_EVENT_TEXT_LEN);
        len = MAX_EVENT_TEXT_LEN;
    }

    p.getTextWidths(text, mCharWidths);
    String fragment = text;
    float top = rf.top + mEventTextAscent + topMargin;
    int start = 0;

    while (start < len && height >= (lineHeight + 1)) {
        boolean lastLine = (height < 2 * lineHeight + 1);
        do {
            char c = text.charAt(start);
            if (c != ' ') break;
            start += 1;
        } while (start < len);

        float sum = 0;
        int end = start;
        for (int ii = start; ii < len; ii++) {
            char c = text.charAt(ii);
            if (c == ' ') {
                end = ii;
            }
            sum += mCharWidths[ii];
            if (sum > width) {
                if (end > start && !lastLine) {
                    fragment = text.substring(start, end);
                    start = end;
                    break;
                }
                fragment = text.substring(start, ii);
                start = ii;
                break;
            }
        }

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
    int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
    int top = 0;
    mSelectedEvent = null;

    mSelectedEvents.clear();
    if (mSelectionAllDay) {
        // Additional handling for all day selection
    }
    mSelectedEvent = closestEvent;
}

//<End of snippet n. 0>