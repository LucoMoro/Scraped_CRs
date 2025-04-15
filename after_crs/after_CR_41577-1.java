/*Phone: Disable call time display when display is off

Currently a 1 sec timer is used to display the call
duration even when the display is off. This patch
disables the use of this timer when the display is
off.

Change-Id:Ie35baf474d842c6f09b936c8bfa2791b71468613Author: john mathew <john.mathew@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 9267*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index 77702d1..bec3e98 100644

//Synthetic comment -- @@ -183,7 +183,7 @@

mApplication = PhoneApp.getInstance();

        mCallTime = new CallTime(this, context);

// create a new object to track the state for the photo.
mPhotoTracker = new ContactsAsyncHelper.ImageTracker();








//Synthetic comment -- diff --git a/src/com/android/phone/CallTime.java b/src/com/android/phone/CallTime.java
//Synthetic comment -- index 39a69af..df3f74a 100644

//Synthetic comment -- @@ -17,6 +17,9 @@
package com.android.phone;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
//Synthetic comment -- @@ -42,6 +45,8 @@

private static int sProfileState = PROFILE_STATE_NONE;

    private static boolean screenOn = true;

private Call mCall;
private long mLastReportedTime;
private boolean mTimerRunning;
//Synthetic comment -- @@ -49,13 +54,28 @@
private PeriodicTimerCallback mTimerCallback;
private OnTickListener mListener;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                screenOn = false;
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                screenOn = true;
            }
        }
    };

interface OnTickListener {
void onTickForCallTimeElapsed(long timeElapsed);
}

    public CallTime(OnTickListener listener, Context context) {
mListener = listener;
mTimerCallback = new PeriodicTimerCallback();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(mBroadcastReceiver, intentFilter);
}

/**
//Synthetic comment -- @@ -167,9 +187,13 @@
if (PROFILE && isTraceRunning()) {
stopTrace();
}
            if (screenOn) {
                if (DBG) log("SCREEN ON...");
                mTimerRunning = false;
                periodicUpdateTimer();
            } else {
                mTimerRunning = false;
            }
}
}








