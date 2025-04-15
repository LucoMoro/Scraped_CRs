/*fix build*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index de27bac..4c33b73 100644

//Synthetic comment -- @@ -371,13 +371,6 @@
nm.cancel(notificationId);
}

    private static Intent getAppIntent() {
        Intent appIntent = new Intent(Intent.ACTION_MAIN, Threads.CONTENT_URI);

        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return appIntent;
   }

private static void updateDeliveryNotification(
	    Context context,
	    boolean isNew,







