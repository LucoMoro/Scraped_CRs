/*Removed ununsed imports and unused variables

Change-Id:I2ada7dc7c042cd67812f02bfb8d899bd7a2bf27b*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/Alarm.java b/src/com/android/alarmclock/Alarm.java
//Synthetic comment -- index 350b7b4..284a11c 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.text.format.DateFormat;

import java.text.DateFormatSymbols;
import java.util.Calendar;








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index 89a866a..5fa93db 100644

//Synthetic comment -- @@ -25,14 +25,12 @@
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
import android.widget.Button;
import android.widget.Toast;








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlertFullScreen.java b/src/com/android/alarmclock/AlarmAlertFullScreen.java
//Synthetic comment -- index c00ffce..ead38b7 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.alarmclock;

import android.os.Bundle;
import android.view.WindowManager;

/**
* Full screen alarm alert: pops visible indicator and plays alarm tone. This








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmClock.java b/src/com/android/alarmclock/AlarmClock.java
//Synthetic comment -- index 75477fd..fe32f4d 100644

//Synthetic comment -- @@ -26,8 +26,6 @@
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -35,7 +33,6 @@
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmKlaxon.java b/src/com/android/alarmclock/AlarmKlaxon.java
//Synthetic comment -- index a8891c1..2f2a608 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmProvider.java b/src/com/android/alarmclock/AlarmProvider.java
//Synthetic comment -- index 5849a38..703c37a 100644

//Synthetic comment -- @@ -212,14 +212,12 @@
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








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmReceiver.java b/src/com/android/alarmclock/AlarmReceiver.java
//Synthetic comment -- index 97374ef..979cc34 100644

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








//Synthetic comment -- diff --git a/src/com/android/alarmclock/Alarms.java b/src/com/android/alarmclock/Alarms.java
//Synthetic comment -- index 63a67d7..54628ba 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import android.text.format.DateFormat;

import java.util.Calendar;
import java.text.DateFormatSymbols;

/**
* The Alarms provider supplies info about Alarm Clock settings








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AnalogAppWidgetProvider.java b/src/com/android/alarmclock/AnalogAppWidgetProvider.java
//Synthetic comment -- index 524b1d2..fb57b1d 100644

//Synthetic comment -- @@ -16,33 +16,12 @@

package com.android.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.Calendar;
import android.provider.Calendar.Attendees;
import android.provider.Calendar.Calendars;
import android.provider.Calendar.EventsColumns;
import android.provider.Calendar.Instances;
import android.provider.Calendar.Reminders;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Arrays;

/**
* Simple widget to show analog clock.
*/








//Synthetic comment -- diff --git a/src/com/android/alarmclock/Log.java b/src/com/android/alarmclock/Log.java
//Synthetic comment -- index 18cc391..1b40035 100644

//Synthetic comment -- @@ -20,7 +20,6 @@

package com.android.alarmclock;

import android.os.SystemClock;
import android.util.Config;

class Log {








//Synthetic comment -- diff --git a/src/com/android/alarmclock/RepeatPreference.java b/src/com/android/alarmclock/RepeatPreference.java
//Synthetic comment -- index 6af023b..aacddcd 100644

//Synthetic comment -- @@ -61,7 +61,6 @@
@Override
protected void onPrepareDialogBuilder(Builder builder) {
CharSequence[] entries = getEntries();
        CharSequence[] entryValues = getEntryValues();

builder.setMultiChoiceItems(
entries, mDaysOfWeek.getBooleanArray(),








//Synthetic comment -- diff --git a/src/com/android/alarmclock/SetAlarm.java b/src/com/android/alarmclock/SetAlarm.java
//Synthetic comment -- index 03533af..6edea0f 100644

//Synthetic comment -- @@ -19,8 +19,6 @@
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;







