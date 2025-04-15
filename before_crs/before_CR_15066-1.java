/*Canvas.drawVertices: correct color range-check

The range-check in drawVertices previously checked that the color
count was equal to the vertex count. However, the vertex count is
specified in array elements, and there's two array elements per
vertex for the positions, but only one array element per vertex
for the colors.

Correct the code so the check is consistent with the element count.

Change-Id:Id043f1003f9245f18adc91bac49dc33727ca765e*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/Canvas.java b/graphics/java/android/graphics/Canvas.java
//Synthetic comment -- index 345f810..76cde73 100644

//Synthetic comment -- @@ -1221,7 +1221,7 @@
checkRange(texs.length, texOffset, vertexCount);
}
if (colors != null) {
            checkRange(colors.length, colorOffset, vertexCount);
}
if (indices != null) {
checkRange(indices.length, indexOffset, indexCount);







