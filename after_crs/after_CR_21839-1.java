/*Add bit masks for support of LCDRenderText and BitmapText

Currently applications cannot use Bitmap font.
Because applications have no way to set
EMBEDDED_BITMAP_TEXT_FLAG = 0x400 in Paint class.
In order to use Bitmap font, kEmbeddedBitmapText_Flag=0x400 must be
set in external/skia/core/SkPaint.h by using SetFlags().
But Paint class doesn't support this flag and application cannot set
 it now.
So I need to add EMBEDDED_BITMAP_TEXT_FLAG to Paint.java.

Futhermore, LCD_RENDER_TEXT_FLAG should be defined too.
kLCDRenderText_Flag=0x200 exists in external/skia/core/SkPaint.h, but
 application cannot set the flag because it's not defined
in Paint.java.

Change-Id:I55178bbee1a4b7b5932ecd548d063f67c3a9e750*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/Paint.java b/graphics/java/android/graphics/Paint.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3e3f87b..f8e225c

//Synthetic comment -- @@ -71,6 +71,10 @@
public static final int SUBPIXEL_TEXT_FLAG  = 0x80;
/** bit mask for the flag enabling device kerning for text */
public static final int DEV_KERN_TEXT_FLAG  = 0x100;
    /** bit mask for the flag enabling lcd rendering */
    public static final int LCD_RENDER_TEXT_FLAG = 0x200;
    /** bit mask for the flag enabling embedded bitmap text */
    public static final int EMBEDDED_BITMAP_TEXT_FLAG = 0x400;

// we use this when we first create a paint
private static final int DEFAULT_PAINT_FLAGS = DEV_KERN_TEXT_FLAG;







