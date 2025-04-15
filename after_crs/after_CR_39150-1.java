/*Fix wrong condition.

Should check width and height, not width and width.

Change-Id:Ie84d3605b2a7a0f4776adb876608ea92f56817c4*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/IconUtilities.java b/policy/src/com/android/internal/policy/impl/IconUtilities.java
//Synthetic comment -- index e997355..a47c904 100644

//Synthetic comment -- @@ -130,7 +130,7 @@
int sourceWidth = icon.getIntrinsicWidth();
int sourceHeight = icon.getIntrinsicHeight();

        if (sourceWidth > 0 && sourceHeight > 0) {
// There are intrinsic sizes.
if (width < sourceWidth || height < sourceHeight) {
// It's too big, scale it down.







