/*Add WEBP to the list of Image formats that support Compression.

Note: The integrator of this change to Android internal code-repo will have to
run one extra step 'make update-api' to update 'api/current.txt' file
corresponding to approved API. The AOSP branch didn't have this file,
hence I could not add the same to this change. The updated file
'api/current.txt' has to be submitted along with this change.

Change-Id:I0af509d4526526293b59301e341db9ee99fa4117*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/Bitmap.java b/graphics/java/android/graphics/Bitmap.java
//Synthetic comment -- index dd83c3b..a9ac1d3 100644

//Synthetic comment -- @@ -546,7 +546,8 @@
*/
public enum CompressFormat {
JPEG    (0),
        PNG     (1),
        WEBP    (2);

CompressFormat(int nativeInt) {
this.nativeInt = nativeInt;







