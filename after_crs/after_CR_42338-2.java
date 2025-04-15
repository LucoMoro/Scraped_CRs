/*Performance increase in thumbnail handling

Allow for 160*120 thumbnails which is what cameras commonly
generates. The constants for the micro thumbnail were set to other
values, resulting in recalculations of the thumbnais, which
takes time.

This change only affects the maximum size of the temporary image,
when choosing whether to use the EXIF thumbnail or decoding and
downsampling the full image. Without this change, it will choose
an x16 downsampling of the full image over an x2 downsampling of
the EXIF thumbnail, after the change it will prefer the EXIF
thumbnail.

Cf the DCF specifications athttp://www.exif.org/dcf.PDF,"3.3.6. DCF basic thumbnail data structure, (C) Pixel count"

Tested by running DDMS and measuring the time required to create
a thumbnail. This was 220-280 ms prior to change, < 20 ms after.

Change-Id:I59c753493f947e920bad3fde5eeed5d49d509863*/




//Synthetic comment -- diff --git a/media/java/android/media/ThumbnailUtils.java b/media/java/android/media/ThumbnailUtils.java
//Synthetic comment -- index 8eb9332..756638c 100644

//Synthetic comment -- @@ -48,7 +48,7 @@

/* Maximum pixels size for created bitmap. */
private static final int MAX_NUM_PIXELS_THUMBNAIL = 512 * 384;
    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 160 * 120;
private static final int UNCONSTRAINED = -1;

/* Options used internally. */







