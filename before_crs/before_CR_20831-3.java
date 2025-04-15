/*Fix the delivery report error

Although TP-status is PENDING or FAILED, delivery report is displayed.
Only in case of SUCCESS, it should be displayed to users.

Change-Id:I00eeed61f8797ff1d468c0bf23a7f3431ed02faeSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index 73a4b18..3431fd7 100644

//Synthetic comment -- @@ -51,8 +51,10 @@
boolean isStatusMessage = updateMessageStatus(context, messageUri, pdu);

// Called on the UI thread so don't block.
            MessagingNotification.nonBlockingUpdateNewMessageIndicator(context,
                    true, isStatusMessage);
}
}








