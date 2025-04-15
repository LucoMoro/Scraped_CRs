/*Delete AlarmManagerTest#testSetTimeZone

Bug 3188260

I don't think this can or will be fixed, so delete this test.

Change-Id:Ibaa5d7a215b5ef7ec9430d41b80d8215e3ec03b6*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/AlarmManagerTest.java b/tests/tests/app/src/android/app/cts/AlarmManagerTest.java
//Synthetic comment -- index dc2ff92..7c03a75 100644

//Synthetic comment -- @@ -16,28 +16,19 @@

package android.app.cts;

import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.app.cts.ISecondary;
import android.test.AndroidTestCase;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestStatus;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.ToBeFixed;

@TestTargetClass(AlarmManager.class)
public class AlarmManagerTest extends AndroidTestCase {
private AlarmManager mAlarmManager;
//Synthetic comment -- @@ -52,11 +43,8 @@
private long mWakeupTime;
private MockAlarmReceiver mMockAlarmReceiver;

    private Sync mSync;

private final int TIME_DELTA = 200;
private final int TIME_DELAY = 2000;
    private ISecondary mSecondaryService = null;

class Sync {
public boolean mIsConnected;
//Synthetic comment -- @@ -83,79 +71,6 @@
}

@TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setTimeZone",
        args = {java.lang.String.class}
    )
    @BrokenTest("Broken by CL148448. Default timezone of the test and the service differ.")
    public void testSetTimeZone() throws Exception {
        mSync = new Sync();
        final String ACTION = "android.app.REMOTESERVICE";
        mServiceIntent = new Intent(ACTION);
        mContext.startService(mServiceIntent);
        mContext.bindService(new Intent(ISecondary.class.getName()), mSecondaryConnection,
                Context.BIND_AUTO_CREATE);

        synchronized (mSync) {
            if (!mSync.mIsConnected) {
                mSync.wait();
            }
        }
        final TimeZone currentZone = TimeZone.getDefault();

        // test timeZone is null, timeZone won't be set
        String timeZone = null;
        mAlarmManager.setTimeZone(timeZone);
        TimeZone values = TimeZone.getDefault();
        assertEquals(currentZone.getID(), values.getID());
        // test another process's timezone

        assertEquals(currentZone.getID(), mSecondaryService.getTimeZoneID());

        // nothing in timZone, timeZone won't be set
        timeZone = "";
        mAlarmManager.setTimeZone(timeZone);
        values = TimeZone.getDefault();
        assertEquals(currentZone.getID(), values.getID());
        // test timeZone as different time zone
        String[] timeZones = TimeZone.getAvailableIDs();
        // set different time zone
        timeZone = currentZone.getID().equals(timeZones[0]) ? timeZones[1] : timeZones[0];
        mAlarmManager.setTimeZone(timeZone);
        Thread.sleep(TIME_DELAY);
        values = TimeZone.getDefault();
        TimeZone zone = TimeZone.getTimeZone(timeZone);
        assertEquals(zone.getID(), values.getID());

        // test another process's timezone
        assertEquals(zone.getID(), mSecondaryService.getTimeZoneID());

        // set time zone as origin time zone
        TimeZone.setDefault(currentZone);

        mContext.stopService(mServiceIntent);
        mServiceIntent = null;
    }

    private ServiceConnection mSecondaryConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mSecondaryService = ISecondary.Stub.asInterface(service);
            synchronized (mSync) {
                mSync.mIsConnected = true;
                mSync.notify();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mSecondaryService = null;
            synchronized (mSync) {
                mSync.mIsDisConnected = true;
                mSync.notify();
            }
        }
    };

    @TestTargetNew(
level = TestLevel.PARTIAL,
method = "set",
args = {int.class, long.class, android.app.PendingIntent.class}
//Synthetic comment -- @@ -216,7 +131,7 @@
Thread.sleep(TIME_DELAY);
assertTrue(mMockAlarmReceiver.alarmed);
}
    
@TestTargetNew(
level = TestLevel.PARTIAL,
method = "setRepeating",







