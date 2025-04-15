/*MountService: turn on Screen when insert/remove a SD card

Change-Id:Ie10d80125463097080abbb642cde50f8e5c433b5Author: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 45584*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index c512bc1..2a50d57 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
//Synthetic comment -- @@ -118,6 +119,11 @@
/** Maximum number of ASEC containers allowed to be mounted. */
private static final int MAX_CONTAINERS = 250;

/*
* Internal vold volume state constants
*/
//Synthetic comment -- @@ -787,6 +793,10 @@
int major = -1;
int minor = -1;

try {
String devComp = cooked[6].substring(1, cooked[6].length() -1);
String[] devTok = devComp.split(":");
//Synthetic comment -- @@ -1309,6 +1319,15 @@
mObbActionHandler = new ObbActionHandler(mHandlerThread.getLooper());

/*
* Create the connection to vold with a maximum queue of twice the
* amount of containers we'd ever expect to have. This keeps an
* "asec list" from blocking a thread repeatedly.
//Synthetic comment -- @@ -2676,4 +2695,11 @@
mConnector.monitor();
}
}
}







