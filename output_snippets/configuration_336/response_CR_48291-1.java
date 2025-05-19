//<Beginning of snippet n. 0>
try (InputStream input = new FileInputStream(new URI("file://path_to_image"));
     ByteArrayOutputStream os = new ByteArrayOutputStream()) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(input, null, options);
    
    if (options.outHeight > SOME_MAX_HEIGHT || options.outWidth > SOME_MAX_WIDTH) {
        options.inSampleSize = calculateInSampleSize(options, SOME_MAX_WIDTH, SOME_MAX_HEIGHT);
        options.inJustDecodeBounds = false;
    }

    input.close(); // Ensure the InputStream is closed before using it again
    input = new FileInputStream(new URI("file://path_to_image"));
    Bitmap b = BitmapFactory.decodeStream(input, null, options);
    
    int quality = MessageUtils.IMAGE_COMPRESSION_QUALITY;
    b.compress(Bitmap.CompressFormat.JPEG, quality, os);
    int jpgFileSize = os.size();
    Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);

    if (jpgFileSize > SOME_THRESHOLD) {
        quality = adjustQualityForSize(jpgFileSize);
        os.reset(); // Resetting the output stream before compressing again
        b.compress(Bitmap.CompressFormat.JPEG, quality, os);
    }
} catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage(), e);
    return null;
} catch (OutOfMemoryError e) {
    Log.e(TAG, "OutOfMemoryError: " + e.getMessage(), e);
    return null;
} catch (Exception e) {
    Log.e(TAG, "Exception: " + e.getMessage(), e);
    return null;
}
//<End of snippet n. 0>