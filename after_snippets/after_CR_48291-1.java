
//<Beginning of snippet n. 0>


}

InputStream input = null;
        ByteArrayOutputStream os = null;
try {
int attempts = 1;
int sampleSize = 1;
BitmapFactory.Options options = new BitmapFactory.Options();
// Compress the image into a JPG. Start with MessageUtils.IMAGE_COMPRESSION_QUALITY.
// In case that the image byte size is still too large reduce the quality in
// proportion to the desired byte size.
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
os = new ByteArrayOutputStream();
b.compress(CompressFormat.JPEG, quality, os);
int jpgFileSize = os.size();
Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);
}

                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
os = new ByteArrayOutputStream();
b.compress(CompressFormat.JPEG, quality, os);
}
} catch (java.lang.OutOfMemoryError e) {
Log.e(TAG, e.getMessage(), e);
return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
}
}
}

//<End of snippet n. 0>








