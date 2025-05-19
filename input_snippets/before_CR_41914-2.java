
//<Beginning of snippet n. 0>


@Override
public void onOffsetsChanged(float xOffset, float yOffset,
float xStep, float yStep, int xPixels, int yPixels) {
            mRenderer.setOffset(xOffset, yOffset, xPixels, yPixels);
}

@Override
@Override
public Bundle onCommand(String action, int x, int y, int z,
Bundle extras, boolean resultRequested) {
            return mRenderer.onCommand(action, x, y, z, extras, resultRequested);
}

}

//<End of snippet n. 0>








