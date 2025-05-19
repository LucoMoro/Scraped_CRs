//<Beginning of snippet n. 0>
try {
    mDimSurface = new Surface(session, 0, -1, 16, 16, PixelFormat.TRANSLUCENT, Surface.FX_SURFACE_DIM);
    mDimSurface.setAlpha(0);
    Log.d(TAG, "Dim surface created with alpha = 0");
} catch (Exception e) {
    Log.e(TAG, "Exception creating Dim surface", e);
    mDimSurface = new Surface(session, 0, -1, 16, 16, PixelFormat.TRANSLUCENT, Surface.FX_SURFACE_DIM);
    mDimSurface.setAlpha(0);
    Log.d(TAG, "Dim surface created in catch block with alpha = 0");
}
//<End of snippet n. 0>