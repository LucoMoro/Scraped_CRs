//<Beginning of snippet n. 0>
long time = exif.getGpsDateTime();
if (time != -1) {
    values.put(Images.Media.DATE_TAKEN, time);
    Log.i("ExifData", "Using GPS DateTime: " + time);
} else {
    long originalTime = exif.getOriginalDateTime();
    if (originalTime != -1) {
        values.put(Images.Media.DATE_TAKEN, originalTime);
        Log.i("ExifData", "Using Original DateTime: " + originalTime);
    } else {
        Log.e("ExifData", "Error: Both GPS and Original DateTime are unavailable.");
        throw new IllegalStateException("Both datetime sources are unavailable.");
    }
}

int orientation = exif.getAttributeInt(
//<End of snippet n. 0>