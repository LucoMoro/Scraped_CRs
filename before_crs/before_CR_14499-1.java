/*TimePicker dialog for snooze time setting and 3-times autosnooze via Preference snoozeCounter are fully functional

Change-Id:Ic0ca23c16837947764a6bc703536486512bce8cb*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index 694c4b0..394c5eb 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
//Synthetic comment -- @@ -43,10 +44,11 @@
* Alarm Clock alarm alert: pops visible indicator and plays alarm
* tone
*/
public class AlarmAlert extends Activity {

	private static final int DEFAULT_SNOOZE_MINUTES = 10;
	private static final int AUTO_SNOOZE_MINUTES = 1;
private static final int UNKNOWN = 0;
private static final int SNOOZE = 1;
private static final int DISMISS = 2;
//Synthetic comment -- @@ -58,6 +60,9 @@
private int mAlarmId;
private String mLabel;
private long mDelta;

private Alarms.DaysOfWeek mDaysOfWeek = new Alarms.DaysOfWeek();

//Synthetic comment -- @@ -65,8 +70,9 @@

@Override
protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);     
        
// Maintain a lock during the playback of the alarm. This lock may have
// already been acquired in AlarmReceiver. If the process was killed,
// the global wake lock is gone. Acquire again just to be sure.
//Synthetic comment -- @@ -89,25 +95,22 @@
Alarms.setNextAlert(this);

mKlaxon.setKillerCallback(new AlarmKlaxon.KillerCallback() {
            public void onKilled() {  
	            updateSilencedText();                		
        		
	            /* don't allow snooze */
	            mSnoozeButton.setEnabled(false);            	
            	
                SharedPreferences setting = AlarmAlert.this.getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = setting.edit();
            	int snoozeCount = setting.getInt("snoozeCount", 0);
                
	/* Alert may be displayed and snoozed three times. */            	
	            if (snoozeCount < 3) {
	            	snoozeCount++;
	            	mDelta = snoozeCount * AUTO_SNOOZE_MINUTES * 1000 * 60;
	            	editor.putInt("snoozeCount", snoozeCount);
	            	editor.commit();
	            	snooze();
	            } else {
		            if (Log.LOGV) Log.v("onKilled()");
		            
		            // Dismiss the alarm but mark the state as killed so if the
		            // config changes, we show the silenced message and disable
//Synthetic comment -- @@ -213,6 +216,7 @@
displayTime = getString(R.string.alarm_alert_snooze_not_set,
Alarms.formatTime(AlarmAlert.this, c));
mState = DISMISS;
} else {
Alarms.saveSnoozeAlert(AlarmAlert.this, mAlarmId, snoozeTime,
mLabel);
//Synthetic comment -- @@ -248,44 +252,37 @@

String[] formats = AlarmAlert.this.getResources().getStringArray(R.array.alarm_alert_snooze_set);
return String.format(formats[index], hourSeq, minSeq);            
    }
    
    // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {
                long alarm = Alarms.calculateAlarm(hour, minute,
                        mDaysOfWeek).getTimeInMillis();
                mDelta = alarm - System.currentTimeMillis();
            	snooze();            	
            	finish();
            }
        };    
        
    @Override
    protected Dialog onCreateDialog(int id) {
        // Start with now.
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 
        		DEFAULT_SNOOZE_MINUTES);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);    		    		
        switch (id) {
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this,
                    mTimeSetListener, hour, minute, false);
        }
        return null;
    }  

// Dismiss the alarm.
private void dismiss() {
mState = DISMISS;
mKlaxon.stop(this, false);
releaseLocks();
}

/**
* this is called when a second alarm is triggered while a
* previous alert window is still active.
//Synthetic comment -- @@ -310,15 +307,27 @@
Alarms.setNextAlert(this);
setIntent(intent);
}

    @Override
    protected void onDestroy() {
    	super.onDestroy();
        SharedPreferences setting = AlarmAlert.this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt("snoozeCount", 0);
        editor.commit();   	
    }

@Override
protected void onResume() {
//Synthetic comment -- @@ -339,7 +348,31 @@
// the lock and keyguard.
releaseLocks();
}

@Override
public boolean dispatchKeyEvent(KeyEvent event) {
// Do this on key down to handle a few of the system keys. Only handle
//Synthetic comment -- @@ -385,4 +418,5 @@
private synchronized void releaseLocks() {
AlarmAlertWakeLock.release();
}
}








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmKlaxon.java b/src/com/android/alarmclock/AlarmKlaxon.java
//Synthetic comment -- index 033284d..1c8ea5a 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
}

/** Play alarm up to 1 minutes before silencing */
    final static int ALARM_TIMEOUT_SECONDS = 1 * 5;

private static final long[] sVibratePattern = new long[] { 500, 500 };









//Synthetic comment -- diff --git a/src/com/android/alarmclock/SetAlarm.java b/src/com/android/alarmclock/SetAlarm.java
//Synthetic comment -- index 6461b6a..2859c9c 100644

//Synthetic comment -- @@ -98,8 +98,8 @@
*/
@Override
protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

addPreferencesFromResource(R.xml.alarm_prefs);
mLabel = (EditTextPreference) findPreference("label");
mLabel.setOnPreferenceChangeListener(
//Synthetic comment -- @@ -107,7 +107,7 @@
public boolean onPreferenceChange(Preference p,
Object newValue) {
p.setSummary((String) newValue);
                        saveAlarm(false, (String) newValue);
return true;
}
});
//Synthetic comment -- @@ -178,18 +178,16 @@
break;
}
}

@Override
public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

if (preference == mTimePref) {
showDialog(DIALOG_TIMEPICKER);
        } else if (preference == mAlarmOnPref) {
saveAlarm(true);
} else if (preference == mVibratePref) {
saveAlarm(false);
}

return super.onPreferenceTreeClick(preferenceScreen, preference);
}

//Synthetic comment -- @@ -197,9 +195,9 @@
mHour = hourOfDay;
mMinutes = minute;
mAlarmOnPref.setChecked(true);
        saveAlarm(true);
}

/**
* Alarms.AlarmSettings implementation.  Database feeds current
* settings in through this call
//Synthetic comment -- @@ -269,7 +267,8 @@
}

private void saveAlarm(boolean popToast) {
        saveAlarm(popToast, mLabel.getText());
}

/**
//Synthetic comment -- @@ -302,7 +301,7 @@

if (enabled && popToast) {
popAlarmSetToast(context, hour, minute, daysOfWeek);
        }
}

/**
//Synthetic comment -- @@ -370,9 +369,18 @@
return true;
}

public boolean onOptionsItemSelected(MenuItem item) {
if (item == mDeleteAlarmItem) {
Alarms.deleteAlarm(this, mId);
finish();
return true;
}
//Synthetic comment -- @@ -406,5 +414,4 @@
saveAlarm(this, mId, true, hour, minutes, mDaysOfWeek, true,
mLabel.getText(), mAlarmPref.mAlert.toString(), true);
}

}







