/*Fixed a npe in Calendar on Emulator

This happened when working in the emulator. Open the calendar,
add a new Event with standard values (only enter a description)
and click done. A new Notification will pop up with this newly
created event. Click on it, and then on the Eventdetail.
A NPE will occur, because the mCalendarOwnerAccount seems to
be null in the emulator. Now the variable is reset to ""
if it is null (like the initial value). When there are attempts
to save this variable, there's already a check if != "", so it will
not be stored. The other calls to this variable are only .equals()

Change-Id:I0a27547a3849c260d12310771992df496d3b2e94*/




//Synthetic comment -- diff --git a/src/com/android/calendar/EventInfoActivity.java b/src/com/android/calendar/EventInfoActivity.java
//Synthetic comment -- index aec7953..3371214 100644

//Synthetic comment -- @@ -329,7 +329,8 @@
mCalendarOwnerAccount = "";
if (mCalendarsCursor != null) {
mCalendarsCursor.moveToFirst();
            String tempAccount = mCalendarsCursor.getString(CALENDARS_INDEX_OWNER_ACCOUNT);
            mCalendarOwnerAccount = (tempAccount == null) ? "" : tempAccount;
}
String eventOrganizer = mEventCursor.getString(EVENT_INDEX_ORGANIZER);
mIsOrganizer = mCalendarOwnerAccount.equals(eventOrganizer);







