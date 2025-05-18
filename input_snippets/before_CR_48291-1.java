
//<Beginning of snippet n. 0>


}

InputStream input = null;
try {
            ByteArrayOutputStream os = null;
int attempts = 1;
int sampleSize = 1;
BitmapFactory.Options options = new BitmapFactory.Options();
// Compress the image into a JPG. Start with MessageUtils.IMAGE_COMPRESSION_QUALITY.
// In case that the image byte size is still too large reduce the quality in
// proportion to the desired byte size.
os = new ByteArrayOutputStream();
b.compress(CompressFormat.JPEG, quality, os);
int jpgFileSize = os.size();
Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);
}

os = new ByteArrayOutputStream();
b.compress(CompressFormat.JPEG, quality, os);
}
} catch (java.lang.OutOfMemoryError e) {
Log.e(TAG, e.getMessage(), e);
return null;
}
}
}

//<End of snippet n. 0>








