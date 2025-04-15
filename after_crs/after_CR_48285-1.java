/*Mms: Upon already initialized, return immediately

If the sInstance parameter is already initialized then there is
no need to create a new one, this will lead to memory leaks.
Just return immediately.

Change-Id:I68dc53766b5a7e37c591ad253fbd65cf6416658aAuthor: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28037*/




//Synthetic comment -- diff --git a/src/com/android/mms/util/RateController.java b/src/com/android/mms/util/RateController.java
//Synthetic comment -- index 13e90de..b2e0cdc 100644

//Synthetic comment -- @@ -79,6 +79,7 @@

if (sInstance != null) {
Log.w(TAG, "Already initialized.");
            return;
}
sInstance = new RateController(context);
}







