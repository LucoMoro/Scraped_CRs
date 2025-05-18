//<Beginning of snippet n. 0>

@Override
public void onOffsetsChanged(float xOffset, float yOffset,
                              float xStep, float yStep, int xPixels, int yPixels) {
    if (mRenderer == null) {
        Log.e("WaterLiveWallpaper", "mRenderer is null in onOffsetsChanged, attempting reinitialization.");
        initializeRenderer();
    }
    if (mRenderer != null) {
        mRenderer.setOffset(xOffset, yOffset, xPixels, yPixels);
    } else {
        Log.e("WaterLiveWallpaper", "mRenderer remains null in onOffsetsChanged after reinitialization.");
    }
}

@Override
public Bundle onCommand(String action, int x, int y, int z,
                        Bundle extras, boolean resultRequested) {
    if (mRenderer == null) {
        Log.e("WaterLiveWallpaper", "mRenderer is null in onCommand, attempting reinitialization.");
        initializeRenderer();
    }
    if (mRenderer != null) {
        return mRenderer.onCommand(action, x, y, z, extras, resultRequested);
    } else {
        Log.e("WaterLiveWallpaper", "mRenderer is still null in onCommand after reinitialization, returning error bundle.");
        Bundle errorBundle = new Bundle();
        errorBundle.putString("error", "Renderer not initialized.");
        return errorBundle;
    }
}

private synchronized void initializeRenderer() {
    if (mRenderer == null) {
        // Logic to initialize mRenderer should be added here
    }
}

//<End of snippet n. 0>