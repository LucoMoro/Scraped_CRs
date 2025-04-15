/*Browser: web access failed after user start search in “confirm popup dispalying"

When pop up the _to_save_password dialog, it will block
the thread which is loading the page, if we want to use search to load a
new page, we should cancel the dialog in first but not only dismiss it.

Change-Id:I17a053134cadb886e8cd2e757bc33f086b57a0dcAuthor: Yuan Zhao <yuanx.zhao@intel.com>
Signed-off-by: Yuan Zhao <yuanx.zhao@intel.com>
Signed-off-by: Weiwei Ji <weiweix.ji@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 19120*/




//Synthetic comment -- diff --git a/core/java/android/app/Dialog.java b/core/java/android/app/Dialog.java
//Synthetic comment -- index b3d99c5..4c2e539 100644

//Synthetic comment -- @@ -953,7 +953,12 @@
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







