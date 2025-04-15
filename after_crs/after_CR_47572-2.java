/*Mms: Pre-view pops up twice after tapping preview button

The flag of the intent needs to be set to single top.

Change-Id:Ibc0fe202bd500054454b28e7caef915ec41e3bc5Author: Jianli Zhang <jianli.zhang@borqs.com>
Signed-off-by: Jianli Zhang <jianli.zhang@borqs.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by:Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 8583*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 57075f8..502bfde 100644

//Synthetic comment -- @@ -904,6 +904,7 @@
// Launch the slideshow activity to play/view.
Intent intent = new Intent(context, SlideshowActivity.class);
intent.setData(msgUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
if (requestCode > 0 && context instanceof Activity) {
((Activity)context).startActivityForResult(intent, requestCode);
} else {







