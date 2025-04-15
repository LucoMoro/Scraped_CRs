/*Display the daily appointments via a StaticLayout:
1. Much less code
2. Now supports BiDi*/




//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 6716edf..5c0f892 100644

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
//Synthetic comment -- @@ -2197,14 +2201,11 @@
return;
}

// Leave one pixel extra space between lines
int lineHeight = mEventTextHeight + 1;

// If the rectangle is too small for text, then return
        if (rf.width() < MIN_CELL_WIDTH_FOR_TEXT || rf.height() <= lineHeight) {
return;
}

//Synthetic comment -- @@ -2213,73 +2214,17 @@

text = drawTextSanitizer(text);

	// Use a StaticLayout to format the string.
	canvas.save();
	canvas.translate( rf.left+1, rf.top ); // So the layout happens at the right place

	StaticLayout layout=new StaticLayout( text, 0, text.length(), new TextPaint(p), (int)rf.width()-2,
		Alignment.ALIGN_NORMAL, (float)1, (float)0.0, true, TextUtils.TruncateAt.END,
	       	MAX_EVENT_TEXT_LEN );
	
	layout.draw( canvas );

	canvas.restore();
}

private void updateEventDetails() {







