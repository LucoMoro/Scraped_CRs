/*Continue to transact pending Mms once after boot completed

Change-Id:Id0cc24fdd1eaf985681c12c34303e854a8d83ce2Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MmsSystemEventReceiver.java b/src/com/android/mms/transaction/MmsSystemEventReceiver.java
//Synthetic comment -- index b8eb917..78b027b 100644

//Synthetic comment -- @@ -79,6 +79,10 @@
// Called on the UI thread so don't block.
MessagingNotification.nonBlockingUpdateNewMessageIndicator(
context, MessagingNotification.THREAD_NONE, false);

            // Scan and send pending Mms once after boot completed since
            // ACTION_ANY_DATA_CONNECTION_STATE_CHANGED wasn't registered in a whole life cycle
            wakeUpService(context);
}
}








