/*Use name and number for SMS delivery reports

Switched SMS delivery reports to display both
name and number of the contact instead of just
displaying their number.

I also switched to using Context.getString() instead
of String.format and also added brackets to the if
statements to follow the Android coding style.

Change-Id:Icb95712714523c2ac465835eeb94a99865fae0c3Signed-off-by: Alexander Blom <alexvem@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index 160b24e..0ed2466 100644

//Synthetic comment -- @@ -340,18 +340,22 @@
SMS_STATUS_PROJECTION, NEW_DELIVERY_SM_CONSTRAINT,
null, Sms.DATE);

        if (cursor == null)
return null;

try {
            if (!cursor.moveToLast())
            return null;

String address = cursor.getString(COLUMN_SMS_ADDRESS);
long timeMillis = 3000;

            return new MmsSmsDeliveryInfo(String.format(
                context.getString(R.string.delivery_toast_body), address),
timeMillis);

} finally {







