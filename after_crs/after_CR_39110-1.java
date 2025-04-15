/*Ignore xxhigh density in Asset Studio.

Change-Id:I1cf4c6e2f28759fa87652e44b9343ae985fed9f9*/




//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java b/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java
//Synthetic comment -- index 0124000..5d18d4d 100644

//Synthetic comment -- @@ -163,7 +163,8 @@
if (!density.isValidValueForDevice()) {
continue;
}
            if (density == Density.TV || density == Density.XXHIGH) {
                // TODO don't manually check and instead gracefully handle missing stencils.
// Not yet supported -- missing stencil image
continue;
}







