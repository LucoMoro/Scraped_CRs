/*IME: dismiss IME select dialog on finishInput

The IME select dialog could become irresponsive after rotating the
device. This commit workarounds the issue by dismiss the IME select
dialog on finishInput().

Change-Id:Iff895ef60ec56f8d395edf3347aa21df1a07958eAuthor: Wang Zhifeng <zhifeng.wang@intel.com>
Signed-off-by: Wang Zhifeng <zhifeng.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57103*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index c9ff595..d876571 100644

//Synthetic comment -- @@ -1248,6 +1248,9 @@

@Override
public void finishInput(IInputMethodClient client) {
        synchronized (mMethodMap) {
            hideInputMethodMenuLocked();
        }
}

@Override







