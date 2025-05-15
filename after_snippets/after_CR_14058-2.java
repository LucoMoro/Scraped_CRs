
//<Beginning of snippet n. 0>


try {
mDimSurface = new Surface(session, 0, -1, 16, 16, PixelFormat.OPAQUE,
Surface.FX_SURFACE_DIM);
                    mDimSurface.setAlpha(0.0f);
} catch (Exception e) {
Log.e(TAG, "Exception creating Dim surface", e);
}

//<End of snippet n. 0>








