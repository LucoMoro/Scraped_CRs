/*Mms: Fix javacrash in com.android.mms due to memory leak

when the UriImage resize the picture. It will not close the
InputStream and OutputStream.

Change-Id:I80fa1eb3726308fe75d74357e0b9782759de3393Author: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28037*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/UriImage.java b/src/com/android/mms/ui/UriImage.java
//Synthetic comment -- index c74764b..da3e8f1 100644

//Synthetic comment -- @@ -278,8 +278,8 @@
}

InputStream input = null;
        ByteArrayOutputStream os = null;
try {
int attempts = 1;
int sampleSize = 1;
BitmapFactory.Options options = new BitmapFactory.Options();
//Synthetic comment -- @@ -354,6 +354,13 @@
// Compress the image into a JPG. Start with MessageUtils.IMAGE_COMPRESSION_QUALITY.
// In case that the image byte size is still too large reduce the quality in
// proportion to the desired byte size.
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
os = new ByteArrayOutputStream();
b.compress(CompressFormat.JPEG, quality, os);
int jpgFileSize = os.size();
//Synthetic comment -- @@ -367,6 +374,13 @@
Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);
}

                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
os = new ByteArrayOutputStream();
b.compress(CompressFormat.JPEG, quality, os);
}
//Synthetic comment -- @@ -400,6 +414,21 @@
} catch (java.lang.OutOfMemoryError e) {
Log.e(TAG, e.getMessage(), e);
return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
}
}
}







