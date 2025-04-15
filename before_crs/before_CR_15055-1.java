/*Making sure CalendarProvider closes cursor even if the cursor count == 0 after query

Change-Id:I57e827242ed590185e9183f2b295629ed8ed3e25*/
//Synthetic comment -- diff --git a/src/com/android/providers/calendar/CalendarProvider.java b/src/com/android/providers/calendar/CalendarProvider.java
//Synthetic comment -- index 4bc51dc..917b5d1 100644

//Synthetic comment -- @@ -3237,13 +3237,15 @@
Account account = null;
String calendarUrl = null;
boolean oldSyncEvents = false;
        if (cursor != null && cursor.moveToFirst()) {
try {
                final String accountName = cursor.getString(0);
                final String accountType = cursor.getString(1);
                account = new Account(accountName, accountType);
                calendarUrl = cursor.getString(2);
                oldSyncEvents = (cursor.getInt(3) != 0);
} finally {
cursor.close();
}







