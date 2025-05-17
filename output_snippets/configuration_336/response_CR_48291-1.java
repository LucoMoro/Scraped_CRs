//<Beginning of snippet n. 0>
InputStream input = null;
try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
    int quality = MessageUtils.IMAGE_COMPRESSION_QUALITY;
    int attempts = 1;
    BitmapFactory.Options options = new BitmapFactory.Options();
    
    b.compress(CompressFormat.JPEG, quality, os);
    int jpgFileSize = os.size();
    Log.v(TAG, "getResizedImageData: compress(1) w/ quality=" + quality + " size=" + jpgFileSize);

    while (jpgFileSize > DESIRED_SIZE && attempts < MAX_ATTEMPTS && quality > MIN_QUALITY) {  // Assume MIN_QUALITY is defined
        quality -= QUALITY_DECREMENT;  // Assume QUALITY_DECREMENT is defined
        os.reset();
        b.compress(CompressFormat.JPEG, quality, os);
        jpgFileSize = os.size();
        Log.v(TAG, "getResizedImageData: compress(" + (attempts + 1) + ") w/ quality=" + quality + " size=" + jpgFileSize);
        attempts++;
    }
    
    // Call to handle the resulting ByteArrayOutputStream
} catch (java.lang.OutOfMemoryError e) {
    Log.e(TAG, e.getMessage(), e);
    return null;
} catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage(), e);
    return null;
} catch (Exception e) {  // Adding a general exception catch for other potential I/O exceptions
    Log.e(TAG, "Exception: " + e.getMessage(), e);
    return null;
} finally {
    if (b != null && !b.isRecycled()) {  // Ensure bitmap is not recycled prematurely
        b.recycle();
    }
}
//<End of snippet n. 0>