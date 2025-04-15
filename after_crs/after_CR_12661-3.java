/*Display the daily appointements via StaticLayout instead of manual formatting

Change-Id:I147c166c8fe1614c160ee71ad868b05a6951457f*/




//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 6716edf..4833342 100644

//Synthetic comment -- @@ -46,6 +46,10 @@
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
//Synthetic comment -- @@ -140,6 +144,7 @@
private long mLastReloadMillis;

private ArrayList<Event> mEvents = new ArrayList<Event>();
    private ArrayList<StaticLayout> mLayouts = new ArrayList<StaticLayout>();
private int mSelectionDay;        // Julian day
private int mSelectionHour;

//Synthetic comment -- @@ -265,6 +270,7 @@
private boolean mComputeSelectedEvents;
private Event mSelectedEvent;
private Event mPrevSelectedEvent;
    private StaticLayout mSelectedLayout;
private Rect mPrevBox = new Rect();
protected final Resources mResources;
private String mAmString;
//Synthetic comment -- @@ -526,6 +532,7 @@
mBaseDate.set(time);
mSelectionHour = mBaseDate.hour;
mSelectedEvent = null;
	mSelectedLayout = null;
mPrevSelectedEvent = null;
long millis = mBaseDate.toMillis(false /* use isDst */);
mSelectionDay = Time.getJulianDay(millis, mBaseDate.gmtoff);
//Synthetic comment -- @@ -767,6 +774,7 @@
view.remeasure(getWidth(), getHeight());

view.mSelectedEvent = null;
	view.mSelectedLayout = null;
view.mPrevSelectedEvent = null;
view.mStartDay = mStartDay;
if (view.mEvents.size() > 0) {
//Synthetic comment -- @@ -962,6 +970,7 @@
case KeyEvent.KEYCODE_DPAD_LEFT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextLeft;
		mSelectedLayout = null;
}
if (mSelectedEvent == null) {
selectionDay -= 1;
//Synthetic comment -- @@ -972,6 +981,7 @@
case KeyEvent.KEYCODE_DPAD_RIGHT:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextRight;
		mSelectedLayout = null;
}
if (mSelectedEvent == null) {
selectionDay += 1;
//Synthetic comment -- @@ -982,6 +992,7 @@
case KeyEvent.KEYCODE_DPAD_UP:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextUp;
		mSelectedLayout = null;
}
if (mSelectedEvent == null) {
if (!mSelectionAllDay) {
//Synthetic comment -- @@ -997,6 +1008,7 @@
case KeyEvent.KEYCODE_DPAD_DOWN:
if (mSelectedEvent != null) {
mSelectedEvent = mSelectedEvent.nextDown;
		mSelectedLayout = null;
}
if (mSelectedEvent == null) {
if (mSelectionAllDay) {
//Synthetic comment -- @@ -1054,11 +1066,13 @@
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
//Synthetic comment -- @@ -1157,6 +1171,7 @@
}

mSelectedEvent = null;
	mSelectedLayout = null;
mPrevSelectedEvent = null;
mSelectedEvents.clear();

//Synthetic comment -- @@ -1180,6 +1195,11 @@
mEventLoader.loadEventsInBackground(mNumDays, events, millis, new Runnable() {
public void run() {
mEvents = events;
		mLayouts = new ArrayList<StaticLayout>(events.size()); // Replace the layouts as well
		// Fill the layouts with nulls
		while( mLayouts.size()<events.size() ) {
		    mLayouts.add(null);
		}
mRemeasure = true;
mRedrawScreen = true;
mComputeSelectedEvents = true;
//Synthetic comment -- @@ -1588,6 +1608,46 @@
return box;
}

    /**
     * Return the layout for a numbered event. Create it if not already existing
     */
    private StaticLayout getEventLayout( int i, Event event, Paint paint, RectF rf )
    {
	StaticLayout layout=mLayouts.get(i);

	// Check if we have already initialized the StaticLayout
	if( layout==null ) {
	    // No, we havn't...
	    String text = event.getTitleAndLocation();

	    // XXX Is this really needed when working with a StaticLayout?
	    text=drawTextSanitizer(text);

	    layout=new StaticLayout( text, 0, text.length(), new TextPaint(paint), (int)rf.width()-2,
		    Alignment.ALIGN_NORMAL, (float)1, (float)0.0, true, TextUtils.TruncateAt.END,
		    (int)rf.width()-2 );

	    mLayouts.set(i, layout);
	}

	return layout;
    }

    /**
     * Return the layout matching the currently selected event.
     */
    private StaticLayout getSelectedLayout(Paint paint, RectF rf)
    {
	if( mSelectedLayout!=null )
	    return mSelectedLayout;

	int index=mEvents.indexOf( mSelectedEvent );
	StaticLayout layout=getEventLayout( index, mSelectedEvent, paint, rf );
	mSelectedLayout=layout;

	return layout;
    }

private void drawAllDayEvents(int firstDay, int numDays,
Rect r, Canvas canvas, Paint p) {
p.setTextSize(NORMAL_FONT_SIZE);
//Synthetic comment -- @@ -1662,7 +1722,9 @@
event.bottom = event.top + height * 0.9f;

RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);

	    StaticLayout layout=getEventLayout( i, event, eventTextPaint, rf );
            drawEventText(layout, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);

// Check if this all-day event intersects the selected day
if (mSelectionAllDay && mComputeSelectedEvents) {
//Synthetic comment -- @@ -1679,7 +1741,8 @@
if (mSelectedEvent != null) {
Event event = mSelectedEvent;
RectF rf = drawAllDayEventRect(event, canvas, p, eventTextPaint);
		StaticLayout layout = getSelectedLayout( eventTextPaint, rf );
                drawEventText(layout, rf, canvas, eventTextPaint, ALL_DAY_TEXT_TOP_MARGIN);
}

// Draw the highlight on the selected all-day area
//Synthetic comment -- @@ -1756,8 +1819,10 @@
}
if (startEvent != null) {
mSelectedEvent = startEvent;
	    mSelectedLayout = null;
} else {
mSelectedEvent = maxPositionEvent;
	    mSelectedLayout = null;
}
}

//Synthetic comment -- @@ -1814,7 +1879,8 @@
}

RectF rf = drawEventRect(event, canvas, p, eventTextPaint);
	    StaticLayout layout=getEventLayout( i, event, eventTextPaint, rf );
            drawEventText(layout, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}

if (date == mSelectionDay && !mSelectionAllDay && isFocused()
//Synthetic comment -- @@ -1822,7 +1888,8 @@
computeNeighbors();
if (mSelectedEvent != null) {
RectF rf = drawEventRect(mSelectedEvent, canvas, p, eventTextPaint);
		StaticLayout layout = getSelectedLayout( eventTextPaint, rf );
                drawEventText(layout, rf, canvas, eventTextPaint, NORMAL_TEXT_TOP_MARGIN);
}
}
}
//Synthetic comment -- @@ -2112,6 +2179,7 @@
ev.nextRight = rightEvent;
}
mSelectedEvent = startEvent;
	mSelectedLayout = null;
}


//Synthetic comment -- @@ -2192,94 +2260,26 @@
return string;
}

    private void drawEventText(StaticLayout eventLayout, RectF rf, Canvas canvas, Paint p, int topMargin) {
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
	canvas.translate( rf.left+1, rf.top ); // So the layout happens at the right place

	eventLayout.draw( canvas );

	canvas.restore();
}

private void updateEventDetails() {
//Synthetic comment -- @@ -2850,6 +2850,7 @@
int left = mHoursWidth + (mSelectionDay - mFirstJulianDay) * (cellWidth + DAY_GAP);
int top = 0;
mSelectedEvent = null;
	mSelectedLayout = null;

mSelectedEvents.clear();
if (mSelectionAllDay) {







