//<Beginning of snippet n. 0>

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.Time;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;

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
private StaticLayout mStaticLayout;

private void updateSelection(long time) {
    mBaseDate.set(time);
    mSelectionHour = mBaseDate.hour;
    mSelectedEvent = null;
    mPrevSelectedEvent = null;
    long millis = mBaseDate.toMillis(false /* use isDst */);
    mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
    view.remeasure(getWidth(), getHeight());
}

private void handleKeyEvent(int keyCode) {
    switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextLeft;
            } else {
                mSelectionDay -= 1;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextRight;
            } else {
                mSelectionDay += 1;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextUp;
            } else if (!mSelectionAllDay) {
                handleVerticalSelection(KeyEvent.KEYCODE_DPAD_DOWN);
            }
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            handleVerticalSelection(KeyEvent.KEYCODE_DPAD_DOWN);
            break;
    }
}

private void handleVerticalSelection(int keyCode) {
    if (mSelectedEvent != null) {
        mSelectedEvent = mSelectedEvent.nextDown;
    }
    if (mSelectedEvent == null) {
        if (mSelectionAllDay) {
            adjustSelectionHour();
        }
        resetSelection();
    }
}

private void adjustSelectionHour() {
    if (mSelectionHour < mFirstHour + 1) {
        mSelectionHour = mFirstHour + 1;
    } else if (mSelectionHour > mFirstHour + mNumHours - 3) {
        mSelectionHour = mFirstHour + mNumHours - 3;
    }
}

private void resetSelection() {
    mSelectedEvent = null;
    mPrevSelectedEvent = null;
    mSelectedEvents.clear();
}

private void loadEventsInBackground(long millis) {
    mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
        public void run() {
            mEvents = events;
            mRemeasure = true;
            mRedrawScreen = true;
            mComputeSelectedEvents = true;
        }
    });
}

private void drawAllDayEvents(int firstDay, int numDays, Rect r, Canvas canvas, Paint p) {
    for (Event event : mEvents) {
        drawEventRectAndText(event, canvas, p);
    }
}

private void drawEventRectAndText(Event event, Canvas canvas, Paint p) {
    RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
    if (mSelectionAllDay && mComputeSelectedEvents) {
        drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
    }
}

private void drawEventText(Event event, RectF rf, Canvas canvas, TextPaint p, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }

    String text = event.getTitleAndLocation();
    text = drawTextSanitizer(text);
    if (text.length() > MAX_EVENT_TEXT_LEN) {
        text = text.substring(0, MAX_EVENT_TEXT_LEN);
    }

    mStaticLayout = new StaticLayout(text, p, (int) rf.width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    
    float top = rf.top + mEventTextAscent + topMargin;

    canvas.save();
    canvas.translate(rf.left + 1, top);
    mStaticLayout.draw(canvas);
    canvas.restore();
}

//<End of snippet n. 0>