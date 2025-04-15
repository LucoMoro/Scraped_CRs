/*Fix uncaught exception which caused cyclic reboot if too many widgets are created

The bitmap facilities can throw an 'OutOfMemoryError' exception if called
when there is insufficient VM heap available to fulfill the allocation request.
Add a try/catch handler to convert the uncaught exception into a caught one,
this prevents the normal crash. It does rely on existing null-pointer traps.*/
//Synthetic comment -- diff --git a/core/java/android/widget/RemoteViews.java b/core/java/android/widget/RemoteViews.java
//Synthetic comment -- index 6771711..91c81a7 100644

//Synthetic comment -- @@ -324,7 +324,11 @@
this.value = Uri.CREATOR.createFromParcel(in);
break;
case BITMAP:
                    this.value = Bitmap.CREATOR.createFromParcel(in);
break;
default:
break;








//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/Drawable.java b/graphics/java/android/graphics/drawable/Drawable.java
//Synthetic comment -- index 6a7b2d1..6a3922f 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.util.StateSet;
import android.util.Xml;
import android.util.TypedValue;

/**
* A Drawable is a general abstraction for "something that can be drawn."  Most
//Synthetic comment -- @@ -694,7 +695,12 @@
// drawn to the screen.
if (opts == null) opts = new BitmapFactory.Options();
opts.inScreenDensity = DisplayMetrics.DENSITY_DEVICE;
        Bitmap  bm = BitmapFactory.decodeResourceStream(res, value, is, pad, opts);
if (bm != null) {
byte[] np = bm.getNinePatchChunk();
if (np == null || !NinePatch.isNinePatchChunk(np)) {







