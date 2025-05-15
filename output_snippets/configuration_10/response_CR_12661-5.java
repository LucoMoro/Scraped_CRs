//<Beginning of snippet n. 0>

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.graphics.Canvas;
import android.graphics.RectF;
// other necessary imports

// Your existing code...

private void drawEventText(Event event, RectF rf, Canvas canvas, TextPaint p, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }

    float width = rf.right - rf.left;
    float height = rf.bottom - rf.top;

    // If the rectangle is too small for text, then return
    if (width < MIN_CELL_WIDTH_FOR_TEXT || height <= mEventTextHeight) {
        return;
    }

    // Truncate the event title to a known (large enough) limit
    String text = event.getTitleAndLocation();
    text = drawTextSanitizer(text);
    
    if (text.length() > MAX_EVENT_TEXT_LEN) {
        text = text.substring(0, MAX_EVENT_TEXT_LEN);
    }

    // Create StaticLayout for text rendering
    TextPaint textPaint = new TextPaint(p);
    StaticLayout staticLayout = new StaticLayout(text, textPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

    // Validate enough height for drawing the text
    if (height < staticLayout.getHeight()) {
        return;
    }

    // Draw the text using StaticLayout
    canvas.save();
    canvas.translate(rf.left, rf.top + topMargin);
    staticLayout.draw(canvas);
    canvas.restore();
}

// Your existing code...

//<End of snippet n. 0>