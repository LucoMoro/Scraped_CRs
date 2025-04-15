/*code cleanup
	- unused import statements
        - unused local/private membbers and methods
        - ...

Change-Id:I5e7679d31d09c8daf3335bbb5149c18f2a46053d*/
//Synthetic comment -- diff --git a/src/com/android/deskclock/Alarm.java b/src/com/android/deskclock/Alarm.java
//Synthetic comment -- index dedc0d8..411b439 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.text.format.DateFormat;

import java.text.DateFormatSymbols;
import java.util.Calendar;








//Synthetic comment -- diff --git a/src/com/android/deskclock/AlarmAlert.java b/src/com/android/deskclock/AlarmAlert.java
//Synthetic comment -- index 72b8a3b..4c8c8d9 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

/**
* Full screen alarm alert: pops visible indicator and plays alarm tone. This








//Synthetic comment -- diff --git a/src/com/android/deskclock/AlarmAlertFullScreen.java b/src/com/android/deskclock/AlarmAlertFullScreen.java
//Synthetic comment -- index 56fc177..806e607 100644

//Synthetic comment -- @@ -24,13 +24,10 @@
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;








//Synthetic comment -- diff --git a/src/com/android/deskclock/AlarmClock.java b/src/com/android/deskclock/AlarmClock.java
//Synthetic comment -- index 5ade787..d0f03e8 100644

//Synthetic comment -- @@ -21,13 +21,9 @@
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -39,7 +35,6 @@
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
//Synthetic comment -- @@ -60,7 +55,6 @@
test code, etc. */
static final boolean DEBUG = false;

    private SharedPreferences mPrefs;
private LayoutInflater mFactory;
private ListView mAlarmsList;
private Cursor mCursor;
//Synthetic comment -- @@ -199,7 +193,6 @@
super.onCreate(icicle);

mFactory = LayoutInflater.from(this);
        mPrefs = getSharedPreferences(PREFERENCES, 0);
mCursor = Alarms.getAlarmsCursor(getContentResolver());

updateLayout();
//Synthetic comment -- @@ -304,7 +297,7 @@
return super.onCreateOptionsMenu(menu);
}

    public void onItemClick(AdapterView parent, View v, int pos, long id) {
Intent intent = new Intent(this, SetAlarm.class);
intent.putExtra(Alarms.ALARM_ID, (int) id);
startActivity(intent);








//Synthetic comment -- diff --git a/src/com/android/deskclock/AlarmKlaxon.java b/src/com/android/deskclock/AlarmKlaxon.java
//Synthetic comment -- index 38098c8..f0dc05d 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;








//Synthetic comment -- diff --git a/src/com/android/deskclock/AlarmProvider.java b/src/com/android/deskclock/AlarmProvider.java
//Synthetic comment -- index 133e79a..65eb415 100644

//Synthetic comment -- @@ -183,14 +183,12 @@
public int delete(Uri url, String where, String[] whereArgs) {
SQLiteDatabase db = mOpenHelper.getWritableDatabase();
int count;
        long rowId = 0;
switch (sURLMatcher.match(url)) {
case ALARMS:
count = db.delete("alarms", where, whereArgs);
break;
case ALARMS_ID:
String segment = url.getPathSegments().get(1);
                rowId = Long.parseLong(segment);
if (TextUtils.isEmpty(where)) {
where = "_id=" + segment;
} else {








//Synthetic comment -- diff --git a/src/com/android/deskclock/AlarmReceiver.java b/src/com/android/deskclock/AlarmReceiver.java
//Synthetic comment -- index 5bb0e5a..89c56b8 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.os.Parcel;

import java.text.SimpleDateFormat;








//Synthetic comment -- diff --git a/src/com/android/deskclock/Alarms.java b/src/com/android/deskclock/Alarms.java
//Synthetic comment -- index 4c90753..929b41e 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
import android.text.format.DateFormat;

import java.util.Calendar;
import java.text.DateFormatSymbols;

/**
* The Alarms provider supplies info about Alarm Clock settings
//Synthetic comment -- @@ -148,12 +147,6 @@

private static ContentValues createContentValues(Alarm alarm) {
ContentValues values = new ContentValues(8);
        // Set the alarm_time value if this alarm does not repeat. This will be
        // used later to disable expire alarms.
        long time = 0;
        if (!alarm.daysOfWeek.isRepeatSet()) {
            time = calculateAlarm(alarm);
        }

values.put(Alarm.Columns.ENABLED, alarm.enabled ? 1 : 0);
values.put(Alarm.Columns.HOUR, alarm.hour);








//Synthetic comment -- diff --git a/src/com/android/deskclock/DeskClock.java b/src/com/android/deskclock/DeskClock.java
//Synthetic comment -- index 2e16b4d..09b87a3 100644

//Synthetic comment -- @@ -18,61 +18,40 @@

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
//Synthetic comment -- @@ -81,11 +60,8 @@
import static android.os.BatteryManager.BATTERY_STATUS_FULL;
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
//Synthetic comment -- @@ -574,7 +550,7 @@
if (DEBUG) Log.d(LOG_TAG, "onNewIntent with intent: " + newIntent);

// update our intent so that we can consult it to determine whether or
        // not the most recent launch was via a dock event 
setIntent(newIntent);
}









//Synthetic comment -- diff --git a/src/com/android/deskclock/DigitalClock.java b/src/com/android/deskclock/DigitalClock.java
//Synthetic comment -- index 56544e3..8b9fd04 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;








//Synthetic comment -- diff --git a/src/com/android/deskclock/Log.java b/src/com/android/deskclock/Log.java
//Synthetic comment -- index 6749caa..e498105 100644

//Synthetic comment -- @@ -20,7 +20,6 @@

package com.android.deskclock;

import android.os.SystemClock;
import android.util.Config;

class Log {








//Synthetic comment -- diff --git a/src/com/android/deskclock/RepeatPreference.java b/src/com/android/deskclock/RepeatPreference.java
//Synthetic comment -- index 61e2af5..f9284e3 100644

//Synthetic comment -- @@ -62,7 +62,6 @@
@Override
protected void onPrepareDialogBuilder(Builder builder) {
CharSequence[] entries = getEntries();
        CharSequence[] entryValues = getEntryValues();

builder.setMultiChoiceItems(
entries, mDaysOfWeek.getBooleanArray(),








//Synthetic comment -- diff --git a/src/com/android/deskclock/SetAlarm.java b/src/com/android/deskclock/SetAlarm.java
//Synthetic comment -- index ee36a38..67ca86d 100644

//Synthetic comment -- @@ -29,15 +29,8 @@
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

//Synthetic comment -- @@ -54,7 +47,6 @@
private AlarmPreference mAlarmPref;
private CheckBoxPreference mVibratePref;
private RepeatPreference mRepeatPref;
    private MenuItem mTestAlarmItem;

private int     mId;
private int     mHour;







