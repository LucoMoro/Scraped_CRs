/*Fix scaling of ninepatches.

Change-Id:Id9888dec034d3010de35aa61ee5f651a672fc410*/
//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatchChunk.java b/ninepatch/src/com/android/ninepatch/NinePatchChunk.java
//Synthetic comment -- index 6dca61e..20abaeb 100644

//Synthetic comment -- @@ -86,7 +86,7 @@
graphics2D = (Graphics2D) graphics2D.create();

// scale and transform
                float densityScale = (float) destDensity / srcDensity;

// translate/rotate the canvas.
graphics2D.translate(x, y);







