/*Truncate and ellipsize at the right place*/
//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarView.java b/src/com/android/calendar/CalendarView.java
//Synthetic comment -- index 5c0f892..496fa90 100644

//Synthetic comment -- @@ -2220,7 +2220,7 @@

	StaticLayout layout=new StaticLayout( text, 0, text.length(), new TextPaint(p), (int)rf.width()-2,
		Alignment.ALIGN_NORMAL, (float)1, (float)0.0, true, TextUtils.TruncateAt.END,
	       	MAX_EVENT_TEXT_LEN );
	
	layout.draw( canvas );








