/*Mms: "Add slide" item is not displayed during Edit slideshow

With this patch the "Add Slide" item is added as the last item
when some items are removed during edit slide show.

Change-Id:I57fa159183b87628ff2908a4ad299b97031f2d41Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 7875*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowEditActivity.java b/src/com/android/mms/ui/SlideshowEditActivity.java
//Synthetic comment -- index 6879f48..27dceb1 100644

//Synthetic comment -- @@ -341,6 +341,7 @@

try {
initSlideList();
} catch (MmsException e) {
Log.e(TAG, "Failed to initialize the slide-list.", e);
finish();







