//<Beginning of snippet n. 0>


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.view.KeyEvent;

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

    TextPaint textPaint = new TextPaint(p);
    Layout.Alignment alignment = (textDirection == TextDirection.RTL) ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
    StaticLayout staticLayout = new StaticLayout(text, textPaint, Math.round(rf.width()), alignment, 1.0f, 0.0f, false);
    canvas.save();
    canvas.translate(rf.left + 1, rf.top + topMargin);
    staticLayout.draw(canvas);
    canvas.restore();
}

private void navigateEvents(int direction) {
    if (mSelectedEvent != null) {
        Event nextEvent = null;
        switch (direction) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                nextEvent = mSelectedEvent.nextLeft;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                nextEvent = mSelectedEvent.nextRight;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                nextEvent = mSelectedEvent.nextUp;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                nextEvent = mSelectedEvent.nextDown;
                break;
        }
        
        if (nextEvent != null) {
            mSelectedEvent = nextEvent;
        }
    }
    if (mSelectedEvent == null) {
        mLastPopupEventID = INVALID_EVENT_ID;
    }
}

//<End of snippet n. 0>