/*Add Jelly Bean notification features to NotificationCompat.

Add support for progress, priorities, actions, styles and more to
NotificationCompat.

Change-Id:I0b83bc2ac3079d0e2c9c1733e69435e033746bf2*/
//Synthetic comment -- diff --git a/v4/honeycomb/android/support/v4/app/NotificationCompatHoneycomb.java b/v4/honeycomb/android/support/v4/app/NotificationCompatHoneycomb.java
//Synthetic comment -- index 3048e91..7d0b4f1 100644

//Synthetic comment -- @@ -17,38 +17,34 @@
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

class NotificationCompatHoneycomb {
    static Notification add(Context context, Notification n,
            CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo,
            RemoteViews tickerView, int number,
            PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon) {
        Notification.Builder b = new Notification.Builder(context)
                .setWhen(n.when)
                .setSmallIcon(n.icon, n.iconLevel)
                .setContent(n.contentView)
                .setTicker(n.tickerText, tickerView)
                .setSound(n.sound, n.audioStreamType)
                .setVibrate(n.vibrate)
                .setLights(n.ledARGB, n.ledOnMS, n.ledOffMS)
                .setOngoing((n.flags & Notification.FLAG_ONGOING_EVENT) != 0)
                .setOnlyAlertOnce((n.flags & Notification.FLAG_ONLY_ALERT_ONCE) != 0)
                .setAutoCancel((n.flags & Notification.FLAG_AUTO_CANCEL) != 0)
                .setDefaults(n.defaults)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentInfo(contentInfo)
                .setContentIntent(contentIntent)
                .setDeleteIntent(n.deleteIntent)
                .setFullScreenIntent(fullScreenIntent,
                        (n.flags & Notification.FLAG_HIGH_PRIORITY) != 0)
                .setLargeIcon(largeIcon)
                .setNumber(number);

        return b.getNotification();
}
}








//Synthetic comment -- diff --git a/v4/ics/android/support/v4/app/NotificationCompatICS.java b/v4/ics/android/support/v4/app/NotificationCompatICS.java
new file mode 100644
//Synthetic comment -- index 0000000..f0a6ff7

//Synthetic comment -- @@ -0,0 +1,34 @@








//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/NotificationCompat.java b/v4/java/android/support/v4/app/NotificationCompat.java
//Synthetic comment -- index 71ec14e..14007ab 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

public class NotificationCompat {
/**
//Synthetic comment -- @@ -37,34 +38,72 @@
*/
public static final int FLAG_HIGH_PRIORITY = 0x00000080;

private static final NotificationCompatImpl IMPL;

interface NotificationCompatImpl {
        public Notification getNotification(Builder b);
}

static class NotificationCompatImplBase implements NotificationCompatImpl {
        public Notification getNotification(Builder b) {
            Notification result = (Notification) b.mNotification;
result.setLatestEventInfo(b.mContext, b.mContentTitle,
b.mContentText, b.mContentIntent);
return result;
}
}

    static class NotificationCompatImplHoneycomb implements NotificationCompatImpl {
        public Notification getNotification(Builder b) {
            return NotificationCompatHoneycomb.add(b.mContext, b.mNotification,
                    b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView,
                    b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon);
        }
    }

static {
        if (Build.VERSION.SDK_INT >= 11) {
            IMPL = new NotificationCompatImplHoneycomb();
} else {
            IMPL = new NotificationCompatImplBase();
}
}

//Synthetic comment -- @@ -83,6 +122,17 @@
Bitmap mLargeIcon;
CharSequence mContentInfo;
int mNumber;

Notification mNotification = new Notification();

//Synthetic comment -- @@ -178,15 +228,14 @@

/**
* Set the progress this notification represents, which may be
         * represented as a {@link ProgressBar}.
*/
        /* TODO
public Builder setProgress(int max, int progress, boolean indeterminate) {
mProgressMax = max;
mProgress = progress;
mProgressIndeterminate = indeterminate;
return this;
        }*/

/**
* Supply a custom RemoteViews to use instead of the standard one.
//Synthetic comment -- @@ -280,7 +329,7 @@
/**
* Set the sound to play.  It will play on the stream you supply.
*
         * @see #STREAM_DEFAULT
* @see AudioManager for the <code>STREAM_</code> constants.
*/
public Builder setSound(Uri sound, int streamType) {
//Synthetic comment -- @@ -378,11 +427,296 @@
}

/**
         * Combine all of the options that have been set and return a new {@link Notification}
* object.
*/
        public Notification getNotification() {
            return (Notification) IMPL.getNotification(this);
}
}
}








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/NotificationCompatJB.java b/v4/jellybean/android/support/v4/app/NotificationCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..5f8830e

//Synthetic comment -- @@ -0,0 +1,80 @@







