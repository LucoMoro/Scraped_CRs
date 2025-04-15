/*Browser: web access failed after user start search in “confirm popup
dispalying

When pop up the _to_save_password dialog, it will block
the thread which is loading the page, if we want to use search to load a
new page, we should cancel the dialog in first but not only dismiss it.

Change-Id:Ia03d622df0ffe1e4f8ca7bccd42093307c2b0427Author: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Yuan Zhao <yuanx.zhao@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 19120*/




//Synthetic comment -- diff --git a/core/java/android/app/Dialog.java b/core/java/android/app/Dialog.java
//Synthetic comment -- index 2cc3b02..d467118 100644

//Synthetic comment -- @@ -949,7 +949,12 @@
final ComponentName appName = getAssociatedActivity();
if (appName != null && searchManager.getSearchableInfo(appName) != null) {
searchManager.startSearch(null, false, appName, null, false);
            if (mCancelable) {
                //here should cancel the dialog, if not, if it is an alertdialog, it will block thread always.
                cancel();
            } else {
                dismiss();
            }
return true;
} else {
return false;







