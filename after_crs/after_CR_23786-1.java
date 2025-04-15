/*Add FLAG_AUTO_CANCEL flag for multiple supl notifications.

Added FLAG_AUTO_CANCEL to notification flags in GpsNetInitiatedHandler.java.
This is done to clear consecutive notifications in UI

Change-Id:I84be36af0b556f43ffd7d51ac04ff12300090ca5Signed-off-by: Anshuman Pandey <anshuman.pandey@stericsson.com>
Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsNetInitiatedHandler.java b/location/java/com/android/internal/location/GpsNetInitiatedHandler.java
//Synthetic comment -- index ffc3346..29dec63 100755

//Synthetic comment -- @@ -205,7 +205,7 @@
mNiNotification.defaults &= ~Notification.DEFAULT_SOUND;
}        

        mNiNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
mNiNotification.tickerText = getNotifTicker(notif, mContext);

// if not to popup dialog immediately, pending intent will open the dialog







