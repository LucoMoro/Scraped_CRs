/*the vertex index should be "first + i".*/




//Synthetic comment -- diff --git a/opengl/java/android/opengl/GLLogWrapper.java b/opengl/java/android/opengl/GLLogWrapper.java
//Synthetic comment -- index 4119bf8..f332448 100644

//Synthetic comment -- @@ -1517,7 +1517,7 @@
arg("count", count);
startLogIndices();
for (int i = 0; i < count; i++) {
            doElement(mStringBuilder, i, first + i);
}
endLogIndices();
end();







