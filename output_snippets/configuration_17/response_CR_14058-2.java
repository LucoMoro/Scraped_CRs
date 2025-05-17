//<Beginning of snippet n. 0>
try {
    mDimSurface = new Surface(session, 0, -1, 16, 16, PixelFormat.TRANSLUCENT,
    Surface.FX_SURFACE_DIM);
    if (mDimSurface != null && mDimSurface.isValid()) {
        // Set alpha to 0 for transparency
        mDimSurface.setAlpha(0);
    } else {
        Log.e(TAG, "Surface creation failed or surface is invalid");
    }
} catch (Exception e) {
    Log.e(TAG, "Exception creating Dim surface", e);
}
//<End of snippet n. 0>