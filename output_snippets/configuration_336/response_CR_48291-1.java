//<Beginning of snippet n. 0>
InputStream input = null;
ByteArrayOutputStream os = null;
try {
    int attempts = 1;
    int sampleSize = 1;
    BitmapFactory.Options options = new BitmapFactory.Options();
    os = new ByteArrayOutputStream();
    
    if (b != null && b.compress(CompressFormat.JPEG, quality, os)) {
        int jpgFileSize = os.size();
        Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);
    } else {
        Log.e(TAG, "Compression failed or bitmap is null.");
        return null;
    }
} catch (java.lang.OutOfMemoryError e) {
    Log.e(TAG, e.getMessage(), e);
    return null;
} finally {
    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing ByteArrayOutputStream", e);
        }
    }
}
//<End of snippet n. 0>