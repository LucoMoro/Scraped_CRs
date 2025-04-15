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

        mCallTime = new CallTime(this);

// create a new object to track the state for the photo.
mPhotoTracker = new ContactsAsyncHelper.ImageTracker();








//Synthetic comment -- diff --git a/src/com/android/phone/CallTime.java b/src/com/android/phone/CallTime.java
//Synthetic comment -- index 39a69af..df3f74a 100644

//Synthetic comment -- @@ -17,6 +17,9 @@
package com.android.phone;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
//Synthetic comment -- @@ -42,6 +45,8 @@

private static int sProfileState = PROFILE_STATE_NONE;

private Call mCall;
private long mLastReportedTime;
private boolean mTimerRunning;
//Synthetic comment -- @@ -49,13 +54,28 @@
private PeriodicTimerCallback mTimerCallback;
private OnTickListener mListener;

interface OnTickListener {
void onTickForCallTimeElapsed(long timeElapsed);
}

    public CallTime(OnTickListener listener) {
mListener = listener;
mTimerCallback = new PeriodicTimerCallback();
}

/**
//Synthetic comment -- @@ -167,9 +187,13 @@
if (PROFILE && isTraceRunning()) {
stopTrace();
}

            mTimerRunning = false;
            periodicUpdateTimer();
}
}








