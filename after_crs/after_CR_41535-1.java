/*MtpData bmp format file can't recognize when copy bmp into DUT

add support for bmp format.

Change-Id:I67d4703b43cd32abad638bae57bbceb65b4be6deAuthor: bo huang <bo.b.huang@intel.com>
Signed-off-by: bo huang <bo.b.huang@intel.com>
Signed-off-by: Wu, Hao <hao.wu@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 24555*/




//Synthetic comment -- diff --git a/media/java/android/mtp/MtpDatabase.java b/media/java/android/mtp/MtpDatabase.java
//Synthetic comment -- index 7532d79..a0325dd 100755

//Synthetic comment -- @@ -492,6 +492,7 @@
MtpConstants.FORMAT_MPEG,
MtpConstants.FORMAT_EXIF_JPEG,
MtpConstants.FORMAT_TIFF_EP,
            MtpConstants.FORMAT_BMP,
MtpConstants.FORMAT_GIF,
MtpConstants.FORMAT_JFIF,
MtpConstants.FORMAT_PNG,







