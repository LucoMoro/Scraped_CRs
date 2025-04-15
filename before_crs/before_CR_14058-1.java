/*Set alpha value for newly created dim surface.

Newly created dim surface has alpha set to 1 (opaque),
but it is assumed in dim animation code that it is 0 (transparent).
When new dim surface is created and expected dim value is calculated to 0
then alpha is never set making screen black (dut to default aplha=1)
when dim surface is shown.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 24caf1f..b49b0c7 100644

//Synthetic comment -- @@ -10985,6 +10985,7 @@
try {
mDimSurface = new Surface(session, 0, -1, 16, 16, PixelFormat.OPAQUE,
Surface.FX_SURFACE_DIM);
} catch (Exception e) {
Log.e(TAG, "Exception creating Dim surface", e);
}







