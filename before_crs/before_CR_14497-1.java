/*Foo

Change-Id:I4de99d15fa346ffd569690504b47a0f99965a48b*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmAlert.java b/src/com/android/alarmclock/AlarmAlert.java
//Synthetic comment -- index 2dd9eac..10fb594 100644

//Synthetic comment -- @@ -95,7 +95,9 @@
public void onKilled() {

switch (mState) {
                	case UNKNOWN: {
	mDelta = ALARM_SNOOZE_MINUTES_1 * 1000 * 60; 
	mState = ALARM_SNOOZE_1;
	snooze();
//Synthetic comment -- @@ -107,7 +109,7 @@
	snooze();
	break;                		
	}
                	case ALARM_SNOOZE_2: {
if (Log.LOGV) Log.v("onKilled()");
updateSilencedText();                		
		
//Synthetic comment -- @@ -227,13 +229,14 @@
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








