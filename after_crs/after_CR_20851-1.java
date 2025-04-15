/*Delete AlarmManagerTest#testSetTimeZone

Bug 3188260

I don't think this can or will be fixed, so delete this test.

Change-Id:Ibaa5d7a215b5ef7ec9430d41b80d8215e3ec03b6*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/AlarmManagerTest.java b/tests/tests/app/src/android/app/cts/AlarmManagerTest.java
//Synthetic comment -- index dc2ff92..7c03a75 100644

//Synthetic comment -- @@ -16,28 +16,19 @@

package android.app.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.ToBeFixed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.test.AndroidTestCase;

@TestTargetClass(AlarmManager.class)
public class AlarmManagerTest extends AndroidTestCase {
private AlarmManager mAlarmManager;
//Synthetic comment -- @@ -52,11 +43,8 @@
private long mWakeupTime;
private MockAlarmReceiver mMockAlarmReceiver;

private final int TIME_DELTA = 200;
private final int TIME_DELAY = 2000;

class Sync {
public boolean mIsConnected;
//Synthetic comment -- @@ -83,79 +71,6 @@
}

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







