/*Add Jelly Bean notification features to NotificationCompat.

Add support for progress, priorities, actions, styles and more to
NotificationCompat.

Change-Id:I0b83bc2ac3079d0e2c9c1733e69435e033746bf2*/




//Synthetic comment -- diff --git a/v4/honeycomb/android/support/v4/app/NotificationCompatHoneycomb.java b/v4/honeycomb/android/support/v4/app/NotificationCompatHoneycomb.java
//Synthetic comment -- index 3048e91..7d0b4f1 100644

//Synthetic comment -- @@ -17,38 +17,34 @@
package android.support.v4.app;

import android.app.Notification;

class NotificationCompatHoneycomb implements NotificationCompat.NotificationCompatImpl {
    static Notification.Builder createBuilder(NotificationCompat.Builder b) {
        final Notification n = b.mNotification;
        return new Notification.Builder(b.mContext)
            .setWhen(n.when)
            .setSmallIcon(n.icon, n.iconLevel)
            .setContent(n.contentView)
            .setTicker(n.tickerText, b.mTickerView)
            .setSound(n.sound, n.audioStreamType)
            .setVibrate(n.vibrate)
            .setLights(n.ledARGB, n.ledOnMS, n.ledOffMS)
            .setOngoing((n.flags & Notification.FLAG_ONGOING_EVENT) != 0)
            .setOnlyAlertOnce((n.flags & Notification.FLAG_ONLY_ALERT_ONCE) != 0)
            .setAutoCancel((n.flags & Notification.FLAG_AUTO_CANCEL) != 0)
            .setDefaults(n.defaults)
            .setContentTitle(b.mContentTitle)
            .setContentText(b.mContentText)
            .setContentInfo(b.mContentInfo)
            .setContentIntent(b.mContentIntent)
            .setDeleteIntent(n.deleteIntent)
            .setFullScreenIntent(b.mFullScreenIntent,
                (n.flags & Notification.FLAG_HIGH_PRIORITY) != 0)
            .setLargeIcon(b.mLargeIcon)
            .setNumber(b.mNumber);
    }

    public Notification build(NotificationCompat.Builder b) {
        return createBuilder(b).getNotification();
}
}








//Synthetic comment -- diff --git a/v4/ics/android/support/v4/app/NotificationCompatICS.java b/v4/ics/android/support/v4/app/NotificationCompatICS.java
new file mode 100644
//Synthetic comment -- index 0000000..5b83fd8

//Synthetic comment -- @@ -0,0 +1,18 @@
package android.support.v4.app;

import android.app.Notification;

class NotificationCompatICS implements NotificationCompat.NotificationCompatImpl {
    static Notification.Builder createBuilder(NotificationCompat.Builder b) {
        Notification.Builder builder = NotificationCompatHoneycomb.createBuilder(b);
        if (b.mProgressSet) {
            builder.setProgress(b.mProgressMax, b.mProgress, b.mProgressIndeterminate);
        }
        return builder;
    }

    @Override
    public Notification build(NotificationCompat.Builder builder) {
        return createBuilder(builder).getNotification();
    }
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/NotificationCompat.java b/v4/java/android/support/v4/app/NotificationCompat.java
//Synthetic comment -- index 71ec14e..14007ab 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import java.util.ArrayList;

public class NotificationCompat {
/**
//Synthetic comment -- @@ -37,34 +38,72 @@
*/
public static final int FLAG_HIGH_PRIORITY = 0x00000080;

    /**
     * Default notification {@code priority}. If your application does not prioritize its own
     * notifications, use this value for all notifications.
     *
     * <p>This will only be respected on API level 16 and above.</p>
     */
    public static final int PRIORITY_DEFAULT = 0;

    /**
     * Lower {@code priority}, for items that are less important. The UI may choose to show these
     * items smaller, or at a different position in the list, compared with your app's
     * {@link #PRIORITY_DEFAULT} items.
     *
     * <p>This will only be respected on API level 16 and above.</p>
     */
    public static final int PRIORITY_LOW = -1;

    /**
     * Lowest {@code priority}; these items might not be shown to the user except under special
     * circumstances, such as detailed notification logs.
     *
     * <p>This will only be respected on API level 16 and above.</p>
     */
    public static final int PRIORITY_MIN = -2;

    /**
     * Higher {@code priority}, for more important notifications or alerts. The UI may choose to
     * show these items larger, or at a different position in notification lists, compared with
     * your app's {@link #PRIORITY_DEFAULT} items.
     *
     * <p>This will only be respected on API level 16 and above.</p>
     */
    public static final int PRIORITY_HIGH = 1;

    /**
     * Highest {@code priority}, for your application's most important items that require the
     * user's prompt attention or input.
     *
     * <p>This will only be respected on API level 16 and above.</p>
     */
    public static final int PRIORITY_MAX = 2;

private static final NotificationCompatImpl IMPL;

interface NotificationCompatImpl {
        public Notification build(Builder builder);
}

static class NotificationCompatImplBase implements NotificationCompatImpl {
        public Notification build(Builder b) {
            Notification result = b.mNotification;
result.setLatestEventInfo(b.mContext, b.mContentTitle,
b.mContentText, b.mContentIntent);
return result;
}
}

static {
        if (Build.VERSION.SDK_INT >= 16) {
          IMPL = new NotificationCompatJB();
        } else if (Build.VERSION.SDK_INT >= 14) {
          IMPL = new NotificationCompatICS();
        } else if (Build.VERSION.SDK_INT >= 11) {
          IMPL = new NotificationCompatHoneycomb();
} else {
          IMPL = new NotificationCompatImplBase();
}
}

//Synthetic comment -- @@ -83,6 +122,17 @@
Bitmap mLargeIcon;
CharSequence mContentInfo;
int mNumber;
        int mPriority;
        CharSequence mSubText;
        boolean mUsesChronometer;
        Style mStyle;
        ArrayList<Integer> mActionIcons;
        ArrayList<CharSequence> mActionTitles;
        ArrayList<PendingIntent> mActionIntents;
        int mProgress;
        int mProgressMax;
        boolean mProgressIndeterminate;
        boolean mProgressSet;

Notification mNotification = new Notification();

//Synthetic comment -- @@ -178,15 +228,14 @@

/**
* Set the progress this notification represents, which may be
         * represented as a {@link android.widget.ProgressBar}.
*/
public Builder setProgress(int max, int progress, boolean indeterminate) {
mProgressMax = max;
mProgress = progress;
mProgressIndeterminate = indeterminate;
return this;
        }

/**
* Supply a custom RemoteViews to use instead of the standard one.
//Synthetic comment -- @@ -280,7 +329,7 @@
/**
* Set the sound to play.  It will play on the stream you supply.
*
         * @see Notification#STREAM_DEFAULT
* @see AudioManager for the <code>STREAM_</code> constants.
*/
public Builder setSound(Uri sound, int streamType) {
//Synthetic comment -- @@ -378,11 +427,296 @@
}

/**
         * Add an action to this notification. Actions are typically displayed
         * by the system as a button adjacent to the notification content.
         *
         * @param icon Resource ID of a drawable that represents the action.
         * @param title Text describing the action.
         * @param intent PendingIntent to be fired when the action is invoked.
         */
        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            if (mActionIcons == null) {
                mActionIcons = new ArrayList<Integer>();
                mActionTitles = new ArrayList<CharSequence>();
                mActionIntents = new ArrayList<PendingIntent>();
            }
            mActionIcons.add(icon);
            mActionTitles.add(title);
            mActionIntents.add(intent);
            return this;
        }

        /**
         * Set the priority of this notification.
         */
        public Builder setPriority(int priority) {
            mPriority = priority;
            return this;
        }

        /**
         * Add a rich notification style to be applied at build time.
         */
        public Builder setStyle(Style style) {
            mStyle = style;
            return this;
        }

        /**
         * Set the third line of text in the platform notification template.
         * Don't use if you're also using
         * {@link #setProgress(int, int, boolean)}; they occupy the same
         * location in the standard template.
         */
        public Builder setSubText(CharSequence subtext) {
            mSubText = subtext;
            return this;
        }

        /**
         * Show the {@link Notification#when} field as a stopwatch. Instead of
         * presenting {@code when} as a timestamp, the notification will show
         * an automatically updating display of the minutes and seconds since
         * {@code when}. Useful when showing an elapsed time (like an ongoing
         * phone call).
         */
        public Builder setUsesChronometer(boolean usesChronometer) {
            mUsesChronometer = usesChronometer;
            return this;
        }

        /** @deprecated Use {@link #build()}. */
        @Deprecated
        public Notification getNotification() {
            return build();
        }

        /**
         * Combine all of the options that have been set and return a new {@link android.app.Notification}
* object.
*/
        public Notification build() {
            return IMPL.build(this);
        }
    }

    /**
     * An object that can apply a rich notification style to a {@link Notification.Builder}
     * object.
     */
    public static abstract class Style {
        protected NotificationCompat.Builder mBuilder;
        CharSequence mBigContentTitle;
        CharSequence mSummaryText;

        public void setBuilder(NotificationCompat.Builder builder) {
            mBuilder = builder;
        }

        protected void checkBuilder() {
            if (mBuilder == null) {
                throw new IllegalArgumentException("Style requires a valid Builder object");
            }
        }

        protected RemoteViews getStandardView(int layoutId) {
            return null;
        }

        protected void internalSetBigContentTitle(CharSequence title) {
            mBigContentTitle = title;
        }

        protected void internalSetSummaryText(CharSequence cs) {
            mSummaryText = cs;
        }

        public abstract Notification build();
    }

    /**
     * Helper class for generating large-format notifications that include a large image attachment.
     *
     * This class is a "rebuilder": It consumes a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification noti = new NotificationCompat.BigPictureStyle(
     *      new NotificationCompat.Builder()
     *         .setContentTitle(&quot;New photo from &quot; + sender.toString())
     *         .setContentText(subject)
     *         .setSmallIcon(R.drawable.new_post)
     *         .setLargeIcon(aBitmap))
     *      .bigPicture(aBigBitmap)
     *      .build();
     * </pre>
     */
    public static class BigPictureStyle extends Style {
        Bitmap mBigLargeIcon;
        Bitmap mBigPicture;

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        /**
         * Override the large icon when the big notification is shown.
         */
        public BigPictureStyle bigLargeIcon(Bitmap b) {
            mBigLargeIcon = b;
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            mBigPicture = b;
            return this;
        }

        @Override
        public Notification build() {
            checkBuilder();
            if (Build.VERSION.SDK_INT >= 16) {
                return NotificationCompatJB.buildBigPictureStyle(this);
            }
            return mBuilder.build();
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public BigPictureStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public BigPictureStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(cs);
            return this;
        }
    }

    /**
     * Helper class for generating large-format notifications that include a lot of text.
     *
     * This class is a "rebuilder": It consumes a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification noti = new NotificationCompat.BigPictureStyle(
     *      new NotificationCompat.Builder()
     *         .setContentTitle(&quot;New mail from &quot; + sender.toString())
     *         .setContentText(subject)
     *         .setSmallIcon(R.drawable.new_mail)
     *         .setLargeIcon(aBitmap))
     *      .bigText(aVeryLongString)
     *      .build();
     * </pre>
     */
    public static class BigTextStyle extends Style {
        CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigTextStyle bigText(CharSequence text) {
            mBigText = text;
            return this;
        }

        @Override
        public Notification build() {
            checkBuilder();
            if (Build.VERSION.SDK_INT >= 16) {
                return NotificationCompatJB.buildBigTextStyle(this);
            }
            return mBuilder.build();
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public BigTextStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public BigTextStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(cs);
            return this;
        }
    }

    /**
     * Helper class for generating large-format notifications that include a list of (up to 5) strings.
     *
     * This class is a "rebuilder": It consumes a Builder object and modifies its behavior, like so:
     * <pre class="prettyprint">
     * Notification noti = new NotificationCompat.InboxStyle(
     *      new NotificationCompat.Builder()
     *         .setContentTitle(&quot;5 New mails from &quot; + sender.toString())
     *         .setContentText(subject)
     *         .setSmallIcon(R.drawable.new_mail)
     *         .setLargeIcon(aBitmap))
     *      .addLine(str1)
     *      .addLine(str2)
     *      .setContentTitle("")
     *      .setSummaryText(&quot;+3 more&quot;)
     *      .build();
     * </pre>
     */
    public static class InboxStyle extends Style {
        ArrayList<CharSequence> mLines;

        public InboxStyle() {
        }

        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        public InboxStyle addLine(CharSequence line) {
            if (mLines == null) {
                mLines = new ArrayList<CharSequence>();
            }
            mLines.add(line);
            return this;
        }

        @Override
        public Notification build() {
            checkBuilder();
            if (Build.VERSION.SDK_INT >= 16) {
                return NotificationCompatJB.buildInboxStyle(this);
            }
            return mBuilder.build();
        }

        /**
         * Overrides ContentTitle in the big form of the template.
         * This defaults to the value passed to setContentTitle().
         */
        public InboxStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(title);
            return this;
        }

        /**
         * Set the first line of text after the detail section in the big form of the template.
         */
        public InboxStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(cs);
            return this;
}
}
}








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/NotificationCompatJB.java b/v4/jellybean/android/support/v4/app/NotificationCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..ebe8186

//Synthetic comment -- @@ -0,0 +1,63 @@
package android.support.v4.app;

import android.app.Notification;

class NotificationCompatJB implements NotificationCompat.NotificationCompatImpl {
    public static Notification.Builder createBuilder(NotificationCompat.Builder b) {
        final Notification.Builder builder = NotificationCompatICS.createBuilder(b);

        if (b.mActionIcons != null) {
            final int size = b.mActionIcons.size();
            for (int i = 0; i < size; i++) {
                builder.addAction(b.mActionIcons.get(i), b.mActionTitles.get(i),
                    b.mActionIntents.get(i));
            }
        }

        return builder.setPriority(b.mPriority)
                .setSubText(b.mSubText)
                .setUsesChronometer(b.mUsesChronometer);
    }

    @Override
    public Notification build(NotificationCompat.Builder b) {
        if (b.mStyle != null) {
            NotificationCompat.Style style = b.mStyle;
            b.mStyle = null; // Avoid infinite recursion
            style.setBuilder(b);
            return style.build();
        }
        return createBuilder(b).build();
    }

    public static Notification buildBigPictureStyle(NotificationCompat.BigPictureStyle s) {
        return new Notification.BigPictureStyle(createBuilder(s.mBuilder))
                .bigLargeIcon(s.mBigLargeIcon)
                .bigPicture(s.mBigPicture)
                .setBigContentTitle(s.mBigContentTitle)
                .setSummaryText(s.mSummaryText)
                .build();
    }

    public static Notification buildBigTextStyle(NotificationCompat.BigTextStyle s) {
        return new Notification.BigTextStyle(createBuilder(s.mBuilder))
                .bigText(s.mBigText)
                .setBigContentTitle(s.mBigContentTitle)
                .setSummaryText(s.mSummaryText)
                .build();
    }

    public static Notification buildInboxStyle(NotificationCompat.InboxStyle s) {
        Notification.InboxStyle style = new Notification.InboxStyle(createBuilder(s.mBuilder))
                .setBigContentTitle(s.mBigContentTitle)
                .setSummaryText(s.mSummaryText);

        if (s.mLines != null) {
            for (CharSequence line : s.mLines) {
                style.addLine(line);
            }
        }

        return style.build();
    }
}







