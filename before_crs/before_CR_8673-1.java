/*Remove reminders when event has none (issue 1300). Otherwise they appear on more and more views as the views are recycled.*/
//Synthetic comment -- diff --git a/src/com/android/calendar/AgendaAdapter.java b/src/com/android/calendar/AgendaAdapter.java
//Synthetic comment -- index 07a6bc7..7c51402 100644

//Synthetic comment -- @@ -107,6 +107,9 @@
boolean hasAlarm = cursor.getInt(AgendaActivity.INDEX_HAS_ALARM) != 0;
if (hasAlarm) {
updateReminder(view, context, begin, cursor.getLong(AgendaActivity.INDEX_EVENT_ID));
}

// Where







