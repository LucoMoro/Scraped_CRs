/*Ignore xxhigh density in Asset Studio.

(cherry picked from commit 44869ed53465d6d61a8148718fde0ea1d484322f)

Change-Id:Id09a64671a9f38bd3314b2132b4201c72c8de271*/
//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java b/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java
//Synthetic comment -- index 0124000..5d18d4d 100644

//Synthetic comment -- @@ -163,7 +163,8 @@
if (!density.isValidValueForDevice()) {
continue;
}
            if (density == Density.TV) {
// Not yet supported -- missing stencil image
continue;
}







