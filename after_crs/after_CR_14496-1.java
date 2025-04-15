/*Added autosnooze-but not work

Change-Id:I2d6d983bf33e442ecd7a6672da1d3c48afaf9709*/




//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index f0e6de4..2dd9eac 100644

//Synthetic comment -- @@ -44,17 +44,22 @@
*/
public class AlarmAlert extends Activity {

	private static final int USR_SNOOZE_MINUTES = 10;
	private static final int ALARM_SNOOZE_MINUTES_1 = 1;//5
	private static final int ALARM_SNOOZE_MINUTES_2 = 2;//10
private static final int UNKNOWN = 0;
    private static final int USR_SNOOZE = 1;
    private static final int ALARM_SNOOZE_1 = 2;
    private static final int ALARM_SNOOZE_2 = 3;
    private static final int DISMISS = 4;
    private static final int KILLED = 5;
private Button mSnoozeButton;
private int mState = UNKNOWN;

private AlarmKlaxon mKlaxon;
private int mAlarmId;
private String mLabel;
    private long mDelta;

private Alarms.DaysOfWeek mDaysOfWeek = new Alarms.DaysOfWeek();    

//Synthetic comment -- @@ -79,6 +84,7 @@
mKlaxon = new AlarmKlaxon();
mKlaxon.postPlay(this, mAlarmId);

        mDelta = USR_SNOOZE_MINUTES * 1000 * 60;
/* allow next alarm to trigger while this activity is
active */
Alarms.disableSnoozeAlert(AlarmAlert.this);
//Synthetic comment -- @@ -87,17 +93,35 @@

mKlaxon.setKillerCallback(new AlarmKlaxon.KillerCallback() {
public void onKilled() {
                
                switch (mState) {
                	case UNKNOWN: {
                    	mDelta = ALARM_SNOOZE_MINUTES_1 * 1000 * 60; 
                    	mState = ALARM_SNOOZE_1;
                    	snooze();
                    	break;
                	}
                	case ALARM_SNOOZE_1: {
                    	mDelta = ALARM_SNOOZE_MINUTES_2 * 1000 * 60;                 	  
                    	mState = ALARM_SNOOZE_2;
                    	snooze();
                    	break;                		
                	}
                	case ALARM_SNOOZE_2: {
                        if (Log.LOGV) Log.v("onKilled()");
                        updateSilencedText();                		
                		
                        /* don't allow snooze */
                        mSnoozeButton.setEnabled(false);

                        // Dismiss the alarm but mark the state as killed so if the
                        // config changes, we show the silenced message and disable
                        // snooze.
                        dismiss();
                        mState = KILLED;  
                        break;
                	}                	
                }
}
});

//Synthetic comment -- @@ -179,15 +203,16 @@
}

// Attempt to snooze this alert.
    private void snooze() {
        if (mState != UNKNOWN || mState != USR_SNOOZE || 
        		mState != ALARM_SNOOZE_1 || mState != ALARM_SNOOZE_2) {
return;
}
// If the next alarm is set for sooner than the snooze interval, don't
// snooze. Instead, toast the user that the snooze will not be set.
        final long snoozeTime = System.currentTimeMillis() + mDelta;
        long hours =  mDelta / (1000 * 60 * 60);
        long minutes =  mDelta / (1000 * 60) % 60;        
final long nextAlarm =
Alarms.calculateNextAlert(AlarmAlert.this).getAlert();
String displayTime = null;
//Synthetic comment -- @@ -202,13 +227,13 @@
mLabel);
Alarms.setNextAlert(AlarmAlert.this);
displayTime = formatToast(hours, minutes);
            mState = USR_SNOOZE;
}
// Intentionally log the snooze time for debugging.
Log.v(displayTime);
// Display the snooze houres and minutes in a toast.
Toast.makeText(AlarmAlert.this, displayTime, Toast.LENGTH_LONG).show();
        mKlaxon.stop(this, mState == USR_SNOOZE);
releaseLocks();
}

//Synthetic comment -- @@ -240,9 +265,9 @@
public void onTimeSet(TimePicker view, int hour, int minute) {
long alarm = Alarms.calculateAlarm(hour, minute,
mDaysOfWeek).getTimeInMillis();
                mDelta = alarm - System.currentTimeMillis();
            	snooze();
            	finish();                
}
};    

//Synthetic comment -- @@ -251,7 +276,7 @@
// Start with now.
Calendar c = Calendar.getInstance();
c.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 
        		USR_SNOOZE_MINUTES);

int hour = c.get(Calendar.HOUR_OF_DAY);
int minute = c.get(Calendar.MINUTE);    		    		
//Synthetic comment -- @@ -312,7 +337,7 @@
super.onStop();
if (Log.LOGV) Log.v("AlarmAlert.onStop()");
// As a last resort, try to snooze if this activity is stopped.
        snooze();
// We might have been killed by the KillerCallback so always release
// the lock and keyguard.
releaseLocks();
//Synthetic comment -- @@ -348,7 +373,7 @@
if (dismiss) {
dismiss();
} else {
                        snooze();
}
finish();
}








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmKlaxon.java b/src/com/android/alarmclock/AlarmKlaxon.java
//Synthetic comment -- index fe8ebd1..033284d 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
}

/** Play alarm up to 1 minutes before silencing */
    final static int ALARM_TIMEOUT_SECONDS = 1 * 5;

private static final long[] sVibratePattern = new long[] { 500, 500 };








