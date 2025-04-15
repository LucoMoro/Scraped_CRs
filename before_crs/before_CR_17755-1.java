/*Discarding duplicate part of concatenated SMS message upon reception

If receiving the same part of a concatenated SMS message several times,
the duplicates were all stored. This could lead to a crash further on
since the message could be considered complete before all parts were
actually received, causing some PDUs to be null in the dispatched intent.
The change adds a check to see if the same part is already present, and
if so simply discards the duplicate.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index ca526a5..cb72203 100644

//Synthetic comment -- @@ -550,6 +550,33 @@
*/
protected abstract int dispatchMessage(SmsMessageBase sms);


/**
* If this is the last part send the parts out to the application, otherwise
//Synthetic comment -- @@ -572,6 +599,10 @@
byte[][] pdus = null;
Cursor cursor = null;
try {
cursor = mResolver.query(mRawUri, RAW_PROJECTION, where.toString(), whereArgs, null);
int cursorCount = cursor.getCount();
if (cursorCount != concatRef.msgCount - 1) {







