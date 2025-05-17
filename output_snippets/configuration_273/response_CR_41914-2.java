//<Beginning of snippet n. 0>
@Override
public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
    if (mRenderer != null) {
        mRenderer.setOffset(xOffset, yOffset, xPixels, yPixels);
    } else {
        // Log error or throw exception
    }
}

@Override
public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
    if (mRenderer != null) {
        return mRenderer.onCommand(action, x, y, z, extras, resultRequested);
    } else {
        // Log error or throw exception
        return null; // or appropriate handling
    }
}
//<End of snippet n. 0>