//<Beginning of snippet n. 0>

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

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
    switch (direction) {
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
    return box;
});

private void drawAllDayEvents(int firstDay, int numDays, Rect r, Canvas canvas, TextPaint p) {
    p.setTextSize(NORMAL_FONT_SIZE);
    event.bottom = event.top + height * 0.9f;

    RectF rf = drawAllDayEventRect(event, canvas, p);
    drawEventText(event, rf, canvas, p, ALL_DAY_TEXT_TOP_MARGIN);

    if (mSelectionAllDay && mComputeSelectedEvents) {
        if (mSelectedEvent != null) {
            Event event = mSelectedEvent;
            RectF rf = drawAllDayEventRect(event, canvas, p);
            drawEventText(event, rf, canvas, p, ALL_DAY_TEXT_TOP_MARGIN);
        }
    } else {
        mSelectedEvent = maxPositionEvent;
    }
}

private RectF drawAllDayEventRect(Event event, Canvas canvas, Paint p) {
    // Implementation here
}

private void drawEventText(Event event, RectF rf, Canvas canvas, TextPaint p, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }
    
    String text = drawTextSanitizer(event.getTitleAndLocation());
    int len = text.length();
    if (len > MAX_EVENT_TEXT_LEN) {
        text = text.substring(0, MAX_EVENT_TEXT_LEN);
        len = MAX_EVENT_TEXT_LEN;
    }
    
    StaticLayout layout = new StaticLayout(text, p, (int) rf.width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    
    float top = rf.top + topMargin;
    
    for (int i = 0; i < layout.getLineCount(); i++) {
        float lineHeight = layout.getLineBottom(i) - layout.getLineTop(i);
        canvas.save();
        canvas.translate(rf.left, top);
        layout.draw(canvas);
        canvas.restore();
        top += lineHeight;
    }
}

private void updateEventDetails() {
    int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
    int top = 0;
    mSelectedEvent = null;

    mSelectedEvents.clear();
    if (mSelectionAllDay) {
        // Additional logic if needed
    }
    mSelectedEvent = closestEvent;
}

//<End of snippet n. 0>