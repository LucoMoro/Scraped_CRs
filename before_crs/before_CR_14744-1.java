/*Reduced Warnings, Added a Preferencecommit check for further development

Change-Id:I2ccb39bd1fa660237eed100a27349c31ce37b869*/
//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmClock.java b/src/com/android/alarmclock/AlarmClock.java
//Synthetic comment -- index 75477fd..39126f6 100644

//Synthetic comment -- @@ -319,7 +319,7 @@
}
}

    public void onItemClick(AdapterView parent, View v, int pos, long id) {
Intent intent = new Intent(this, SetAlarm.class);
intent.putExtra(Alarms.ALARM_ID, (int) id);
startActivity(intent);








//Synthetic comment -- diff --git a/src/com/android/alarmclock/AlarmReceiver.java b/src/com/android/alarmclock/AlarmReceiver.java
//Synthetic comment -- index 97374ef..f4ad6d5 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
context.sendBroadcast(closeDialogs);

// Decide which activity to start based on the state of the keyguard.
        Class c = AlarmAlert.class;
KeyguardManager km = (KeyguardManager) context.getSystemService(
Context.KEYGUARD_SERVICE);
if (km.inKeyguardRestrictedInputMode()) {








//Synthetic comment -- diff --git a/src/com/android/alarmclock/ClockPicker.java b/src/com/android/alarmclock/ClockPicker.java
//Synthetic comment -- index 31150a2..1268095 100644

//Synthetic comment -- @@ -68,8 +68,8 @@
mGallery.setSelection(face, false);
}

    public void onItemSelected(AdapterView parent, View v, int position, long id) {
        if (mClock != null) {
mClockLayout.removeView(mClock);
}
mClock = mFactory.inflate(AlarmClock.CLOCKS[position], null);
//Synthetic comment -- @@ -77,20 +77,21 @@
mPosition = position;
}

    public void onItemClick(AdapterView parent, View v, int position, long id) {
selectClock(position);
}

private synchronized void selectClock(int position) {
SharedPreferences.Editor ed = mPrefs.edit();
ed.putInt(AlarmClock.PREF_CLOCK_FACE, position);
        ed.commit();

        setResult(RESULT_OK);
finish();
}

    public void onNothingSelected(AdapterView parent) {
}

class ClockAdapter extends BaseAdapter {







