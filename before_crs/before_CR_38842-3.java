/*There is a memory leak in Gallery2 when do some doodle on photos.

you can reproduce the issue with below actions:
Gallery2 -> open photo -> doodle it -> save -> doodle again ->
repeat this action sometimes -> go back to see the old doodles.
Above actions may cause OutOfMemory exception for there will be
duplicated origin photos in the momery, i recycle the duplicated
photos that solve this memory leak.

Change-Id:Idfeea7250c2184ca70bb864a2707a5152d3e5927*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/photoeditor/filters/DoodleFilter.java b/src/com/android/gallery3d/photoeditor/filters/DoodleFilter.java
//Synthetic comment -- index 277e06d..c8c0334 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
Effect effect = getEffect(EffectFactory.EFFECT_BITMAPOVERLAY);
effect.setParameter("bitmap", bitmap);
effect.apply(src.texture(), src.width(), src.height(), dst.texture());
}

@Override







