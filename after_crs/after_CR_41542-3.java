/*MountService: turn on screen when SD card insert/remove

Turn on screen when user insert/remove a SD card

Change-Id:I35115ad0731d5c35d853c1ab776d74a24ce78181Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 45584*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 04267a3..d8999ef 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
//Synthetic comment -- @@ -111,6 +112,11 @@
/** Maximum number of ASEC containers allowed to be mounted. */
private static final int MAX_CONTAINERS = 250;

    /* power manager wake lock to wake up screen */
    private final PowerManager mPowerManager;
    private final PowerManager.WakeLock mWakeLock;
    private final Handler mWakeLockHandler;

/*
* Internal vold volume state constants
*/
//Synthetic comment -- @@ -740,6 +746,10 @@
int major = -1;
int minor = -1;

            mWakeLockHandler.removeCallbacks(DiskScreenCtrl);
            mWakeLock.acquire();
            mWakeLockHandler.postDelayed(DiskScreenCtrl, 10000);

try {
String devComp = cooked[6].substring(1, cooked[6].length() -1);
String[] devTok = devComp.split(":");
//Synthetic comment -- @@ -1185,6 +1195,15 @@
mObbActionHandler = new ObbActionHandler(mHandlerThread.getLooper());

/*
         * get the msg from vold and handle it with wakelock
         */
        mPowerManager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP, "sd_screen_lock");
        mWakeLock.setReferenceCounted(false);
        mWakeLockHandler = new Handler();

        /*
* Create the connection to vold with a maximum queue of twice the
* amount of containers we'd ever expect to have. This keeps an
* "asec list" from blocking a thread repeatedly.
//Synthetic comment -- @@ -2480,4 +2499,12 @@
mConnector.monitor();
}
}

    private Runnable DiskScreenCtrl = new Runnable() {
        public void run() {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
    };
}







