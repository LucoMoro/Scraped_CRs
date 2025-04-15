/*If the TP-Status is not PENDING and FAILED, delivery report is updated.

Change-Id:Idb3cdb9eca2d3faf182170e4a0b783c591ba73e2*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusReceiver.java b/src/com/android/mms/transaction/MessageStatusReceiver.java
//Synthetic comment -- index 73a4b18..0f3b9a4 100644

//Synthetic comment -- @@ -51,8 +51,9 @@
boolean isStatusMessage = updateMessageStatus(context, messageUri, pdu);

// Called on the UI thread so don't block.
            if (message.getStatus() < Sms.STATUS_PENDING)
		MessagingNotification.nonBlockingUpdateNewMessageIndicator(context,
	                    true, isStatusMessage);
}
}








