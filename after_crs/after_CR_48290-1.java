/*Mms: Add a check on the Media model

In case there is no image and no video in the slideshow, the
media model might be null.
Add a check to avoid a null pointer access.

Change-Id:I55d63b226835acfa53199eee0d706b8c5ae553c2Author: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28037*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 502bfde..6af5662 100644

//Synthetic comment -- @@ -574,6 +574,10 @@
mm = slide.getVideo();
}

        if (mm == null) {
            return;
        }

Intent intent = new Intent(Intent.ACTION_VIEW);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
intent.putExtra("SingleItemOnly", true); // So we don't see "surrounding" images in Gallery







