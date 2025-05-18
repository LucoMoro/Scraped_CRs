//<Beginning of snippet n. 0>

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;

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
private StaticLayout eventTextLayout;

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
    switch (event.getKeyCode()) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextLeft;
            }
            if (mSelectedEvent == null) {
                mSelectionDay -= 1;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextRight;
            }
            if (mSelectedEvent == null) {
                mSelectionDay += 1;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            if (mSelectedEvent != null) {
                mSelectedEvent = mSelectedEvent.nextUp;
            }
            if (mSelectedEvent == null) {
                if (!mSelectionAllDay) {
                    // Handle selection for non-all-day events
                }
            }
            break;
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
                });
                return box;
            }
        }
    }

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
    }
    
    if (startEvent != null) {
        mSelectedEvent = startEvent;
    } else {
        mSelectedEvent = maxPositionEvent;
    }
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

mSelectedEvent = startEvent;

return string;
}

private void drawEventText(Event event, RectF rf, Canvas canvas, Paint p, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }

    float width = rf.right - rf.left;
    float height = rf.bottom - rf.top;

    if (width < MIN_CELL_WIDTH_FOR_TEXT || height <= 0) {
        return;
    }

    String text = event.getTitleAndLocation();
    text = drawTextSanitizer(text);

    if (text.length() > MAX_EVENT_TEXT_LEN) {
        text = text.substring(0, MAX_EVENT_TEXT_LEN);
    }

    TextPaint textPaint = new TextPaint(p);
    eventTextLayout = new StaticLayout(text, textPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

    float top = rf.top + mEventTextAscent + topMargin;

    canvas.save();
    canvas.translate(rf.left + 1, top);
    eventTextLayout.draw(canvas);
    canvas.restore();
}

private void updateEventDetails() {
    int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
    int top = 0;
    mSelectedEvent = null;

    mSelectedEvents.clear();
    if (mSelectionAllDay) {
        // Placeholder for logic handling all day events
    }
}

//<End of snippet n. 0>