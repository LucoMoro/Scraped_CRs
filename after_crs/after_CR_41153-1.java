/*Mms: Add a check on the Media model

In case there is no image and no video in the slideshow, the
media model might be null.
Add a check to avoid a null pointer access.

Change-Id:I712a1d4da4a7197af1d8f3da339051cd8f40c1f2Author: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28037*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 5d6afb7..fb608c6 100644

//Synthetic comment -- @@ -532,6 +532,10 @@
mm = slide.getVideo();
}

        if (mm == null) {
            return;
        }

Intent intent = new Intent(Intent.ACTION_VIEW);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
intent.putExtra("SingleItemOnly", true); // So we don't see "surrounding" images in Gallery







