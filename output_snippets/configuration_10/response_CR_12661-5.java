//<Beginning of snippet n. 0>


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout;

private void drawAllDayEvents(int firstDay, int numDays,
                               Rect r, Canvas canvas, Paint p) {
    p.setTextSize(NORMAL_FONT_SIZE);
    event.bottom = event.top + height * 0.9f;

    RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
    drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

    // Check if this all-day event intersects the selected day
    if (mSelectionAllDay && mComputeSelectedEvents) {
        if (mSelectedEvent != null) {
            Event event = mSelectedEvent;
            RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
            drawEventText(event, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
        }
    }

    // Draw the highlight on the selected all-day area
    if (startEvent != null) {
        mSelectedEvent = startEvent;
    } else {
        mSelectedEvent = maxPositionEvent;
    }
}

private void drawEventText(Event event, RectF rf, Canvas canvas, TextPaint p, int topMargin) {
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

    // Create StaticLayout for proper text rendering
    StaticLayout staticLayout = new StaticLayout(text, p, 
        (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

    float top = rf.top + mEventTextAscent + topMargin;

    // Draw the text layout on the canvas at the defined rectangle
    canvas.save();
    canvas.translate(rf.left + 1, top);
    staticLayout.draw(canvas);
    canvas.restore();
}

private void updateEventDetails() {
    int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
    int top = 0;
    mSelectedEvent = null;

    mSelectedEvents.clear();
    if (mSelectionAllDay) {
        // Implementation for all-day event details...
    }
}

//<End of snippet n. 0>