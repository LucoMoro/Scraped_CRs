//<Beginning of snippet n. 0>
InputStream input = null;
ByteArrayOutputStream os = null;
try {
    int attempts = 1;
    int sampleSize = 1;
    BitmapFactory.Options options = new BitmapFactory.Options();
    os = new ByteArrayOutputStream();

    // Assuming 'b' is defined and is a Bitmap
    b.compress(CompressFormat.JPEG, quality, os);
    int jpgFileSize = os.size();
    Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);
    
    if (jpgFileSize > MAX_SIZE_THRESHOLD) {
        options.inSampleSize = calculateSampleSize(jpgFileSize);
        input = new ByteArrayInputStream(os.toByteArray());
        Bitmap resizedBitmap = BitmapFactory.decodeStream(input, null, options);
        b = resizedBitmap;
    }

} catch (java.lang.OutOfMemoryError e) {
    Log.e(TAG, "Out of Memory: " + e.getMessage(), e);
    return null;
} catch (IOException e) {
    Log.e(TAG, "I/O Error: " + e.getMessage(), e);
} finally {
    try {
        if (os != null) {
            os.close();
        }
        if (input != null) {
            input.close();
        }
    } catch (IOException e) {
        Log.e(TAG, "Error closing streams", e);
    }
}
//<End of snippet n. 0>