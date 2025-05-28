//<Beginning of snippet n. 0>
try (InputStream input = new FileInputStream(new URI("file://path_to_image")); 
     ByteArrayOutputStream os = new ByteArrayOutputStream()) {
    int attempts = 1;
    int sampleSize = 1;
    BitmapFactory.Options options = new BitmapFactory.Options();
    int quality = MessageUtils.IMAGE_COMPRESSION_QUALITY;

    b.compress(CompressFormat.JPEG, quality, os);
    int jpgFileSize = os.size();
    Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);

    if (jpgFileSize > SOME_THRESHOLD) {
        quality = adjustQualityForSize(jpgFileSize);
        b.compress(CompressFormat.JPEG, quality, os);
    }
} catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage(), e);
    return null;
} catch (java.lang.OutOfMemoryError e) {
    Log.e(TAG, "OutOfMemoryError: " + e.getMessage(), e);
    return null;
} catch (Exception e) {
    Log.e(TAG, "Exception: " + e.getMessage(), e);
    return null;
}
//<End of snippet n. 0>