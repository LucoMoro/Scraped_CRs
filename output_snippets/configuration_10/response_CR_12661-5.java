//<Beginning of snippet n. 0>

import android.text.StaticLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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

private StaticLayout createStaticLayout(Event event, TextPaint paint, float maxWidth) {
    String text = drawTextSanitizer(event.getTitleAndLocation());
    if (text.isEmpty() || maxWidth <= 0) {
        return null;
    }
    return new StaticLayout(text, paint, (int) maxWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
}

private void drawEventText(Event event, RectF rf, Canvas canvas, TextPaint eventTextPaint, int topMargin) {
    if (!mDrawTextInEventRect) {
        return;
    }

    float width = rf.right - rf.left;
    float height = rf.bottom - rf.top;

    // Leave one pixel extra space between lines
    int lineHeight = mEventTextHeight + 1;

    if (width < MIN_CELL_WIDTH_FOR_TEXT || height <= lineHeight) {
        return;
    }

    StaticLayout layout = createStaticLayout(event, eventTextPaint, width);
    if (layout != null) {
        canvas.save();
        canvas.translate(rf.left + 1, rf.top + topMargin);
        layout.draw(canvas);
        canvas.restore();
    }
}

// ...rest of the code remains unchanged

//<End of snippet n. 0>