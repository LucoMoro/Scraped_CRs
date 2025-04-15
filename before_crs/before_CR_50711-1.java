/*Send next message if current one fails

If there is an exception during sending current
message, it will not try to send next pending one.

How to reproduce:
1) Send a sms to 2 recipents. First one is a
   invalid address, for example "invalid", and
   the second one is a valid address;
2) Message to the first address fails;
3) It will not send the message to the second
   address.

Change-Id:I9442711118a64d0d4b9b08a04cf717c97428bf46Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
//Synthetic comment -- index 724e863..642b778 100755

//Synthetic comment -- @@ -272,6 +272,12 @@
mSending = false;
messageFailedToSend(msgUri, SmsManager.RESULT_ERROR_GENERIC_FAILURE);
success = false;
}
}
} finally {







