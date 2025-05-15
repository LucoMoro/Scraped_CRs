//<Beginning of snippet n. 0>
@Override
public void onOffsetsChanged(float xOffset, float yOffset,
                             float xStep, float yStep, int xPixels, int yPixels) {
    if (mRenderer != null) {
        mRenderer.setOffset(xOffset, yOffset, xPixels, yPixels);
    } else {
        Log.e("LiveWallpaper", "mRenderer is null in onOffsetsChanged.");
    }
}

@Override
public Bundle onCommand(String action, int x, int y, int z,
                        Bundle extras, boolean resultRequested) {
    if (mRenderer != null) {
        if (extras != null) {
            return mRenderer.onCommand(action, x, y, z, extras, resultRequested);
        } else {
            Log.e("LiveWallpaper", "extras Bundle is null in onCommand.");
        }
    } else {
        Log.e("LiveWallpaper", "mRenderer is null in onCommand.");
    }
    return null; // Return null if mRenderer or extras are null.
}
//<End of snippet n. 0>