/*PorterDuff Mode comment is inconsistent for SRC_OVER, DST_OVER

I removed Rc.

Change-Id:If3aae9b6e5612358ec7308e633ee91239e3f2af2Signed-off-by: NoraBora <noranbora@gmail.com>*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/PorterDuff.java b/graphics/java/android/graphics/PorterDuff.java
//Synthetic comment -- index 2ef1662..5d7fe76 100644

//Synthetic comment -- @@ -26,9 +26,9 @@
SRC         (1),
/** [Da, Dc] */
DST         (2),
        /** [Sa + (1 - Sa)*Da, Rc = Sc + (1 - Sa)*Dc] */
SRC_OVER    (3),
        /** [Sa + (1 - Sa)*Da, Rc = Dc + (1 - Da)*Sc] */
DST_OVER    (4),
/** [Sa * Da, Sc * Da] */
SRC_IN      (5),







