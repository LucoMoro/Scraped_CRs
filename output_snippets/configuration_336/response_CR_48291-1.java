//<Beginning of snippet n. 0>
InputStream input = null;
try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
    int attempts = 1;
    int sampleSize = 1;
    BitmapFactory.Options options = new BitmapFactory.Options();
    boolean compressedSuccessfully = false;

    while (attempts <= 3 && !compressedSuccessfully) {
        b.compress(CompressFormat.JPEG, quality, os);
        int jpgFileSize = os.size();
        Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);

        if (jpgFileSize < MAX_BYTE_SIZE) {
            compressedSuccessfully = true;
        } else {
            quality = Math.max(0, quality - 10); // Reduce quality for next attempt
            os.reset(); // Clear the stream for the next attempt
            attempts++;
        }
    }

    if (!compressedSuccessfully) {
        Log.e(TAG, "Failed to compress image within the attempts");
        return null;
    }
    // Further processing with the compressed image data in 'os'
} catch (java.lang.OutOfMemoryError e) {
    Log.e(TAG, e.getMessage(), e);
    return null;
} catch (IOException e) {
    Log.e(TAG, "I/O error during image processing", e);
    return null;
}
//<End of snippet n. 0>