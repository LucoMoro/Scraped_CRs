/*Use DateUtils rather then DateFormatSymbols, because not all locales are handled by DateFormatSymbols

Change-Id:I3e4e43634d93ea9300f9c7ddd59891924e9bdcdc*/




//Synthetic comment -- diff --git a/src/com/android/deskclock/Alarm.java b/src/com/android/deskclock/Alarm.java
//Synthetic comment -- index dedc0d8..11c55d6 100644

//Synthetic comment -- @@ -17,14 +17,17 @@
package com.android.deskclock;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.net.URISyntaxException;
import java.text.DateFormatSymbols;
import java.util.Calendar;

//Synthetic comment -- @@ -59,6 +62,8 @@
p.writeString(label);
p.writeParcelable(alert, flags);
p.writeInt(silent ? 1 : 0);
        p.writeString(intent);
        p.writeInt(noDialog ? 1 : 0);
}
//////////////////////////////
// end Parcelable apis
//Synthetic comment -- @@ -124,6 +129,18 @@
public static final String ALERT = "alert";

/**
         * Intent to fire when alarm triggers
         * <P>Type: STRING</P>
         */
        public static final String INTENT = "intent";

        /**
         * Option to show dialog or not
         * <P>Type: BOOLEAN</P>
         */
        public static final String NO_DIALOG = "no_dialog";

        /**
* The default sort order for this table
*/
public static final String DEFAULT_SORT_ORDER =
//Synthetic comment -- @@ -134,7 +151,7 @@

static final String[] ALARM_QUERY_COLUMNS = {
_ID, HOUR, MINUTES, DAYS_OF_WEEK, ALARM_TIME,
            ENABLED, VIBRATE, MESSAGE, ALERT, INTENT, NO_DIALOG };

/**
* These save calls to cursor.getColumnIndexOrThrow()
//Synthetic comment -- @@ -149,6 +166,8 @@
public static final int ALARM_VIBRATE_INDEX = 6;
public static final int ALARM_MESSAGE_INDEX = 7;
public static final int ALARM_ALERT_INDEX = 8;
        public static final int ALARM_INTENT_INDEX = 9;
        public static final int ALARM_NO_DIALOG_INDEX = 10;
}
//////////////////////////////
// End column definitions
//Synthetic comment -- @@ -165,6 +184,8 @@
public String     label;
public Uri        alert;
public boolean    silent;
    public String     intent;
    public boolean    noDialog;

public Alarm(Cursor c) {
id = c.getInt(Columns.ALARM_ID_INDEX);
//Synthetic comment -- @@ -193,6 +214,18 @@
RingtoneManager.TYPE_ALARM);
}
}
        String intentString = c.getString(Columns.ALARM_INTENT_INDEX);
        if (!TextUtils.isEmpty(intentString)) {
            try {
                // Try and parse the URI, see if it breaks.
                Intent.parseUri(intentString, Intent.URI_INTENT_SCHEME);
                // If it's an invalid URI, the exception will short-circuit.
                intent = intentString;
            } catch (URISyntaxException e) {
                intent = null;
            }
        }
        noDialog = c.getInt(Columns.ALARM_NO_DIALOG_INDEX) == 1;
}

public Alarm(Parcel p) {
//Synthetic comment -- @@ -206,6 +239,8 @@
label = p.readString();
alert = (Uri) p.readParcelable(null);
silent = p.readInt() == 1;
        intent = p.readString();
        noDialog = p.readInt() == 1;
}

// Creates a default alarm at the current time.
//Synthetic comment -- @@ -279,15 +314,11 @@
}

// short or long form?
            int abbrev = dayCount > 1 ? DateUtils.LENGTH_SHORT : DateUtils.LENGTH_LONG;
// selected days
for (int i = 0; i < 7; i++) {
if ((mDays & (1 << i)) != 0) {
                    ret.append(DateUtils.getDayOfWeekString(DAY_MAP[i],abbrev));
dayCount -= 1;
if (dayCount > 0) ret.append(
context.getText(R.string.day_concat));







